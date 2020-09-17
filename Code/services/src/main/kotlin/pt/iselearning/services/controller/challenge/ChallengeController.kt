package pt.iselearning.services.controller.challenge

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.models.challenge.ChallengeModel
import pt.iselearning.services.service.challenge.ChallengeService
import pt.iselearning.services.util.CHALLENGE_PATTERN
import javax.validation.Valid

/**
 * Handler responsible to respond to requests regard Challenge entity
 */
@RestController
@RequestMapping(CHALLENGE_PATTERN, produces = ["application/json"])
class ChallengeController (
        private val challengeService: ChallengeService
) {

    /**
     * Method to create an challenge.
     *
     * @param challengeModel json object that represents a object of the type ChallengeModel
     * @param loggedUser user that is calling the service
     * @param ucb helps build URLs
     * @return ResponseEntity<Challenge>
     */
    @PostMapping(name = "createChallenge")
    fun createChallenge(
            @RequestBody challengeModel: ChallengeModel,
            ucb: UriComponentsBuilder,
            loggedUser: User
    ): ResponseEntity<Challenge> {
        val savedChallenge = challengeService.createChallenge(challengeModel, loggedUser)
        val location = ucb.path("/v0/challenges")
                .path(savedChallenge.challengeId.toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(savedChallenge)
    }

    /**
     * Method to get all challenges.
     *
     * @return ResponseEntity<List<Challenge>>
     */
    @GetMapping(name = "getAllChallenges")
    fun getAllChallenges(
            @RequestParam(required = false) tags: String?,
            @RequestParam(required = false) privacy: String?,
            loggedUser: User?
    ): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.getAllChallenges(tags, privacy, loggedUser))
    }


    /**
     * Method to get a single challenge.
     *
     * @param challengeId represents Challenge unique identifier
     * @param loggedUser user that is calling the service
     * @return ResponseEntity<Challenge>
     */
    @GetMapping("/{challengeId}", name = "getChallengeById")
    fun getChallengeById(
            @PathVariable challengeId: Int,
            loggedUser: User?
    ): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getChallengeById(challengeId, loggedUser))
    }


    /**
     * Method to get all challenges created by a specific user.
     *
     * Path variable "id" must be present
     * @param userId represents User unique identifier
     * @param tags represents tags to search for
     * @param privacy represents privacy to search for
     * @return ResponseEntity<List<Challenge>>
     */
    @GetMapping("/users/{userId}", name = "getChallengeByUserId")
    fun getChallengeByUserId(
            @PathVariable userId: Int,
            @RequestParam(required = false) tags: String?,
            @RequestParam(required = false) privacy: String?,
            loggedUser: User?
    ): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getUserChallenges(userId, tags, privacy, loggedUser))
    }

    /**
     * Method to get all challenges from a questionnaire.
     *
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @return ResponseEntity<List<Challenge>>
     */
    @GetMapping("/questionnaires/{questionnaireId}", name = "getAllChallengesByQuestionnaireId")
    fun getAllChallengesByQuestionnaireId(
            @PathVariable questionnaireId: Int,
            loggedUser: User?
    ): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getAllChallengesByQuestionnaireId(questionnaireId))
    }

    /**
     * Method to get one random challenge.
     *
     * @return ResponseEntity<Challenge>
     */
    @GetMapping("/random", name = "getRandomChallenge")
    fun getRandomChallenge(): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getRandomChallenge())
    }


    /**
     * Method to update an challenge.
     *
     * @param challengeId represents Challenge unique identifier
     * @param challengeModel json object that represents a object of the type ChallengeModel
     * @param loggedUser user that is calling the service
     * @return ResponseEntity<Challenge>
     */
    @PutMapping("/{challengeId}", name = "updateChallenge")
    fun updateChallenge(
            @PathVariable challengeId: Int,
            @RequestBody challengeModel: ChallengeModel,
            loggedUser: User
    ): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.updateChallenge(challengeId, challengeModel, loggedUser))
    }

    /**
     * Method to delete an challenge.
     *
     * @param challengeId represents Challenge unique identifier
     * @param loggedUser user that is calling the service
     * @return No Content
     */
    @DeleteMapping("/{challengeId}", name = "deleteChallenge")
    fun deleteChallenge(
            @PathVariable challengeId: Int,
            loggedUser: User
    ): ResponseEntity<Void> {
        challengeService.deleteChallenge(challengeId, loggedUser)
        return ResponseEntity.noContent().build()
    }
}


