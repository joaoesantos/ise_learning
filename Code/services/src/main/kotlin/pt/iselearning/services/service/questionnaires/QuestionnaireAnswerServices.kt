package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.util.CustomValidators
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire answer object
 */
@Validated
@Service
class QuestionnaireAnswerServices(
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        private val questionnaireAnswerRepository: QuestionnaireAnswerRepository
) {

    /**
     * Create a questionnaire answer.
     *
     * @param questionnaireAnswer object information
     * @return created questionnaire answer
     */
    @Validated
    fun createQuestionnaireAnswer(@Valid questionnaireAnswer: QuestionnaireAnswer): QuestionnaireAnswer {
        val questionnaireAnswerParent = questionnaireInstanceRepository.findById(questionnaireAnswer.questionnaireInstanceId!!)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireAnswerParent, questionnaireAnswer.questionnaireInstanceId!!)
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
                    "There are no questionnaire instances for selected questionnaire $questionnaireInstanceId", ErrorCode.ITEM_NOT_FOUND)
        }
        return questionnaireAnswers
    }

    /**
     * Update a questionnaire answer.
     *
     * @param questionnaireAnswer information to be updated
     * @return updated questionnaire answer
     */
    @Validated
    fun updateQuestionnaireAnswerById(@Valid questionnaireAnswer: QuestionnaireAnswer): QuestionnaireAnswer {
        val questionnaireAnswerFromDB = questionnaireAnswerRepository.findById(questionnaireAnswer.questionnaireAnswerId!!)
        CustomValidators.checkIfQuestionnaireAnswerExists(questionnaireAnswerFromDB, questionnaireAnswer.questionnaireAnswerId!!)

        //region data for update operation
        val updatedQuestionnaireAnswer = questionnaireAnswerFromDB.get()
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

}