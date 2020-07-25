package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.service.questionnaires.QuestionnaireAnswerServices
import pt.iselearning.services.util.Constants

/**
 * Handler responsible to respond to requests regard QuestionnaireAnswer domain
 */
@RestController
@RequestMapping(Constants.QUESTIONNAIRE_ANSWER_PATH, produces = ["application/json"])
class QuestionnaireAnswerController(
        private val questionnaireAnswerServices: QuestionnaireAnswerServices
) {

    /**
     * Method to create an questionnaire answer.
     *
     * A json object that represents a object of the type QuestionnaireAnswer must be present in the body
     * @param ucb helps build URLs
     * @param questionnaireAnswer represents a QuestionnaireAnswer
     * @return ResponseEntity<QuestionnaireAnswer> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createQuestionnaire(
            @RequestBody questionnaireAnswer: QuestionnaireAnswer,
            ucb : UriComponentsBuilder
    ): ResponseEntity<QuestionnaireAnswer> {
        val createdQuestionnaireAnswer = questionnaireAnswerServices.createQuestionnaireAnswer(questionnaireAnswer)
        val location = ucb.path("/${Constants.VERSION}/questionnaireAnswers")
                .path((createdQuestionnaireAnswer!!.questionnaireAnswerId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(createdQuestionnaireAnswer)
    }

    /**
     * Method to get a single questionnaire answer.
     *
     * Path variable "questionnaireAnswerId" must be present
     * @param questionnaireAnswerId represents QuestionnaireAnswer unique identifier
     * @return ResponseEntity<QuestionnaireAnswer>
     */
    @GetMapping("/{questionnaireAnswerId}")
    fun getQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId: Int
    ): ResponseEntity<QuestionnaireAnswer> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.getQuestionnaireAnswerById(questionnaireAnswerId))
    }

    /**
     * Method to get all questionnaire answers from questionnaire instance.
     *
     * Path variable "questionnaireInstanceId" must be present
     * @param questionnaireInstanceId represents the QuestionnaireInstance unique identifier
     * @return ResponseEntity<List<QuestionnaireAnswer>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/questionnaireInstance/{questionnaireInstanceId}")
    fun getAllQuestionnaireAnswersFromQuestionnaireInstance(
            @PathVariable questionnaireInstanceId: Int
    ): ResponseEntity<List<QuestionnaireAnswer>> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.getAllQuestionnaireAnswersFromQuestionnaireInstanceId(questionnaireInstanceId))
    }

    /**
     * Method to update an questionnaire answer.
     *
     * A json object that represents a object of the type QuestionnaireAnswer must be present in the body
     * @param questionnaireAnswerId represents a Questionnaire unique identifier
     * @param questionnaireAnswer represents a QuestionnaireAnswer object
     * @return ResponseEntity<Questionnaire> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{questionnaireAnswerId}")
    fun updateQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId : Int,
            @RequestBody questionnaireAnswer: QuestionnaireAnswer
    ): ResponseEntity<QuestionnaireAnswer> {
        questionnaireAnswer.questionnaireAnswerId = questionnaireAnswerId
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireAnswerServices.updateQuestionnaireAnswerById(questionnaireAnswer))
    }

    /**
     * Method to delete a single questionnaire answer.
     *
     * Path variable "questionnaireAnswerId" must be present
     * @param questionnaireAnswerId represents QuestionnaireAnswer unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireAnswerId}")
    fun deleteQuestionnaireAnswerById(
            @PathVariable questionnaireAnswerId: Int
    ): ResponseEntity<QuestionnaireAnswer> {
        questionnaireAnswerServices.deleteQuestionnaireAnswerById(questionnaireAnswerId)
        return ResponseEntity.noContent().build()
    }

}