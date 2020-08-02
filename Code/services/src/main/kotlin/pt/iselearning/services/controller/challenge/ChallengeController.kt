package pt.iselearning.services.controller.challenge

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.service.challenge.ChallengeAnswerService
import pt.iselearning.services.service.challenge.ChallengeService

/**
 * Handler responsible to respond to requests regard Challenge entity
 */
@RestController
@RequestMapping("/v0/challenges")
class ChallengeController (private val challengeService: ChallengeService, private val challengeAnswerService: ChallengeAnswerService) {
    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all challenges
     * @return ResponseEntity<List<Challenge>> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping
    fun getAllChallenges(@RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.getAllChallenges(tags, privacy))
    }


    /**
     * Method to get a single challenge.
     * Path variable "id" must be present
     * @param id represens challenge id
     * @return ResponseEntity<Challenge>
     */
    @GetMapping("/{challengeId}")
    fun getChallengeById(@PathVariable challengeId : Int): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getChallengeById(challengeId))
    }


    /**
     * Method to get a single challenge.
     * Path variable "id" must be present
     * @param id represens challenge id
     * @param tags represens tags to search for
     * @param privacy represens privacy to search for
     * @return ResponseEntity<List<Challenge>>
     */
    @GetMapping("/users/{userId}")
    fun getChallengeByUserId(@PathVariable userId : Int, @RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?) : ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getUserChallenges(userId, tags, privacy))
    }

    /**
     * Method to get all challenges from a questionnaire.
     * Path variable "questionnaireId" must be present
     * @param questionnaireId represents Questionnaire unique identifier
     * @return ResponseEntity<List<Challenge>>
     */
    @GetMapping("/questionnaires/{questionnaireId}")
    fun getAllChallengesByQuestionnaireId(@PathVariable questionnaireId : Int) : ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getAllChallengesByQuestionnaireId(questionnaireId))
    }


    /**
     * Method to create an challenge.
     * A json object that represents a object of the type Challenge must be present in the body
     * @param ucb helps build URLs
     * @param challenge represents a Challenge
     * @return ResponseEntity<Challenge> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createChallenge(@RequestBody challenge: Challenge, ucb : UriComponentsBuilder): ResponseEntity<Challenge> {
        val savedChallenge = challengeService.createChallenge(challenge)
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
    @PutMapping("/{challengeId}")
    fun updateChallenge(@PathVariable challengeId : Int, @RequestBody challenge: Challenge): ResponseEntity<Challenge> {
        challenge.challengeId = challengeId
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.updateChallenge(challenge))
    }

    /**
     * Method to delete an challenge
     * Path variable "id" must be present
     * @param challengeId represents an HTTP message
     * @return ResponseEntity<Void> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{challengeId}")
    fun deleteChallenge(@PathVariable challengeId : Int): ResponseEntity<Void> {
        challengeService.deleteChallenge(challengeId)
        return ResponseEntity.noContent().build()
    }
}


