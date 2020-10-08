package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.models.questionnaire.input.QuestionnaireAnswerInputModel
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstanceQuestionnaireView
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireAnswerModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireAnswerOutputModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceQuestionnaireViewRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.service.ExecutionEnvironmentsService
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
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository,
        private val questionnaireInstanceQuestionnaireViewRepository : QuestionnaireInstanceQuestionnaireViewRepository,
        private val questionnaireInstanceServices: QuestionnaireInstanceServices,
        private val questionnaireChallengeServices: QuestionnaireChallengeServices,
        private val executionEnvironmentsService: ExecutionEnvironmentsService,
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
        val questionnaireAnswerParent = checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository,questionnaireAnswerModel.questionnaireInstanceId)
        checkQuestionnaireInstanceTimeout(questionnaireAnswerParent, questionnaireInstanceRepository)

        val questionnaireChallenge = questionnaireChallengeServices
                .getQuestionnaireChallengeByQuestionnaireIdAndChallengeId(
                        questionnaireAnswerModel.questionnaireId,
                        questionnaireAnswerModel.challengeId
                )

        questionnaireAnswer.questionnaireInstanceId = questionnaireAnswerParent.questionnaireInstanceId
        questionnaireAnswer.qcId = questionnaireChallenge.qcId

        return questionnaireAnswerRepository.save(questionnaireAnswer)
    }

    /**
     * Submit a questionnaire answer.
     *
     * @param questionnaireAnswerInputModel object information
     * @return created questionnaire answer
     */
    @Validated
    @Transactional
    fun submitQuestionnaireAnswer(@Valid questionnaireAnswerInputModel: QuestionnaireAnswerInputModel): MutableIterable<QuestionnaireAnswer>? {
        val questionnaireAnswerParent = checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository,questionnaireAnswerInputModel.questionnaireInstanceId)
        checkQuestionnaireInstanceTimeout(questionnaireAnswerParent, questionnaireInstanceRepository)

        val questionnaireAnswers = questionnaireAnswerInputModel
                .challenges?.map { challenge ->

                    val optionalQc = questionnaireChallengeRepository
                            .findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(
                                    questionnaireAnswerInputModel.questionnaireId,
                                    challenge.challengeId
                            )
                    if(optionalQc.isEmpty){
                        throw ServiceException("Challenge not in that questionnaire",
                                "Challenge  ${challenge.challengeId} its not on the list of challenges for questionnaire ${questionnaireAnswerInputModel.questionnaireId}",
                                "/iselearning/questionnaireAnswers/noChallengesToAnswer",
                                ErrorCode.ITEM_NOT_FOUND)
                    }

                    val qc = optionalQc.get()

                    // evaluates if answer is corrected based on challenge unit tests
                    val executableResult = executionEnvironmentsService.execute(
                            ExecutableModel(
                                    challenge.answer.codeLanguage!!,
                                    challenge.answer.answerCode!!,
                                    qc.challenge?.solutions
                                            ?.first { it -> it.codeLanguage.toString() in challenge.answer.codeLanguage!! }
                                            ?.unitTests!!,
                                    true
                            )
                    )

                    QuestionnaireAnswer(
                            null,
                            questionnaireAnswerInputModel.questionnaireInstanceId,
                            optionalQc.get().qcId,
                            Answer(
                                    null,
                                    challenge.answer.codeLanguage,
                                    challenge.answer.answerCode,
                                    challenge.answer.unitTests,
                                    !executableResult.wasError
                            ))
        }

        questionnaireInstanceServices.closeQuestionnaireInstanceById(questionnaireAnswerInputModel.questionnaireInstanceId)

        return questionnaireAnswers?.let { questionnaireAnswerRepository.saveAll(it) }
    }

    /**
     * Get questionnaire answer by its unique identifier.
     *
     * @param questionnaireAnswerId identifier of questionnaire answer object
     * @return questionnaire answer object
     */
    @Validated
    fun getQuestionnaireAnswerById(@Positive questionnaireAnswerId: Int): QuestionnaireAnswer {
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
    fun getAllQuestionnaireAnswersFromQuestionnaireInstanceId(@Positive questionnaireInstanceId: Int): List<QuestionnaireAnswerOutputModel> {
        val questionnaireAnswers = questionnaireAnswerRepository.findAllByQuestionnaireInstanceId(questionnaireInstanceId)
        val qcIds = questionnaireAnswers.map { it.qcId }
        val questionnaireChallenges = questionnaireChallengeRepository.findAllById(qcIds);

        return questionnaireAnswers.map { questionnaireAnswer ->
            val challenge = questionnaireChallenges.find { it.qcId == questionnaireAnswer.qcId }?.challenge
            QuestionnaireAnswerOutputModel(challenge?.challengeText!!, questionnaireAnswer.answer!!)
        }
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
    fun getAllQuestionnaireAnswersFromQuestionnaireCreator(user: User): List<QuestionnaireInstanceQuestionnaireView> {
        return questionnaireInstanceQuestionnaireViewRepository.findAllByCreatorId(user.userId!!)
    }
    /**
     * Auxiliary function that converts QuestionnaireAnswer model to QuestionnaireAnswer domain
     */
    private fun convertToEntity(questionnaireAnswerModel: QuestionnaireAnswerModel): QuestionnaireAnswer {
        // build answer from model
        val answer = Answer()

        // build questionnaireAnswer from model
        val questionnaireAnswer = QuestionnaireAnswer()
        questionnaireAnswer.questionnaireAnswerId = questionnaireAnswer.questionnaireAnswerId
        questionnaireAnswer.qcId = questionnaireAnswer.qcId
        questionnaireAnswer.answer = answer

        return questionnaireAnswer
    }


}