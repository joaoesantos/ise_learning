package pt.iselearning.services.controller.questionnaire;

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.Questionnaire;
import pt.iselearning.services.models.questionnaire.QuestionnaireModel
import pt.iselearning.services.models.questionnaire.QuestionnaireWithChallengesModel
import pt.iselearning.services.service.questionnaires.QuestionnaireServices;
import pt.iselearning.services.util.QUESTIONNAIRE_PATTERN

/**
 * Handler responsible to respond to requests regard Questionnaire domain
 */
@RestController
@RequestMapping(QUESTIONNAIRE_PATTERN, produces = ["application/json"])
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
    @PostMapping(name = "createQuestionnaire")
    fun createQuestionnaire(
            @RequestBody questionnaire: QuestionnaireModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<Questionnaire> {
        val createdQuestionnaire = questionnaireServices.createQuestionnaire(questionnaire)
        val location = ucb.path(QUESTIONNAIRE_PATTERN)
                .path((createdQuestionnaire.questionnaireId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaire)
    }

    /**
     * Method to create an questionnaire with challenges associated.
     * A json object that represents a object of the type Questionnaire and its Challenges Ids must be present in the body
     * @param ucb helps build URLs
     * @param questionnaireWithChallengesModel represents a Questionnaire object and a collection of challenges unique identifiers
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping("/withChallenges", name = "createQuestionnaireWithChallenges")
    fun createQuestionnaireWithChallenges(
            @RequestBody questionnaireWithChallengesModel: QuestionnaireWithChallengesModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<Questionnaire> {
        val createdQuestionnaire = questionnaireServices.createQuestionnaireWithChallenges(questionnaireWithChallengesModel)
        val location = ucb.path(QUESTIONNAIRE_PATTERN)
                .path((createdQuestionnaire?.questionnaireId).toString())
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
    @GetMapping("/{questionnaireId}", name = "getQuestionnaireById")
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
    @GetMapping("/users/{userId}", name = "getAllUserQuestionnaires") //TODO change to logged in user
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
    @PutMapping("/{questionnaireId}", name = "updateQuestionnaireById")
    fun updateQuestionnaireById(
            @PathVariable questionnaireId: Int,
            @RequestBody questionnaireModel: QuestionnaireModel
    ): ResponseEntity<Questionnaire> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireServices.updateQuestionnaireById(questionnaireId,questionnaireModel))
    }

    /**
     * Method to delete a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireId}", name = "deleteQuestionnaireById")
    fun deleteQuestionnaireById(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<Questionnaire> {
        questionnaireServices.deleteQuestionnaireById(questionnaireId)
        return ResponseEntity.noContent().build()
    }

}
