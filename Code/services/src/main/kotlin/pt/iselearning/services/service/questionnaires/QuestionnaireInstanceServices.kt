package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.CreateQuestionnaireInstanceModel
import pt.iselearning.services.models.questionnaire.UpdateQuestionnaireInstanceModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import java.sql.Timestamp
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive
import kotlin.time.milliseconds

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
     * @param questionnaireInstanceInput object information
     * @return created questionnaire instance
     */
    @Validated
    fun createQuestionnaireInstance(@Valid questionnaireInstanceInput: CreateQuestionnaireInstanceModel): QuestionnaireInstance {
        val questionnaireInstance = convertToEntity(questionnaireInstanceInput)
        val questionnaireInstanceParent = questionnaireRepository.findById(questionnaireInstance.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireExists(questionnaireInstanceParent, questionnaireInstance.questionnaireId!!)

        questionnaireInstance.timer = if(questionnaireInstance.timer == null) questionnaireInstanceParent.get().timer else questionnaireInstance.timer
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

        //TODO: se tiver endTimestamp é porque já foi resolvido e nao pode ser acedido novamente sem ser pelo criador
        if(questionnaireInstance.endTimestamp != null) {
            throw ServerException("Timeout.",
                    "Questionnaire $questionnaireInstanceUuid is closed", ErrorCode.FORBIDDEN)
        }

        //TODO: utilizador que resolve quest so acede por uuid - se estiver a null, faz update com o tempo current
        if(questionnaireInstance.startTimestamp == null) {
            questionnaireInstance.startTimestamp = Timestamp(System.currentTimeMillis())
            questionnaireInstanceRepository.save(questionnaireInstance)
        }

        //TODO:
        if(questionnaireInstance.timer != null && questionnaireInstance.startTimestamp != null) {
            val expiresIn = questionnaireInstance.startTimestamp!!.time + questionnaireInstance.timer!!
            if(System.currentTimeMillis() > expiresIn) {
                questionnaireInstance.endTimestamp = Timestamp(expiresIn)
                questionnaireInstanceRepository.save(questionnaireInstance)
                throw ServerException("Timeout.",
                        "Questionnaire $questionnaireInstanceUuid is closed", ErrorCode.FORBIDDEN)
            }
        }

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
     * @param questionnaireInstanceInput information to be updated
     * @return updated questionnaire instance
     */
    @Validated
    fun updateQuestionnaireInstanceById(@Valid questionnaireInstanceInput: UpdateQuestionnaireInstanceModel): QuestionnaireInstance {
        val questionnaireInstance = convertToEntity(questionnaireInstanceInput)
        val questionnaireInstanceFromDB = questionnaireInstanceRepository.findById(questionnaireInstance.questionnaireInstanceId!!)
        CustomValidators.checkIfQuestionnaireInstanceExists(questionnaireInstanceFromDB, questionnaireInstance.questionnaireInstanceId!!)

        //region data for update operation
        val updatedQuestionnaireInstance = questionnaireInstanceFromDB.get()
        if(questionnaireInstance.description != null)
            updatedQuestionnaireInstance.description = questionnaireInstance.description
        if(questionnaireInstance.timer != null)
            updatedQuestionnaireInstance.timer = questionnaireInstance.timer
        if(questionnaireInstance.startTimestamp != null)
            updatedQuestionnaireInstance.startTimestamp = questionnaireInstance.startTimestamp
        if(questionnaireInstance.endTimestamp != null)
            updatedQuestionnaireInstance.endTimestamp = questionnaireInstance.endTimestamp
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

    private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireInstance::class.java)

}