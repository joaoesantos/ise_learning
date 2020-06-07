package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire instance object
 */
@Validated
@Service
class QuestionnaireInstanceServices(
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository
) {

    /**
     * Create a questionnaire instance
     * @param questionnaireInstance object information
     * @return created questionnaire instance
     */
    @Validated
    fun createQuestionnaireInstance(@Valid questionnaireInstance: QuestionnaireInstance): QuestionnaireInstance {
        val questionnaireInstanceParent = questionnaireRepository.findById(questionnaireInstance.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireExists(questionnaireInstanceParent, questionnaireInstance.questionnaireId!!)
        questionnaireInstance.timer = if(questionnaireInstance.timer == null) questionnaireInstanceParent.get().timer else questionnaireInstance.timer
        return questionnaireInstanceRepository.save(questionnaireInstance)
    }

    /**
     * Get questionnaire by its unique identifier
     * @param questionnaireInstanceId identifier of questionnaire object
     * @return questionnaire instance object
     */
    @Validated
    fun getQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int) : QuestionnaireInstance {
        //TODO quando o tempo terminar é necessário que validar que apenas o criador do questionnaire instance pode aceder ao recurso
        val questionnaireInstance = questionnaireInstanceRepository.findById(questionnaireInstanceId)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstance, questionnaireInstanceId)
        return questionnaireInstance.get()
    }

    /**
     * Get all questionnaires instances of questionnaire
     * @param questionnaireId unique identifier of questionnaire object
     * @return List of questionnaire instance objects
     */
    @Validated
    fun getAllQuestionnaireInstancesByQuestionnaireId(@Positive questionnaireId: Int) : List<QuestionnaireInstance> {
        val questionnaireInstances = questionnaireInstanceRepository.findAllByQuestionnaireId(questionnaireId)
        if (questionnaireInstances.isEmpty()) {
            throw ServerException("Questionnaire instances not found.",
                    "There are no questionnaire instances for selected questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }
        return questionnaireInstances
    }

    /**
     * Update a questionnaire instance
     * @param questionnaireInstance information to be updated
     * @return updated questionnaire instance
     */
    @Validated
    fun updateQuestionnaireInstanceById(@Valid questionnaireInstance: QuestionnaireInstance): QuestionnaireInstance {
        val questionnaireInstanceFromDB = questionnaireInstanceRepository.findById(questionnaireInstance.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstanceFromDB, questionnaireInstance.questionnaireId!!)

        //region data for update operation
        val updatedQuestionnaireInstance = questionnaireInstanceFromDB.get()
        updatedQuestionnaireInstance.description = questionnaireInstance.description
        //endregion

        return questionnaireInstanceRepository.save(updatedQuestionnaireInstance)
    }

    /**
     * Delete a questionnaire instance by its unique identifier
     * @param questionnaireInstanceId identifier of object
     */
    @Validated
    fun deleteQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int) {
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository, questionnaireInstanceId)
        questionnaireRepository.deleteById(questionnaireInstanceId)
    }

    //uapuemonarox

}