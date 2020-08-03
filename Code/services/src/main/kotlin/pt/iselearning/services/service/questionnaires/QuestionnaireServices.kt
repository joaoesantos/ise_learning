package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.models.questionnaire.QuestionnaireModel
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
        private val questionnaireRepository: QuestionnaireRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a questionnaire.
     *
     * @param questionnaireModel object information
     * @return created questionnaire
     */
    @Validated
    fun createQuestionnaire(@Valid questionnaireModel: QuestionnaireModel): Questionnaire {
        val questionnaire = convertToEntity(questionnaireModel)
        questionnaire.timer = if(questionnaire.timer == null) 0 else questionnaire.timer
        return questionnaireRepository.save(questionnaire);
    }

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
     * Update a questionnaire.
     *
     * @param questionnaireModel information to be updated
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaireById(@Positive questionnaireId: Int, @Valid questionnaireModel: QuestionnaireModel): Questionnaire {
        val questionnaireFromDB = questionnaireRepository.findById(questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaireFromDB, questionnaireId)

        //region data for update operation
        val updatedQuestionnaire = questionnaireFromDB.get()
        if(questionnaireModel.description != null)
            updatedQuestionnaire.description = questionnaireModel.description
        if(questionnaireModel.timer != null)
            updatedQuestionnaire.timer = questionnaireModel.timer
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

    /**
     * Auxiliary function that converts Questionnaire model to Questionnaire domain
     */
    private fun convertToEntity(input : Any) = modelMapper.map(input, Questionnaire::class.java)

}