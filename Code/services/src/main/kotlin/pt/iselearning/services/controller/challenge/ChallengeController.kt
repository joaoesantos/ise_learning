package pt.iselearning.services.controller.challenge

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.service.challenge.ChallengeAnswerService
import pt.iselearning.services.service.challenge.ChallengeService
import pt.iselearning.services.util.CHALLENGE_PATTERN

/**
 * Handler responsible to respond to requests regard Challenge entity
 */
@RestController
@RequestMapping(CHALLENGE_PATTERN)
class ChallengeController (private val challengeService: ChallengeService, private val challengeAnswerService: ChallengeAnswerService) {
    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all challenges
     * @return ResponseEntity<List<Challenge>> represents a data stream that can hold zero or one elements of the type ServerResponse
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
     * Path variable "id" must be present
     * @param challengeId represents challenge id
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
     * Path variable "id" must be present
     * @param id represens challenge id
     * @param tags represens tags to search for
     * @param privacy represens privacy to search for
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
     * @return ResponseEntity<Challenge>
     */
    @GetMapping("/random", name = "getRandomChallenge")
    fun getRandomChallenge(): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getRandomChallenge())
    }


    /**
     * Method to create an challenge.
     * A json object that represents a object of the type Challenge must be present in the body
     * @param ucb helps build URLs
     * @param challenge represents a Challenge
     * @return ResponseEntity<Challenge> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createChallenge")
    fun createChallenge(
            @RequestBody challenge: Challenge,
            ucb: UriComponentsBuilder,
            loggedUser: User
    ): ResponseEntity<Challenge> {
        val savedChallenge = challengeService.createChallenge(challenge, loggedUser)
        val location = ucb.path("/v0/challenges")
                .path(savedChallenge!!.challengeId.toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(savedChallenge)
    }

    /**
     * Method to update an challenge.
     * A json object that represents a object of the type Challenge must be present in the body
     * @param challengeId represents a challenge Id
     * @param challenge represents a Challenge
     * @return ResponseEntity<Challenge> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{challengeId}", name = "updateChallenge")
    fun updateChallenge(
            @PathVariable challengeId: Int,
            @RequestBody challenge: Challenge,
            loggedUser: User
    ): ResponseEntity<Challenge> {
        challenge.challengeId = challengeId
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.updateChallenge(challenge, loggedUser))
    }

    /**
     * Method to delete an challenge
     * Path variable "id" must be present
     * @param challengeId represents an HTTP message
     * @return ResponseEntity<Void> represents a data stream that can hold zero or one elements of the type ServerResponse
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


