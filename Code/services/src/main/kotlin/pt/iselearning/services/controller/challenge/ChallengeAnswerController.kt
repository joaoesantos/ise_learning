package pt.iselearning.services.controller.challenge

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.service.challenge.ChallengeAnswerService
import pt.iselearning.services.util.CHALLENGE_ANSWER_PATTERN

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping(CHALLENGE_ANSWER_PATTERN, produces = ["application/json"])
class ChallengeAnswerController(
        private val challengeAnswerService: ChallengeAnswerService
){

    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createChallengeAnswer")
    fun createChallengeAnswer(
            @RequestBody challengeAnswer: ChallengeAnswer,
            ucb: UriComponentsBuilder
    ): ResponseEntity<ChallengeAnswer> {
        val challengeAnswer = challengeAnswerService.createChallengeAnswer(challengeAnswer)
        val location = ucb.path("/v0/challengeAnswers")
                .path(challengeAnswer!!.challengeAnswerId.toString())
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
    @PutMapping("/{challengeAnswerId}", name = "updateChallengeAnswer")
    fun updateChallengeAnswer(
            @PathVariable challengeAnswerId: Int,
            @RequestBody challengeAnswer: ChallengeAnswer
    ): ResponseEntity<ChallengeAnswer> {
        challengeAnswer.challengeAnswerId = challengeAnswerId
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeAnswerService.updateChallengeAnswer(challengeAnswer))
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{challengeAnswerId}", name = "deleteChallengeAnswer")
    fun deleteChallengeAnswer(
            @PathVariable challengeAnswerId: Int
    ): ResponseEntity<Void> {
        challengeAnswerService.deleteChallengeAnswer(challengeAnswerId)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/{challengeId}/answers/users/{userId}", name = "getChallengeAnswerByUserId")
    fun getChallengeAnswerByUserId(
            @PathVariable challengeId: Int,
            @PathVariable userId: Int
    ): ResponseEntity<ChallengeAnswer> {
        val challengeAnswer = challengeAnswerService.getChallengeAnswerByUserId(challengeId, userId)
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeAnswer)
    }
}


