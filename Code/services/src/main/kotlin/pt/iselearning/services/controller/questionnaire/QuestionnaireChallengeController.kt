package pt.iselearning.services.controller.questionnaire

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.service.questionnaires.QuestionnaireChallengeServices
import pt.iselearning.services.util.QUESTIONNAIRE_CHALLENGE_PATTERN

/**
 * Handler responsible to respond to requests regard QuestionnaireAnswer domain
 */
@RestController
@RequestMapping(QUESTIONNAIRE_CHALLENGE_PATTERN, produces = ["application/json"])
class QuestionnaireChallengeController(
        private val questionnaireChallengeServices: QuestionnaireChallengeServices
) {

    /**
     * Method to add one or multiple challenges to a single questionnaire.
     *
     * @param ucb helps build URLs
     * @param listOfQuestionnaireChallenge json object that represents a collection of QuestionnaireChallengeModel
     * @return ResponseEntity<List<QuestionnaireChallenge>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "addChallengesToQuestionnaire")
    fun addChallengesToQuestionnaire(
            @RequestBody listOfQuestionnaireChallenge: QuestionnaireChallengeModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<List<QuestionnaireChallenge>> {
        val addedQuestionnaireChallenge = questionnaireChallengeServices.addChallengesToQuestionnaire(listOfQuestionnaireChallenge)
        val location = ucb.path(QUESTIONNAIRE_CHALLENGE_PATTERN)
                .path((addedQuestionnaireChallenge!!.first().qcId).toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(addedQuestionnaireChallenge)
    }

    /**
     * Method to get a single questionnaire-challenge.
     *
     * @param questionnaireId represents Questionnaire unique identifier
     * @param challengeId represents Challenge unique identifier
     * @return ResponseEntity<QuestionnaireChallenge> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/{questionnaireId}/{challengeId}", name = "getQuestionnaireChallengeByQuestionnaireIdAndChallengeId")
    fun getQuestionnaireChallengeByChallengeIdAndQuestionnaireId(
            @PathVariable questionnaireId: Int,
            @PathVariable challengeId: Int
    ): ResponseEntity<QuestionnaireChallenge> {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(questionnaireChallengeServices.getQuestionnaireChallengeByQuestionnaireIdAndChallengeId(questionnaireId,challengeId))
    }

    /**
     * Method to update the list of challenges on a single questionnaire.
     *
     * @param listOfQuestionnaireChallenge json object that represents a collection of QuestionnaireChallenge
     * @return ResponseEntity<List<QuestionnaireChallenge>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping(name = "updateChallengesOnQuestionnaire")
    fun updateChallengesByIdOnQuestionnaire(
            @RequestBody listOfQuestionnaireChallenge: QuestionnaireChallengeModel
    ): ResponseEntity<List<QuestionnaireChallenge>> {
        val addedQuestionnaireChallenge = questionnaireChallengeServices.updateChallengesOnQuestionnaire(listOfQuestionnaireChallenge)
        return ResponseEntity.ok().body(addedQuestionnaireChallenge)
    }

    /**
     * Method to delete all challenges from a single questionnaire.
     *
     * @param questionnaireId represents Questionnaire unique identifier
     * @return No Content
     */
    @DeleteMapping("/{questionnaireId}", name = "removeAllChallengesFromQuestionnaire")
    fun removeChallengesFromQuestionnaire(
            @PathVariable questionnaireId: Int
    ): ResponseEntity<QuestionnaireAnswer> {
        questionnaireChallengeServices.removeAllChallengesFromQuestionnaire(questionnaireId)
        return ResponseEntity.noContent().build()
    }

}