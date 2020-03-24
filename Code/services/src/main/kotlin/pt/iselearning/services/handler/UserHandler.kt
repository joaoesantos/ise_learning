package pt.iselearning.services.handler

import kotlinx.coroutines.FlowPreview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.repository.UserRepository
import reactor.core.publisher.Mono
import javax.print.attribute.standard.Severity

/**
 *
 */
@Component
class UserHandler{

    @Autowired
    private lateinit var userRepository : UserRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * @return
     */
    @FlowPreview
    fun getAllUsers(request: ServerRequest) : Mono<ServerResponse>  {
        println("getAlluser")
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(userRepository.findAll(), User::class.java)
    }
        //
        /**
         * @return
         */
        fun getUserById(request: ServerRequest) : Mono<ServerResponse> {
            val notFound : Mono<ServerResponse> = ServerResponse.notFound().build()
            val userId = Integer.parseInt(request.pathVariable("id"))

            return try {
                userRepository.findById(userId)
                        .flatMap { t: User ->
                            ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(t))
                        }
                        .switchIfEmpty(notFound)
            }catch (e : IselearningException) {
                println("error")
                notFound
            }
        }


        /**
         * @param
         * @return
         */
          fun createUser(request: ServerRequest): Mono<ServerResponse> {
            println("running createUser")
            val user = request.bodyToMono(User::class.java)
            val newUser = user.doOnNext{u -> userRepository.save(u).subscribe()}
            return newUser
                    .flatMap {
                        ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(it))
                    }
          }

        /**
         * @param
         * @return
         */
        fun updateUser(request : ServerRequest) : Mono<ServerResponse>{
            val user = request.bodyToMono(User::class.java)
            val notFound = ServerResponse.notFound().build()
            return user
                    .doOnNext { u ->
                        u.id?.let { userRepository.findById(it) }
            }
                    .flatMap { u ->
                        userRepository.save(u).doOnNext { t: User? ->  }
                        ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(userRepository.save(u)))
                    }
                    .switchIfEmpty(notFound)//userRepository.save(user)
        }
    }


