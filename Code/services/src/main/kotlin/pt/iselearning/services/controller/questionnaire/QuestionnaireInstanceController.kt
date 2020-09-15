package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.models.questionnaire.QuestionnaireInstanceModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireInstanceOutputModel
import pt.iselearning.services.service.questionnaires.QuestionnaireInstanceServices
import pt.iselearning.services.util.QUESTIONNAIRE_INSTANCE_PATTERN

/**
 * Handler responsible to respond to requests regard QuestionnaireInstance entity
 */
@RestController
@RequestMapping(QUESTIONNAIRE_INSTANCE_PATTERN, produces = ["application/json"])
class QuestionnaireInstanceController(
        private val questionnaireInstanceServices: QuestionnaireInstanceServices
) {

    /**
     * Method to create an questionnaire instance.
     *
     * A json object that represents a object of the type QuestionnaireInstance must be present in the body
     * @param ucb helps build URLs
     * @param questionnaireInstance represents a QuestionnaireInstance object
     * @return ResponseEntity<QuestionnaireInstance> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createQuestionnaireInstance")
    fun createQuestionnaireInstance(
            @RequestBody questionnaireInstance: QuestionnaireInstanceModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<QuestionnaireInstance> {
        val createdQuestionnaireInstance = questionnaireInstanceServices.createQuestionnaireInstance(questionnaireInstance)
        val location = ucb.path(QUESTIONNAIRE_INSTANCE_PATTERN)
                .path((createdQuestionnaireInstance.questionnaireInstanceId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaireInstance)
    }

    /**
     * Method to get a single questionnaire instance.
     *
     * Path variable "questionnaireInstanceId" must be present
     * @param questionnaireInstanceId represents QuestionnaireInstance unique identifier
     * @return ResponseEntity<QuestionnaireInstance>
     */
    @GetMapping("/{questionnaireInstanceId}", name = "getQuestionnaireInstanceById")
    fun getQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int
    ): ResponseEntity<QuestionnaireInstance> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.getQuestionnaireInstanceById(questionnaireInstanceId))
    }

    /**
     * Method to get a single questionnaire instance.
     *
     * Path variable "questionnaireInstanceUuid" must be present
     * @param questionnaireInstanceUuid represents QuestionnaireInstance UUID
     * @return ResponseEntity<QuestionnaireInstance>
     */
    @GetMapping("/solve/{questionnaireInstanceUuid}", name = "getQuestionnaireInstanceByUuid")
    fun getQuestionnaireInstanceByUuid(
            @PathVariable questionnaireInstanceUuid: String
    ): ResponseEntity<QuestionnaireInstanceOutputModel> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.getQuestionnaireInstanceByUuid(questionnaireInstanceUuid))
    }

    /**
     * Method to get all questionnaire instances from a questionnaire.
     *
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents the Questionnaire parent object
     * @return ResponseEntity<List<QuestionnaireInstance>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/questionnaires/{questionnaireId}", name = "getAllQuestionnaireInstancesByQuestionnaireId")
    fun getAllQuestionnaireInstancesByQuestionnaireId(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<List<QuestionnaireInstance>> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.getAllQuestionnaireInstancesByQuestionnaireId(questionnaireId))
    }

    /**
     * Method to update an questionnaire instance.
     *
     * A json object that represents a object of the type Questionnaire must be present in the body
     * @param questionnaireInstanceId represents a QuestionnaireInstance unique identifier
     * @param questionnaireModel represents a QuestionnaireInstance object
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireInstanceId}", name = "updateQuestionnaireInstanceById")
    fun updateQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int,
            @RequestBody questionnaireModel: QuestionnaireInstanceModel
    ): ResponseEntity<QuestionnaireInstance> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.updateQuestionnaireInstanceById(questionnaireInstanceId,questionnaireModel))
    }

    /**
     * Method to delete a single questionnaire instance.
     *
     * Path variable "questionnaireInstanceId" must be present
     * @param questionnaireInstanceId represents QuestionnaireInstance unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireInstanceId}", name = "deleteQuestionnaireInstanceById")
    fun deleteQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int
    ): ResponseEntity<QuestionnaireInstance> {
        questionnaireInstanceServices.deleteQuestionnaireInstanceById(questionnaireInstanceId)
        return ResponseEntity.noContent().build()
    }

}