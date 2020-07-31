package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.models.questionnaire.CreateQuestionnaire
import pt.iselearning.services.models.questionnaire.UpdateQuestionnaire
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire object
 */
@Validated
@Service
class QuestionnaireServices(
        private val questionnaireRepository: QuestionnaireRepository
) {

    /**
     * Get questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of questionnaire object
     * @return questionnaire object
     */
    @Validated
    fun getQuestionnaireById(@Positive questionnaireId: Int) : Questionnaire {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireId)
        return questionnaire.get()
    }

    /**
     * Get all user questionnaires.
     *
     * @param userId unique identifier of user object
     * @return List of questionnaires objects
     */
    @Validated
    fun getUserAllQuestionnaires(@Positive userId: Int) : List<Questionnaire> {
        val questionnaires = questionnaireRepository.findAllByCreatorId(userId)
        if (questionnaires.isEmpty()) {
            throw ServerException("Questionnaires not found.",
                    "There are no questionnaires created by user $userId", ErrorCode.ITEM_NOT_FOUND)
        }
        return questionnaires
    }

    /**
     * Create a questionnaire.
     *
     * @param questionnaireInput object information
     * @return created questionnaire
     */
    @Validated
    fun createQuestionnaire(@Valid questionnaireInput: CreateQuestionnaire): Questionnaire {
        val questionnaire = convertToEntity(questionnaireInput)
        questionnaire.timer = if(questionnaire.timer == null) 0 else questionnaire.timer
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Update a questionnaire.
     *
     * @param questionnaireInput information to be updated
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaire(@Valid questionnaireInput: UpdateQuestionnaire): Questionnaire {
        val questionnaire = convertToEntity(questionnaireInput)
        val questionnaireFromDB = questionnaireRepository.findById(questionnaire.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireExists(questionnaireFromDB, questionnaire.questionnaireId!!)

        //region data for update operation
        val updatedQuestionnaire = questionnaireFromDB.get()
        if(questionnaire.description != null)
            updatedQuestionnaire.description = questionnaire.description
        if(questionnaire.timer != null)
            updatedQuestionnaire.timer = questionnaire.timer
        //endregion

        return questionnaireRepository.save(updatedQuestionnaire)
    }

    /**
     * Delete a questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of object
     */
    @Validated
    fun deleteQuestionnaireById(@Positive questionnaireId: Int) {
        CustomValidators.checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        questionnaireRepository.deleteById(questionnaireId)
    }

    private fun convertToEntity(input : Any) = mapper.map(input, Questionnaire::class.java)

}