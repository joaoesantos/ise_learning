package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstanceQuestionnaireView
import pt.iselearning.services.models.questionnaire.QuestionnaireAnswerModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceQuestionnaireViewRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.util.checkIfQuestionnaireAnswerExists
import pt.iselearning.services.util.checkIfQuestionnaireInstanceExists
import pt.iselearning.services.util.checkQuestionnaireInstanceTimeout
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire answer object
 */
@Validated
@Service
class QuestionnaireAnswerServices(
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        private val questionnaireAnswerRepository: QuestionnaireAnswerRepository,
        private val questionnaireChallengeServices: QuestionnaireChallengeServices,
        private val questionnaireInstanceQuestionnaireViewRepository : QuestionnaireInstanceQuestionnaireViewRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a questionnaire answer.
     *
     * @param questionnaireAnswerModel object information
     * @return created questionnaire answer
     */
    @Validated
    fun createQuestionnaireAnswer(@Valid questionnaireAnswerModel: QuestionnaireAnswerModel): QuestionnaireAnswer {
        val questionnaireAnswer = convertToEntity(questionnaireAnswerModel)
        val questionnaireAnswerParent = questionnaireInstanceRepository.findById(questionnaireAnswerModel.questionnaireInstanceId)
        checkIfQuestionnaireInstanceExists(questionnaireAnswerParent, questionnaireAnswerModel.questionnaireInstanceId)
        checkQuestionnaireInstanceTimeout(questionnaireAnswerParent.get(), questionnaireInstanceRepository)

        val questionnaireChallenge = questionnaireChallengeServices
                .getQuestionnaireChallengeByQuestionnaireIdAndChallengeId(
                        questionnaireAnswerModel.questionnaireId,
                        questionnaireAnswerModel.challengeId
                )

        questionnaireAnswer.questionnaireInstanceId = questionnaireAnswerParent.get().questionnaireInstanceId
        questionnaireAnswer.qcId = questionnaireChallenge.qcId

        return questionnaireAnswerRepository.save(questionnaireAnswer)
    }

    /**
     * Get questionnaire answer by its unique identifier.
     *
     * @param questionnaireAnswerId identifier of questionnaire answer object
     * @return questionnaire answer object
     */
    @Validated
    fun getQuestionnaireAnswerById(@Positive questionnaireAnswerId: Int) : QuestionnaireAnswer {
        val questionnaireAnswer = questionnaireAnswerRepository.findById(questionnaireAnswerId)
        checkIfQuestionnaireAnswerExists(questionnaireAnswer, questionnaireAnswerId)
        return questionnaireAnswer.get()
    }

    /**
     * Get all questionnaires answers of questionnaire instance.
     *
     * @param questionnaireInstanceId unique identifier of questionnaire instance object
     * @return List of questionnaire answer objects
     */
    @Validated
    fun getAllQuestionnaireAnswersFromQuestionnaireInstanceId(@Positive questionnaireInstanceId: Int) : List<QuestionnaireAnswer> {
        return questionnaireAnswerRepository.findAllByQuestionnaireInstanceId(questionnaireInstanceId)
    }

    /**
     * Update a questionnaire answer.
     *
     * @param questionnaireAnswerModel information to be updated
     * @return updated questionnaire answer
     */
    @Validated
    fun updateQuestionnaireAnswerById(@Positive questionnaireAnswerId: Int, @Valid questionnaireAnswerModel: QuestionnaireAnswerModel): QuestionnaireAnswer {
        val questionnaireAnswerParent = questionnaireInstanceRepository.findById(questionnaireAnswerModel.questionnaireInstanceId)
        checkIfQuestionnaireInstanceExists(questionnaireAnswerParent, questionnaireAnswerModel.questionnaireInstanceId)
        checkQuestionnaireInstanceTimeout(questionnaireAnswerParent.get(), questionnaireInstanceRepository)

        val questionnaireAnswerFromDB = questionnaireAnswerRepository.findById(questionnaireAnswerId)
        checkIfQuestionnaireAnswerExists(questionnaireAnswerFromDB,questionnaireAnswerId)

        //region data for update operation
        val updatedQuestionnaireAnswer = questionnaireAnswerFromDB.get()
        if(updatedQuestionnaireAnswer.answer?.answerCode != null)
            updatedQuestionnaireAnswer.answer?.answerCode = questionnaireAnswerModel.answer.answerCode
        if(updatedQuestionnaireAnswer.answer?.unitTests != null)
            updatedQuestionnaireAnswer.answer?.unitTests = questionnaireAnswerModel.answer.unitTests
        //endregion

        return questionnaireAnswerRepository.save(updatedQuestionnaireAnswer)
    }

    /**
     * Delete a questionnaire answer by its unique identifier.
     *
     * @param questionnaireAnswerId identifier of object
     */
    @Validated
    fun deleteQuestionnaireAnswerById(@Positive questionnaireAnswerId: Int) {
        checkIfQuestionnaireAnswerExists(questionnaireAnswerRepository, questionnaireAnswerId)
        questionnaireAnswerRepository.deleteById(questionnaireAnswerId)
    }

    /**
     *
     */
    fun getAllQuestionnaireAnswersFromCreator(user: User): List<QuestionnaireInstanceQuestionnaireView> {
        return questionnaireInstanceQuestionnaireViewRepository.findAllByCreatorId(user.userId!!)
    }
    /**
     * Auxiliary function that converts QuestionnaireAnswer model to QuestionnaireAnswer domain
     */
    //TODO: usar o mapper a funcionar em vez desta função auxiliar ??
    //private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireAnswer::class.java)
    private fun convertToEntity(questionnaireAnswerModel: QuestionnaireAnswerModel): QuestionnaireAnswer {
        // build answer from model
        val answer = Answer()
        answer.codeLanguage = questionnaireAnswerModel.answer.codeLanguage
        answer.answerCode = questionnaireAnswerModel.answer.answerCode
        answer.unitTests = questionnaireAnswerModel.answer.unitTests

        // build questionnaireAnswer from model
        val questionnaireAnswer = QuestionnaireAnswer()
        questionnaireAnswer.questionnaireAnswerId = questionnaireAnswer.questionnaireAnswerId
        questionnaireAnswer.qcId = questionnaireAnswer.qcId
        questionnaireAnswer.answer = answer

        return questionnaireAnswer
    }


}