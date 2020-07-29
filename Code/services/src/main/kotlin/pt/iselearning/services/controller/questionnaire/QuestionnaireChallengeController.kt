package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeIdListModel
import pt.iselearning.services.service.questionnaires.QuestionnaireChallengeServices
import pt.iselearning.services.util.Constants

/**
 * Handler responsible to respond to requests regard QuestionnaireAnswer domain
 */
@RestController
@RequestMapping(Constants.QUESTIONNAIRE_CHALLENGE_PATH, produces = ["application/json"])
class QuestionnaireChallengeController(
        private val questionnaireChallengeServices: QuestionnaireChallengeServices
) {

    /**
     * Method to add one or multiple challenges to a single questionnaire.
     *
     * A json object that represents a object of the type List<QuestionnaireChallenge> must be present in the body
     * @param ucb helps build URLs
     * @param questionnaireChallenges represents a collection of QuestionnaireChallenge
     * @return ResponseEntity<List<QuestionnaireChallenge>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun addChallengesByIdToQuestionnaire(
            @RequestBody questionnaireChallenges: List<QuestionnaireChallenge>,
            ucb : UriComponentsBuilder
    ): ResponseEntity<List<QuestionnaireChallenge>> {
        val addedQuestionnaireChallenge = questionnaireChallengeServices.addChallengesByIdToQuestionnaire(questionnaireChallenges)
        val location = ucb.path("/${Constants.VERSION}/questionnaireChallenges")
                .path((addedQuestionnaireChallenge!!.first().qcId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(addedQuestionnaireChallenge)
    }


    /**
     * Method to get a single questionnaire-challenge.
     *
     * A json object that represents a object of the type LQuestionnaireChallenge> must be present in the body
     * Path variables "questionnaireId" and "challengeId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @param challengeId represents Challenge unique identifier
     * @return ResponseEntity<QuestionnaireChallenge> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/{questionnaireId}/{challengeId}")
    fun getQuestionnaireChallengeByChallengeIdAndQuestionnaireId(
            @PathVariable questionnaireId: Int,
            @PathVariable challengeId: Int
    ): ResponseEntity<QuestionnaireChallenge> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireChallengeServices.getQuestionnaireChallengeByChallengeIdAndQuestionnaireId(questionnaireId,challengeId))
    }


    /**
     * Method to delete one or multiple challenge from a single questionnaire answer.
     *
     * @param questionnaireChallengeIdListModel represents an object with the information
     * of a single questionnaire unique identifier and list of challenges unique identifiers
     * @return No Content
     */
    @DeleteMapping
    fun removeChallengesByIdFromQuestionnaire(
            @RequestBody questionnaireChallengeIdListModel: QuestionnaireChallengeIdListModel
    ): ResponseEntity<QuestionnaireAnswer> {
        questionnaireChallengeServices.removeChallengesByIdFromQuestionnaire(questionnaireChallengeIdListModel)
        return ResponseEntity.noContent().build()
    }

}