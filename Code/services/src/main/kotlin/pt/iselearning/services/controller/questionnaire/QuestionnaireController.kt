package pt.iselearning.services.controller.questionnaire;

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.Questionnaire;
import pt.iselearning.services.models.questionnaire.QuestionnaireModel
import pt.iselearning.services.service.questionnaires.QuestionnaireServices;
import pt.iselearning.services.util.Constants

/**
 * Handler responsible to respond to requests regard Questionnaire domain
 */
@RestController
@RequestMapping(Constants.QUESTIONNAIRE_PATH, produces = ["application/json"])
class QuestionnaireController(
        private val questionnaireServices: QuestionnaireServices
) {

    /**
     * Method to create an questionnaire.
     * A json object that represents a object of the type Questionnaire must be present in the body
     * @param ucb helps build URLs
     * @param questionnaire represents a Questionnaire object
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createQuestionnaire(
            @RequestBody questionnaire: QuestionnaireModel,
            ucb : UriComponentsBuilder
    ): ResponseEntity<Questionnaire> {
        val createdQuestionnaire = questionnaireServices.createQuestionnaire(questionnaire)
        val location = ucb.path(Constants.QUESTIONNAIRE_PATH)
                .path((createdQuestionnaire.questionnaireId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaire)
    }

    /**
     * Method to get a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @return ResponseEntity<Questionnaire>
     */
    @GetMapping("/{questionnaireId}")
    fun getQuestionnaireById(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<Questionnaire> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireServices.getQuestionnaireById(questionnaireId))
    }

    /**
     * Method to get all user questionnaires.
     * Path variable "userId" must be present
     * @param userId represents the Questionnaire creator
     * @return ResponseEntity<List<Questionnaire>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/users/{userId}") //TODO change to logged in user
    fun getAllUserQuestionnaires(
            @PathVariable userId: Int
    ): ResponseEntity<List<Questionnaire>> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireServices.getUserAllQuestionnaires(userId))
    }

    /**
     * Method to update an questionnaire.
     * A json object that represents a object of the type Questionnaire must be present in the body
     * @param questionnaireId represents a Questionnaire unique identifier
     * @param questionnaireModel represents a Questionnaire
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireId}")
    fun updateQuestionnaire(
            @PathVariable questionnaireId : Int,
            @RequestBody questionnaireModel: QuestionnaireModel
    ): ResponseEntity<Questionnaire> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireServices.updateQuestionnaire(questionnaireId,questionnaireModel))
    }

    /**
     * Method to delete a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireId}")
    fun deleteQuestionnaireById(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<Questionnaire> {
        questionnaireServices.deleteQuestionnaireById(questionnaireId)
        return ResponseEntity.noContent().build()
    }

}
