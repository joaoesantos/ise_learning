package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstanceQuestionnaireView
import pt.iselearning.services.models.questionnaire.QuestionnaireAnswerModel
import pt.iselearning.services.service.questionnaires.QuestionnaireAnswerServices
import pt.iselearning.services.util.QUESTIONNAIRE_ANSWER_PATTERN

/**
 * Handler responsible to respond to requests regard QuestionnaireAnswer entity
 */
@RestController
@RequestMapping(QUESTIONNAIRE_ANSWER_PATTERN, produces = ["application/json"])
class QuestionnaireAnswerController(
        private val questionnaireAnswerServices: QuestionnaireAnswerServices
) {

    /**
     * Method to create an questionnaire answer.
     *
     * @param ucb helps build URLs
     * @param questionnaireAnswerModel represents a json object that represents a object of the type QuestionnaireAnswerModel
     * @return ResponseEntity<QuestionnaireAnswer> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createQuestionnaireAnswer")
    fun createQuestionnaireAnswer(
            @RequestBody questionnaireAnswerModel: QuestionnaireAnswerModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<QuestionnaireAnswer> {
        val createdQuestionnaireAnswer = questionnaireAnswerServices.createQuestionnaireAnswer(questionnaireAnswerModel)
        val location = ucb.path(QUESTIONNAIRE_ANSWER_PATTERN)
                .path((createdQuestionnaireAnswer.questionnaireAnswerId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaireAnswer)
    }

    /**
     * Method to get a single questionnaire answer.
     *
     * @param questionnaireAnswerId represents QuestionnaireAnswer unique identifier
     * @return ResponseEntity<QuestionnaireAnswer>
     */
    @GetMapping("/{questionnaireAnswerId}", name = "getQuestionnaireAnswerById")
    fun getQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId: Int
    ): ResponseEntity<QuestionnaireAnswer> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.getQuestionnaireAnswerById(questionnaireAnswerId))
    }

    /**
     * Method to get all questionnaire answers from questionnaire instance.
     *
     * @param questionnaireInstanceId represents the QuestionnaireInstance unique identifier
     * @return ResponseEntity<List<QuestionnaireAnswer>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/questionnaireInstances/{questionnaireInstanceId}", name = "getAllQuestionnaireAnswersFromQuestionnaireInstanceId")
    fun getAllQuestionnaireAnswersFromQuestionnaireInstanceId(
            @PathVariable questionnaireInstanceId: Int
    ): ResponseEntity<List<QuestionnaireAnswer>> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.getAllQuestionnaireAnswersFromQuestionnaireInstanceId(questionnaireInstanceId))
    }

    /**
     * Method to update an questionnaire answer.
     *
     * @param questionnaireAnswerId represents a Questionnaire unique identifier
     * @param questionnaireAnswerModel json object that represents a object of the type QuestionnaireAnswerModel
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireAnswerId}", name = "updateQuestionnaireAnswerById")
    fun updateQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId: Int,
            @RequestBody questionnaireAnswerModel: QuestionnaireAnswerModel
    ): ResponseEntity<QuestionnaireAnswer> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.updateQuestionnaireAnswerById(questionnaireAnswerId,questionnaireAnswerModel))
    }

    /**
     * Method to delete a single questionnaire answer.
     *
     * @param questionnaireAnswerId represents QuestionnaireAnswer unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireAnswerId}", name = "deleteQuestionnaireAnswerById")
    fun deleteQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId: Int
    ): ResponseEntity<QuestionnaireAnswer> {
        questionnaireAnswerServices.deleteQuestionnaireAnswerById(questionnaireAnswerId)
        return ResponseEntity.noContent().build()
    }

    /**
     *
     */
    @GetMapping(name="getQuestionnaireAnswerList")
    fun getQuestionnaireAnswerList( loggedUser: User)
            : ResponseEntity<List<QuestionnaireInstanceQuestionnaireView>> {

        return ResponseEntity.ok().body(questionnaireAnswerServices.getAllQuestionnaireAnswersFromCreator(loggedUser))
    }

}