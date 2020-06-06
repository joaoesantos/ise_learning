package pt.iselearning.services.controller.questionnaire;

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.Questionnaire;
import pt.iselearning.services.service.questionnaires.QuestionnaireServices;
import pt.iselearning.services.util.Constants
import java.lang.String

/**
 * Handler responsible to respond to requests regard Questionnaire entity
 */
@RestController
@RequestMapping(Constants.QUESTIONNAIRE_PATH, produces = ["application/json"])
public class QuestionnaireController(
        private val questionnaireServices: QuestionnaireServices
) {

    /**
     * Method to create an questionnaire.
     * A json object that represents a object of the type Questionnaire must be present in the body
     * @param ucb helps build URLs
     * @param questionnaire represents a Questionnaire
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createQuestionnaire(
            @RequestBody questionnaire: Questionnaire,
            ucb : UriComponentsBuilder
    ): ResponseEntity<Questionnaire> {
        val createdQuestionnaire = questionnaireServices.createQuestionnaire(questionnaire)
        val location = ucb.path("/${Constants.VERSION}/questionnaire")
                .path(String.valueOf(createdQuestionnaire!!.questionnaireId))
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaire)
    }

    /**
     * Method to get a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents questionnaire id
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
     * @param userId represents an questionnaire creator
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
     * @param questionnaireId represents a questionnaire Id
     * @param questionnaire represents a Questionnaire
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireId}")
    fun updateQuestionnaire(
            @PathVariable questionnaireId : Int,
            @RequestBody questionnaire: Questionnaire
    ): ResponseEntity<Questionnaire> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireServices.updateQuestionnaire(questionnaire))
    }

    /**
     * Method to delete a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents questionnaire id
     * @return ResponseEntity<Questionnaire>
     */
    @DeleteMapping("/{questionnaireId}")
    fun deleteQuestionnaireById(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<Questionnaire> {
        questionnaireServices.deleteQuestionnaireById(questionnaireId)
        return ResponseEntity.noContent().build()
    }

}
