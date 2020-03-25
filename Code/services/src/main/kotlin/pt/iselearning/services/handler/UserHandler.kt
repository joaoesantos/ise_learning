package pt.iselearning.services.handler

import kotlinx.coroutines.FlowPreview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.repository.UserRepository
import reactor.core.publisher.Mono

/**
 * Handler responsible to respond to requests regard User entity
 */
@Component
class UserHandler {

    @Autowired
    private lateinit var userRepository: UserRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all users
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @FlowPreview
    fun getAllUsers(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(userRepository.findAll(), User::class.java)
    }

    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun getUserById(request: ServerRequest): Mono<ServerResponse> {
        val notFound = ServerResponse.notFound().build()
        val userId = Integer.parseInt(request.pathVariable("id"))

        return try {
            userRepository.findById(userId)
                    .flatMap { t: User ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(t))
                    }
                    .switchIfEmpty(notFound)
        } catch (e: IselearningException) {
            notFound
        }
    }


    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun createUser(request: ServerRequest): Mono<ServerResponse> {
        println("createUser")
        val badRequest = ServerResponse.badRequest().build()
        val user = request.bodyToMono(User::class.java)

        return user
                .doOnNext { u -> userRepository.save(u).subscribe() }
                .flatMap {
                    ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(it))
                }
    }

    /**
     * Method to update an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun updateUser(request: ServerRequest): Mono<ServerResponse> {
        val userRequest: Mono<User> = request.bodyToMono(User::class.java)
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return userRequest
                .flatMap { u ->
                    userRepository.findById(u.id!!)
                            .flatMap {
                                let { userRepository.save(u!!).subscribe() }
                                ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(u))
                            }
                            .switchIfEmpty(notFound)
                }
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    fun deleteUser(request: ServerRequest): Mono<ServerResponse> {
        val userId = Integer.parseInt(request.pathVariable("id"))
        val notFound = ServerResponse.notFound().build()
        val badRequest = ServerResponse.badRequest().build()

        return userRepository.findById(userId)
                .flatMap { u ->
                    let { userRepository.delete(u).subscribe() }
                    ServerResponse.ok().build()
                }
                .switchIfEmpty(notFound)
    }
}


