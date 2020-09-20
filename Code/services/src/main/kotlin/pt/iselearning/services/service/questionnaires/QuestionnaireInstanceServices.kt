package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.AnswerModel
import pt.iselearning.services.models.ChallengeAnswerModel
import pt.iselearning.services.models.ChallengeAnswerOutputModel
import pt.iselearning.services.models.questionnaire.QuestionnaireInstanceModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireInstanceOutputModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.service.challenge.ChallengeService
import pt.iselearning.services.util.checkIfLoggedUserIsResourceOwner
import pt.iselearning.services.util.checkIfQuestionnaireExists
import pt.iselearning.services.util.checkIfQuestionnaireInstanceExists
import pt.iselearning.services.util.checkQuestionnaireInstanceTimeout
import java.util.*
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire instance object
 */
@Validated
@Service
class QuestionnaireInstanceServices(
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        private val questionnaireAnswerRepository: QuestionnaireAnswerRepository,
        private val questionnaireChallengeRepository : QuestionnaireChallengeRepository,
        private val questionnaireServices: QuestionnaireServices,
        private val challengeService: ChallengeService,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a questionnaire instance.
     *
     * @param questionnaireInstanceModel object information
     * @return created questionnaire instance
     */
    @Validated
    fun createQuestionnaireInstance(@Valid questionnaireInstanceModel: QuestionnaireInstanceModel, loggedUser: User): QuestionnaireInstance {
        val questionnaireInstance = convertToEntity(questionnaireInstanceModel)
        val questionnaireInstanceParent = questionnaireServices.getQuestionnaireById(questionnaireInstance.questionnaireId!!)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaireInstanceParent.creatorId!!)

        // extends parent properties by default
        questionnaireInstance.description = questionnaireInstanceParent.description
        questionnaireInstance.timer = questionnaireInstanceParent.timer

        // updates to model properties if they are different from parent
        if(questionnaireInstance.description != questionnaireInstanceModel.description)
            questionnaireInstance.description = questionnaireInstanceModel.description
        if(questionnaireInstance.timer != questionnaireInstanceModel.timer)
            questionnaireInstance.timer = questionnaireInstanceModel.timer

        questionnaireInstance.questionnaireInstanceUuid = UUID.randomUUID().toString()
        return questionnaireInstanceRepository.save(questionnaireInstance)
    }

    /**
     * Get questionnaire by its unique identifier.
     *
     * @param questionnaireInstanceId identifier of questionnaire object
     * @return questionnaire instance object
     */
    @Validated
    fun getQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int, loggedUser: User) : QuestionnaireInstance {
        val questionnaireInstance = checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository, questionnaireInstanceId)
        val questionnaireInstanceParent = questionnaireServices.getQuestionnaireById(questionnaireInstance.questionnaireId!!)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaireInstanceParent.creatorId!!)

        return questionnaireInstance
    }

    /**
     * Get questionnaire by its unique UUID.
     *
     * @param questionnaireInstanceUuid identifier of questionnaire object
     * @return questionnaire instance object
     */
    @Validated
    @Transactional
    fun getQuestionnaireInstanceByUuid(questionnaireInstanceUuid: String) : QuestionnaireInstanceOutputModel {
        val questionnaireInstanceOptional = questionnaireInstanceRepository.findByQuestionnaireInstanceUuid(questionnaireInstanceUuid)

        if(questionnaireInstanceOptional.isEmpty){
            throw ServiceException(
                    "Questionnaire instances not found.",
                    "There are no questionnaire instances for selected questionnaire $questionnaireInstanceUuid",
                    "/iselearning/questionnaireInstance/nonexistent",
                    ErrorCode.ITEM_NOT_FOUND
            )
        }
        val questionnaireInstance = questionnaireInstanceOptional.get()
        val model = transformQuestionnaireInstanceToOutputModel(questionnaireInstance)
        checkQuestionnaireInstanceTimeout(questionnaireInstance, questionnaireInstanceRepository)
        model.challenges = getOrInitializeChallengeAnswers(questionnaireInstance)
        return model
    }

    /**
     * Get all questionnaires instances of questionnaire.
     *
     * @param questionnaireId unique identifier of questionnaire object
     * @return List of questionnaire instance objects
     */
    @Validated
    fun getAllQuestionnaireInstancesByQuestionnaireId(@Positive questionnaireId: Int, loggedUser: User) : List<QuestionnaireInstance> {
        val questionnaireInstanceParent = questionnaireServices.getQuestionnaireById(questionnaireId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaireInstanceParent.creatorId!!)
        return questionnaireInstanceRepository.findAllByQuestionnaireId(questionnaireId)
    }


    /**
     * Update a questionnaire instance.
     *
     * @param questionnaireInstanceModel information to be updated
     * @return updated questionnaire instance
     */
    @Validated
    fun updateQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int, @Valid questionnaireInstanceModel: QuestionnaireInstanceModel, loggedUser: User): QuestionnaireInstance {
        val updatedQuestionnaireInstance = checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository, questionnaireInstanceId)
        val questionnaireInstanceParent = questionnaireServices.getQuestionnaireById(updatedQuestionnaireInstance.questionnaireId!!)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaireInstanceParent.creatorId!!)

        // creator can only updated questionnaire instance before timer countdown has started
        if(updatedQuestionnaireInstance.startTimestamp != null) {
            checkQuestionnaireInstanceTimeout(updatedQuestionnaireInstance, questionnaireInstanceRepository)
        }

        //region data for update operation
        if(questionnaireInstanceModel.description != null)
            updatedQuestionnaireInstance.description = questionnaireInstanceModel.description
        if(questionnaireInstanceModel.timer != null)
            updatedQuestionnaireInstance.timer = questionnaireInstanceModel.timer
        if(questionnaireInstanceModel.isFinished!!) {
            updatedQuestionnaireInstance.endTimestamp = System.currentTimeMillis()
        }
        //endregion

        return questionnaireInstanceRepository.save(updatedQuestionnaireInstance)
    }

    /**
     * Delete a questionnaire instance by its unique identifier.
     *
     * @param questionnaireInstanceId identifier of object
     */
    @Validated
    fun deleteQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int, @Valid loggedUser: User) {
        val questionnaireInstance = checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository, questionnaireInstanceId)
        val questionnaireInstanceParent = questionnaireServices.getQuestionnaireById(questionnaireInstance.questionnaireId!!)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaireInstanceParent.creatorId!!)

        questionnaireInstanceRepository.deleteById(questionnaireInstanceId)
    }

    /**
     * Auxiliary function that converts QuestionnaireInstance model to QuestionnaireInstance domain
     */
    private fun convertToEntity(input : QuestionnaireInstanceModel) = modelMapper.map(input, QuestionnaireInstance::class.java)

    /**
     * Auxiliary function that converts QuestionnaireInstance domain entity to QuestionnaireInstanceOutputModel
     */
    private fun transformQuestionnaireInstanceToOutputModel(questionnaireInstance: QuestionnaireInstance): QuestionnaireInstanceOutputModel
            = modelMapper.map(questionnaireInstance, QuestionnaireInstanceOutputModel::class.java)

    private fun getOrInitializeChallengeAnswers(questionnaireInstance: QuestionnaireInstance): List<ChallengeAnswerOutputModel> {
        var answers = questionnaireAnswerRepository.findAllByQuestionnaireInstanceId(questionnaireInstance.questionnaireInstanceId!!)
        val challenges = challengeService.getAllChallengesByQuestionnaireId(questionnaireInstance.questionnaireId!!)

        //initialize list
        if(answers.isEmpty()) {
            answers = challenges.map {
                QuestionnaireAnswer(
                        null,
                        questionnaireInstance.questionnaireInstanceId,
                        null,
                        Answer(null, null, null, null, false)
                )
            }
        }

        val questionnaireChallenges = questionnaireChallengeRepository.findAllByQuestionnaireQuestionnaireId(questionnaireInstance.questionnaireId!!)


        return answers.mapIndexed {
            index, questionnaireAnswer ->
            ChallengeAnswerOutputModel(
                    challenges[index].challengeId!!,
                    questionnaireChallenges.first { qc -> qc.challenge?.challengeId == challenges[index].challengeId!! }.languageFilter?.split(",") ,
                    challenges[index].challengeText!!,
                    AnswerModel(
                            questionnaireAnswer.answer?.codeLanguage,
                            questionnaireAnswer.answer?.codeLanguage,
                            questionnaireAnswer.answer?.unitTests
                    )
            )
        }
    }

}