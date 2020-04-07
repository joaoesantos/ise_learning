package pt.iselearning.services.controller

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.ChallengeAnswer
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.repository.ChallengeAnswerRepository
import pt.iselearning.services.repository.ChallengeRepository
import java.lang.String

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/v0/challenges")
class ChallengeController {
    constructor(challengeRepository: ChallengeRepository, challengeAnswerRepository: ChallengeAnswerRepository) {
        this.challengeRepository = challengeRepository
        this.challengeAnswerRepository = challengeAnswerRepository
    }
    private var challengeRepository: ChallengeRepository
    private var challengeAnswerRepository: ChallengeAnswerRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all users
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping
    fun getAllChallenges(@RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?): ResponseEntity<List<Challenge>> {
        ///TODO por a cena das queries
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeRepository.findAll().toList())
    }


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/{challengeId}")
    fun getChallengeById(@PathVariable challengeId : Int): ResponseEntity<Challenge> {
        val notFound : ResponseEntity<Challenge> = ResponseEntity.notFound().build()

        return try {
            challengeRepository.findById(challengeId)
                    .map { t: Challenge ->
                        ResponseEntity.ok().contentType(APPLICATION_JSON).body(t)
                    }
                    .orElse(notFound)
        } catch (e: IselearningException) {
            notFound
        }
    }


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/users/{userId}")
    fun getChallengeByUserId(@PathVariable userId : Int, @RequestParam(required = false) tags : String?, @RequestParam(required = false) privacy : String?) : ResponseEntity<List<Challenge>> {
        ///TODO por a cena das queries
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeRepository.findAllByCreatorId(userId).toList())
    }


    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createChallenge(@RequestBody challenge: Challenge, ucb : UriComponentsBuilder): ResponseEntity<Challenge> {
        val badRequest : ResponseEntity<Challenge> = ResponseEntity.badRequest().build()
        val savedChallenge = challengeRepository.save(challenge);
        val location = ucb.path("/v0/challenges")
                .path(String.valueOf(savedChallenge.challengeId))
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
        val notFound : ResponseEntity<Challenge> = ResponseEntity.notFound().build()
        challenge.challengeId = challengeId
        return challengeRepository.findById(challengeId)
                .map {
                    ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeRepository.save(challenge))
                }
                .orElse(notFound)
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{challengeId}")
    fun deleteChallenge(@PathVariable challengeId : Int): ResponseEntity<Void> {
        val notFound : ResponseEntity<Void> = ResponseEntity.notFound().build()

        return challengeRepository.findById(challengeId)
                .map { u ->
                    challengeRepository.delete(u)
                    val resp : ResponseEntity<Void> = ResponseEntity.ok().build()
                    resp
                }
                .orElse(notFound)
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/{challengeId}/answers/users/{userId}")
    fun getChallengeAnswerByUserId(@PathVariable challengeId : Int, @PathVariable userId : Int): ResponseEntity<ChallengeAnswer> {
        val notFound: ResponseEntity<ChallengeAnswer> = ResponseEntity.notFound().build()
        return try {
            challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId)
                    .map { t: ChallengeAnswer ->
                        ResponseEntity.ok().contentType(APPLICATION_JSON).body(t)
                    }
                    .orElse(notFound)
        } catch (e: IselearningException) {
            notFound
        }
    }
}


