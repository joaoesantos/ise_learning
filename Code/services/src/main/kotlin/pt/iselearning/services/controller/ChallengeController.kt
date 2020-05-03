package pt.iselearning.services.controller

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.service.ChallengeAnswerService
import pt.iselearning.services.service.ChallengeService
import java.lang.String

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/v0/challenges")
class ChallengeController (private val challengeService: ChallengeService, private val challengeAnswerService: ChallengeAnswerService) {
    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all users
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping
    fun getAllChallenges(@RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.getAllChallenges(tags, privacy))
    }


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/{challengeId}")
    fun getChallengeById(@PathVariable challengeId : Int): ResponseEntity<Challenge> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getChallengeById(challengeId))
    }


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/users/{userId}")
    fun getChallengeByUserId(@PathVariable userId : Int, @RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?) : ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeService.getChallengeByUserId(userId, tags, privacy))
    }


    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createChallenge(@RequestBody challenge: Challenge, ucb : UriComponentsBuilder): ResponseEntity<Challenge> {
        val savedChallenge = challengeService.createChallenge(challenge)
        val location = ucb.path("/v0/challenges")
                .path(String.valueOf(savedChallenge!!.challengeId))
                .build()
                .toUri()
        return ResponseEntity.created(location).body(savedChallenge)
    }

    /**
     * Method to update an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{challengeId}")
    fun updateChallenge(@PathVariable challengeId : Int, @RequestBody challenge: Challenge): ResponseEntity<Challenge> {
        challenge.challengeId = challengeId
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeService.updateChallenge(challenge))
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{challengeId}")
    fun deleteChallenge(@PathVariable challengeId : Int): ResponseEntity<Void> {
        challengeService.deleteChallenge(challengeId)
        return ResponseEntity.ok().build()
    }
}


