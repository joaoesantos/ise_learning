package pt.iselearning.services.handler

import kotlinx.coroutines.FlowPreview
import org.postgresql.jdbc.PgResultSet.toInt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.ChallengeAnswer
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.repository.ChallengeAnswerRepository
import pt.iselearning.services.repository.ChallengeRepository
import reactor.core.publisher.Mono
import java.util.*

/**
 * Handler responsible to respond to requests regard User entity
 */
@Component
class ChallengeHandler {
    @Autowired
    private lateinit var challengeRepository: ChallengeRepository

    @Autowired
    private lateinit var challengeAnswerRepository: ChallengeAnswerRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all challenges
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @FlowPreview
    fun getAllChallenges(request: ServerRequest): Mono<ServerResponse> {
        var privacy : Optional<String> = request.queryParam("privacy");
        var tags : Optional<String> = request.queryParam("tags");

        ///falta tratares dos filtros na query

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(challengeRepository.findAll(), Challenge::class.java)
    }

    /**
     * Method to get all challenges from a specific user
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @FlowPreview
    fun getUserChallenges(request: ServerRequest): Mono<ServerResponse> {
        var userId : Optional<String> = request.queryParam("userId");
        var privacy : Optional<String> = request.queryParam("privacy");
        var tags : Optional<String> = request.queryParam("tags");

        ///falta tratares dos filtros na query

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(challengeRepository.findAllByUser(toInt(userId.get())), Challenge::class.java)
    }

    /**
     * Method to get a single challenge.
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun getChallengeById(request: ServerRequest): Mono<ServerResponse> {
        val notFound = ServerResponse.notFound().build()
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))

        return try {
            challengeRepository.findById(challengeId)
                    .flatMap { t: Challenge ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(t))
                    }
                    .switchIfEmpty(notFound)
        } catch (e: IselearningException) {
            notFound
        }
    }

    /**
     * Method to create a challenge.
     * A json object that represents a object of the type Challenge must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun createChallenge(request: ServerRequest): Mono<ServerResponse> {
        val badRequest = ServerResponse.badRequest().build()
        val challenge = request.bodyToMono(Challenge::class.java)

        return challenge
                .doOnNext { u -> challengeRepository.save(u).subscribe() }
                .flatMap {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
                }
    }

    /**
     * Method to update an challenge.
     * A json object that represents a object of the type Challenge must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun updateChallenge(request: ServerRequest): Mono<ServerResponse> {
        val challenge = request.bodyToMono(Challenge::class.java)
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))
        ///Algo aqui nao esta bem, no update a info vem no body!!!
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return challenge
                .flatMap { u ->
                    u.id = challengeId
                    challengeRepository.findById(u.id!!)
                            .flatMap {
                                let { challengeRepository.save(u!!).subscribe() }
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(u))
                            }
                            .switchIfEmpty(notFound)
                }
    }

    /**
     * Method to delete an challenge
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun deleteChallenge(request: ServerRequest): Mono<ServerResponse> {
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return challengeRepository.findById(challengeId)
                .flatMap { u ->
                    let { challengeRepository.delete(u).subscribe() }
                    ServerResponse.ok().build()
                }
                .switchIfEmpty(notFound)
    }

    /**
     * Method to create a challenge answer.
     * A json object that represents a object of the type Challenge Answer must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun createChallengeAnswer(request: ServerRequest): Mono<ServerResponse> {
        val badRequest = ServerResponse.badRequest().build()
        val challengeAnswer = request.bodyToMono(ChallengeAnswer::class.java)

        return challengeAnswer
                .doOnNext { u -> challengeAnswerRepository.save(u).subscribe() }
                .flatMap {
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(it))
                }
    }

    /**
     * Method to get a single challenge answer.
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun getChallengeAnswerByUser(request: ServerRequest): Mono<ServerResponse> {
        val notFound = ServerResponse.notFound().build()
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))
        val userId = Integer.parseInt(request.pathVariable("userId"))

        return try {
            challengeAnswerRepository.findByChallengeAndUser(challengeId, userId)
                    .flatMap { t: ChallengeAnswer ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(t))
                    }
                    .switchIfEmpty(notFound)
        } catch (e: IselearningException) {
            notFound
        }
    }

    /**
     * Method to update an challenge answer.
     * A json object that represents a object of the type Challenge answer must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun updateChallengeAnswerByUser(request: ServerRequest): Mono<ServerResponse> {
        val challengeAnswer = request.bodyToMono(ChallengeAnswer::class.java)
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))
        val userId = Integer.parseInt(request.pathVariable("userId"))
        ///Algo aqui nao esta bem, no update a info vem no body!!!
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return challengeAnswer
                .flatMap { u ->
                    u.id = challengeId
                    u.userId = userId
                    challengeAnswerRepository.findByChallengeAndUser(challengeId, userId)
                            .flatMap {
                                let { challengeAnswerRepository.save(u!!).subscribe() }
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(u))
                            }
                            .switchIfEmpty(notFound)
                }
    }

    /**
     * Method to delete an challenge answer
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun deleteChallengeAnswerByUser(request: ServerRequest): Mono<ServerResponse> {
        val challengeId = Integer.parseInt(request.pathVariable("challengeId"))
        val userId = Integer.parseInt(request.pathVariable("userId"))
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return challengeAnswerRepository.findByChallengeAndUser(challengeId, userId)
                .flatMap { u ->
                    let { challengeAnswerRepository.delete(u).subscribe() }
                    ServerResponse.ok().build()
                }
                .switchIfEmpty(notFound)
    }
}



