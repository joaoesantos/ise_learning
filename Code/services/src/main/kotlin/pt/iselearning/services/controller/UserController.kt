package pt.iselearning.services.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.transfer.UpdateProfileModel
import java.lang.String

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all users
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> = ResponseEntity.ok().body(userRepository.findAll().toList())


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id : Int): ResponseEntity<User> {
        val notFound : ResponseEntity<User> = ResponseEntity.notFound().build()

        return try {
            userRepository.findById(id)
                    .map { t: User ->
                        ResponseEntity.ok().contentType(APPLICATION_JSON).body(t)
                    }
                    .orElse(notFound)
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
    @PostMapping
    fun createUser(@RequestBody user: User, ucb : UriComponentsBuilder): ResponseEntity<User> {
        val badRequest : ResponseEntity<User> = ResponseEntity.badRequest().build()
        val location = ucb.path("/users/")
                .path(String.valueOf(user.id))
                .build()
                .toUri()
        return ResponseEntity.created(location).body(userRepository.save(user))
    }

    /**
     * Method to update an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @PutMapping
    fun updateUser(@RequestBody user: User): ResponseEntity<User> {
        val notFound : ResponseEntity<User> = ResponseEntity.notFound().build()

        return userRepository.findById(user.id!!)
                .map {
                    ResponseEntity.ok().contentType(APPLICATION_JSON).body(userRepository.save(user))
                }
                .orElse(notFound)
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{id}")
    fun deleteUser(@RequestBody id : Int): ResponseEntity<Void> {
        val notFound : ResponseEntity<Void> = ResponseEntity.notFound().build()

        return userRepository.findById(id)
                .map { u ->
                    userRepository.delete(u)
                    val resp : ResponseEntity<Void> = ResponseEntity.noContent().build()
                    resp
                }
                .orElse(notFound)
    }

    @PostMapping("/me")
    fun updateProfile(@RequestBody profileUpdateModel : UpdateProfileModel) : ResponseEntity<User> {
        
    }

    @PostMapping("/me/password")
    fun updatePassword(@RequestBody passwordUpdateModel) : ResponseEntity<User> {

    }

}


