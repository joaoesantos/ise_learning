package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
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
     * @param questionnaireInstance json object that represents a QuestionnaireInstance object
     * @param loggedUser user that is calling the service
     * @param ucb helps build URLs
     * @return ResponseEntity<QuestionnaireInstance> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createQuestionnaireInstance")
    fun createQuestionnaireInstance(
            @RequestBody questionnaireInstance: QuestionnaireInstanceModel,
            loggedUser: User,
            ucb: UriComponentsBuilder
    ): ResponseEntity<QuestionnaireInstance> {
        val createdQuestionnaireInstance = questionnaireInstanceServices.createQuestionnaireInstance(questionnaireInstance, loggedUser)
        val location = ucb.path(QUESTIONNAIRE_INSTANCE_PATTERN)
                .path((createdQuestionnaireInstance.questionnaireInstanceId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaireInstance)
    }

    /**
     * Method to get a single questionnaire instance.
     *
     * @param questionnaireInstanceId represents QuestionnaireInstance unique identifier
     * @param loggedUser user that is calling the service
     * @return ResponseEntity<QuestionnaireInstance>
     */
    @GetMapping("/{questionnaireInstanceId}", name = "getQuestionnaireInstanceById")
    fun getQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int,
            loggedUser: User
    ): ResponseEntity<QuestionnaireInstance> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.getQuestionnaireInstanceById(questionnaireInstanceId, loggedUser))
    }

    /**
     * Method to get a single questionnaire instance.
     *
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
     * @param questionnaireId represents the Questionnaire parent object,
     * @param loggedUser user that is calling the service
     * @return ResponseEntity<List<QuestionnaireInstance>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/questionnaires/{questionnaireId}", name = "getAllQuestionnaireInstancesByQuestionnaireId")
    fun getAllQuestionnaireInstancesByQuestionnaireId(
            @PathVariable questionnaireId: Int,
            loggedUser: User
    ): ResponseEntity<List<QuestionnaireInstance>> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.getAllQuestionnaireInstancesByQuestionnaireId(questionnaireId, loggedUser))
    }

    /**
     * Method to update an questionnaire instance.
     *
     * @param questionnaireInstanceId represents a QuestionnaireInstance unique identifier
     * @param questionnaireModel json object that represents a QuestionnaireInstance object
     * @param loggedUser user that is calling the service
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireInstanceId}", name = "updateQuestionnaireInstanceById")
    fun updateQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int,
            @RequestBody questionnaireInstanceModel: QuestionnaireInstanceModel,
            loggedUser: User
    ): ResponseEntity<QuestionnaireInstance> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireInstanceServices.updateQuestionnaireInstanceById(questionnaireInstanceId, questionnaireInstanceModel, loggedUser))
    }

    /**
     * Method to delete a single questionnaire instance.
     *
     * @param questionnaireInstanceId represents QuestionnaireInstance unique identifier
     * @param loggedUser user that is calling the service
     * @return No Content
     */
    @DeleteMapping("/{questionnaireInstanceId}", name = "deleteQuestionnaireInstanceById")
    fun deleteQuestionnaireInstanceById(
            @PathVariable questionnaireInstanceId: Int,
            loggedUser: User
    ): ResponseEntity<QuestionnaireInstance> {
        questionnaireInstanceServices.deleteQuestionnaireInstanceById(questionnaireInstanceId, loggedUser)
        return ResponseEntity.noContent().build()
    }

}