package pt.iselearning.services.controller.questionnaire;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.iselearning.services.domain.questionnaires.Questionnaire;
import pt.iselearning.services.service.questionnaires.QuestionnaireServices;
import pt.iselearning.services.util.Constants

/**
 * Handler responsible to respond to requests regard Questionnaire entity
 */
@RestController
@RequestMapping(Constants.QUESTIONNAIRE_PATH, produces = ["application/json"])
public class QuestionnaireController(
        private val questionnaireServices: QuestionnaireServices
) {

    /**
     * Method to get a single questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents challenge id
     * @return ResponseEntity<Questionnaire>
     */
    @GetMapping("/{questionnaireId}")
    fun getQuestionnaireById(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<Questionnaire> {
        return ResponseEntity.ok().body(questionnaireServices.getQuestionnaireById(questionnaireId))
    }

}
