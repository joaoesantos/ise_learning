package pt.iselearning.services.controller.challenge

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
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
     * Method to create an challenge answer.
     *
     * @param challengeAnswer json object that represents a object of the type challenge answer model
     * @param loggedUser user that is calling the service
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PostMapping(name = "createChallengeAnswer")
    fun createChallengeAnswer(
            @RequestBody challengeAnswer: ChallengeAnswer,
            ucb: UriComponentsBuilder,
            loggedUser: User
    ): ResponseEntity<ChallengeAnswer> {
        val challengeAnswer = challengeAnswerService.createChallengeAnswer(challengeAnswer, loggedUser)
        val location = ucb.path("/v0/challengeAnswers")
                .path(challengeAnswer!!.challengeAnswerId.toString())
                .build()
                .toUri()
        return ResponseEntity.created(location).body(challengeAnswer)
    }

    /**
     * Method to get an challenge answer
     *
     * Path variable "id" must be present
     * @param challengeId path variable with Challenge unique identifier
     * @param userId path variable with creator unique identifier
     * @param loggedUser user that is calling the service
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping("/{challengeId}/answers/users/{userId}", name = "getChallengeAnswerByUserId")
    fun getChallengeAnswerByUserId(
            @PathVariable challengeId: Int,
            @PathVariable userId: Int,
            loggedUser: User
    ): ResponseEntity<ChallengeAnswer> {
        val challengeAnswer = challengeAnswerService.getChallengeAnswerByChallengeIdAndUserId(challengeId, userId, loggedUser)
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(challengeAnswer)
    }

    /**
     * Method to update an challenge answer.
     *
     * A json object that represents a object of the type challenge answer must be present in the body
     * @param challengeAnswerId  represents ChallengeAnswer unique identifier
     * @param loggedUser user that is calling the service
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping("/{challengeAnswerId}", name = "updateChallengeAnswer")
    fun updateChallengeAnswer(
            @PathVariable challengeAnswerId: Int,
            @RequestBody challengeAnswer: ChallengeAnswer,
            loggedUser: User
    ): ResponseEntity<ChallengeAnswer> {
        challengeAnswer.challengeAnswerId = challengeAnswerId
        return ResponseEntity.ok().contentType(APPLICATION_JSON)
                .body(challengeAnswerService.updateChallengeAnswer(challengeAnswer, loggedUser))
    }

    /**
     * Method to delete an challenge answer
     *
     * @param challengeAnswerId  represents ChallengeAnswer unique identifier
     * @param loggedUser user that is calling the service
     * @return No Content
     */
    @DeleteMapping("/{challengeAnswerId}", name = "deleteChallengeAnswer")
    fun deleteChallengeAnswer(
            @PathVariable challengeAnswerId: Int,
            loggedUser: User
    ): ResponseEntity<Void> {
        challengeAnswerService.deleteChallengeAnswer(challengeAnswerId, loggedUser)
        return ResponseEntity.ok().build()
    }

}


