package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire domain
 */
@Validated
@Service
class QuestionnaireServices(
        private val questionnaireRepository: QuestionnaireRepository
) {

    /**
     * Get questionnaire by its unique identifier
     * @param questionnaireId identifier of questionnaire domain
     * @return questionnaire domain
     */
    @Validated
    fun getQuestionnaireById(@Positive questionnaireId: Int) : Questionnaire {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)
        return questionnaire.get()
    }

    /**
     * Get all user questionnaires
     * @param userId unique identifier of user domain
     * @return questionnaire domain
     */
    @Validated
    fun getUserAllQuestionnaires(@Positive userId: Int) : List<Questionnaire> {
        val questionnaires = questionnaireRepository.findAllByCreatorId(userId)
        //checkIfQuestionnaireExists(questionnaire, questionnaireId)
        return questionnaires
    }

    /**
     * Create a questionnaire
     * @param questionnaire domain information
     * @return created questionnaire
     */
    @Validated
    fun createQuestionnaire(@Valid questionnaire: Questionnaire): Questionnaire {
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Update a questionnaire
     * @param questionnaire information to be updated
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaire(@Valid questionnaire: Questionnaire): Questionnaire {
        val challengeFromDb = questionnaireRepository.findById(questionnaire.questionnaireId)
        checkIfQuestionnaireExists(challengeFromDb, questionnaire.questionnaireId)
        return questionnaireRepository.save(questionnaire)
    }

    /**
     * Delete a questionnaire by its unique identifier
     * @param questionnaireId identifier of domain
     */
    @Validated
    fun deleteQuestionnaire(@Positive questionnaireId: Int) {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)
        questionnaireRepository.deleteById(questionnaireId)
    }

    /**
     * Validates if questionnaire is empty
     * @param questionnaire to be validated
     * @param questionnaireId identifier of domain
     * @throws ServerException when on failure to find questionnaire
     */
    fun checkIfQuestionnaireExists(questionnaire: Optional<Questionnaire>, questionnaireId: Int) {
        if (questionnaire.isEmpty) {
            throw ServerException("Questionnaire not found.",
                    "There is no questionnaire with id $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }
    }

}