package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireInstanceModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import pt.iselearning.services.util.QuestionnaireTimer
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire instance object
 */
@Validated
@Service
class QuestionnaireInstanceServices(
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a questionnaire instance.
     *
     * @param questionnaireInstanceModel object information
     * @return created questionnaire instance
     */
    @Validated
    fun createQuestionnaireInstance(@Valid questionnaireInstanceModel: QuestionnaireInstanceModel): QuestionnaireInstance {
        val questionnaireInstance = convertToEntity(questionnaireInstanceModel)
        val questionnaireInstanceParentOptional = questionnaireRepository.findById(questionnaireInstance.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireExists(questionnaireInstanceParentOptional, questionnaireInstance.questionnaireId!!)
        val questionnaireInstanceParent = questionnaireInstanceParentOptional.get()

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
    fun getQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int) : QuestionnaireInstance {
        val questionnaireInstance = questionnaireInstanceRepository.findById(questionnaireInstanceId)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstance, questionnaireInstanceId)

        return questionnaireInstance.get()
    }

    /**
     * Get questionnaire by its unique UUID.
     *
     * @param questionnaireInstanceUuid identifier of questionnaire object
     * @return questionnaire instance object
     */
    @Validated
    fun getQuestionnaireInstanceByUuid(questionnaireInstanceUuid: String) : QuestionnaireInstance {
        val questionnaireInstanceOptional = questionnaireInstanceRepository.findByQuestionnaireInstanceUuid(questionnaireInstanceUuid)

        if(questionnaireInstanceOptional.isEmpty){
            throw ServerException("Questionnaire instances not found.",
                    "There are no questionnaire instances for selected questionnaire $questionnaireInstanceUuid", ErrorCode.ITEM_NOT_FOUND)
        }
        val questionnaireInstance = questionnaireInstanceOptional.get()

        QuestionnaireTimer.checkQuestionnaireInstanceTimeout(questionnaireInstance, questionnaireInstanceRepository)

        return questionnaireInstance
    }

    /**
     * Get all questionnaires instances of questionnaire.
     *
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
     * Update a questionnaire instance.
     *
     * @param questionnaireInstanceModel information to be updated
     * @return updated questionnaire instance
     */
    @Validated
    fun updateQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int, @Valid questionnaireInstanceModel: QuestionnaireInstanceModel): QuestionnaireInstance {
        val questionnaireInstanceFromDB = questionnaireInstanceRepository.findById(questionnaireInstanceId)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstanceFromDB, questionnaireInstanceId)
        val updatedQuestionnaireInstance = questionnaireInstanceFromDB.get()

        QuestionnaireTimer.checkQuestionnaireInstanceTimeout(updatedQuestionnaireInstance, questionnaireInstanceRepository)

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
    fun deleteQuestionnaireInstanceById(@Positive questionnaireInstanceId: Int) {
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository, questionnaireInstanceId)

        questionnaireInstanceRepository.deleteById(questionnaireInstanceId)
    }

    /**
     * Auxiliary function that converts QuestionnaireInstance model to QuestionnaireInstance domain
     */
    private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireInstance::class.java)

}