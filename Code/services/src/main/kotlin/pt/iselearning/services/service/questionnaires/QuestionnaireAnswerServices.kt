package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireAnswerModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.util.CustomValidators
import pt.iselearning.services.util.QuestionnaireTimer
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire answer object
 */
@Validated
@Service
class QuestionnaireAnswerServices(
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository,
        private val questionnaireAnswerRepository: QuestionnaireAnswerRepository,
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
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireAnswerParent, questionnaireAnswerModel.questionnaireInstanceId)
        QuestionnaireTimer.checkQuestionnaireInstanceTimeout(questionnaireAnswerParent.get(), questionnaireInstanceRepository)
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
        CustomValidators.checkIfQuestionnaireAnswerExists(questionnaireAnswer, questionnaireAnswerId)
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
        val questionnaireAnswers = questionnaireAnswerRepository.findAllByQuestionnaireInstanceId(questionnaireInstanceId)
        if (questionnaireAnswers.isEmpty()) {
            throw ServerException("Questionnaire instances not found.",
                    "There are no questionnaire instances for the selected questionnaire $questionnaireInstanceId", ErrorCode.ITEM_NOT_FOUND)
        }
        return questionnaireAnswers
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
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireAnswerParent, questionnaireAnswerModel.questionnaireInstanceId)
        QuestionnaireTimer.checkQuestionnaireInstanceTimeout(questionnaireAnswerParent.get(), questionnaireInstanceRepository)

        val questionnaireAnswerFromDB = questionnaireAnswerRepository.findById(questionnaireAnswerId)
        CustomValidators.checkIfQuestionnaireAnswerExists(questionnaireAnswerFromDB, questionnaireAnswerId)

        //region data for update operation
        val updatedQuestionnaireAnswer = questionnaireAnswerFromDB.get()
        /*
        if(updatedQuestionnaireAnswer.answer != null)
            updatedQuestionnaireAnswer.answer = questionnaireAnswerModel.answerCode
        */
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
        CustomValidators.checkIfQuestionnaireAnswerExists(questionnaireAnswerRepository, questionnaireAnswerId)
        questionnaireAnswerRepository.deleteById(questionnaireAnswerId)
    }

    /**
     * Auxiliary function that converts QuestionnaireAnswer model to QuestionnaireAnswer domain
     */
    //TODO: usar o mapper a funcionar em vez desta função auxiliar
    //private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireAnswer::class.java)
    private fun convertToEntity(questionnaireAnswerModel: QuestionnaireAnswerModel): QuestionnaireAnswer {
        // build answer from model
        val answer = Answer()
        answer.codeLanguage = questionnaireAnswerModel.answerModel.codeLanguage
        answer.answerCode = questionnaireAnswerModel.answerModel.answerCode
        answer.unitTests = questionnaireAnswerModel.answerModel.unitTests

        // build questionnaireAnswer from model
        val questionnaireAnswer = QuestionnaireAnswer()
        questionnaireAnswer.questionnaireAnswerId = questionnaireAnswer.questionnaireAnswerId
        questionnaireAnswer.qcId = questionnaireAnswer.qcId
        questionnaireAnswer.answer = answer

        return questionnaireAnswer
    }
}