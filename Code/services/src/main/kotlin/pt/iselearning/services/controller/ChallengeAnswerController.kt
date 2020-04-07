package pt.iselearning.services.controller


import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.ChallengeAnswer
import pt.iselearning.services.repository.ChallengeAnswerRepository
import java.lang.String

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/v0/challengeAnswers")
class ChallengeAnswerController {
    constructor(challengeAnswerRepository: ChallengeAnswerRepository) {
        this.challengeAnswerRepository = challengeAnswerRepository
    }
    private lateinit var challengeAnswerRepository: ChallengeAnswerRepository

    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping
    fun createChallenge(@RequestBody challengeAnswer: ChallengeAnswer, ucb : UriComponentsBuilder): ResponseEntity<ChallengeAnswer> {
        val badRequest : ResponseEntity<Challenge> = ResponseEntity.badRequest().build()
        val challengeAnswer = challengeAnswerRepository.save(challengeAnswer);
        val location = ucb.path("/v0/challengeAnswers")
                .path(String.valueOf(challengeAnswer.challengeAnswerId))
                .build()
                .toUri()
        return ResponseEntity.created(location).body(challengeAnswer)
    }

    /**
     * Method to update an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{challengeAnswerId}")
    fun updateChallenge(@PathVariable challengeAnswerId : Int, @RequestBody challengeAnswer: ChallengeAnswer): ResponseEntity<ChallengeAnswer> {
        val notFound : ResponseEntity<ChallengeAnswer> = ResponseEntity.notFound().build()
        challengeAnswer.challengeAnswerId = challengeAnswerId
        return challengeAnswerRepository.findById(challengeAnswerId)
                .map {
                    ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeAnswerRepository.save(challengeAnswer))
                }
                .orElse(notFound)
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{challengeAnswerId}")
    fun deleteChallenge(@PathVariable challengeAnswerId : Int): ResponseEntity<Void> {
        val notFound : ResponseEntity<Void> = ResponseEntity.notFound().build()

        return challengeAnswerRepository.findById(challengeAnswerId)
                .map { u ->
                    challengeAnswerRepository.delete(u)
                    val resp : ResponseEntity<Void> = ResponseEntity.ok().build()
                    resp
                }
                .orElse(notFound)
    }
}


