package pt.iselearning.services.controller


import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.User
import pt.iselearning.services.service.UserService
import pt.iselearning.services.transfer.CreateUserModel
import pt.iselearning.services.transfer.UpdatePasswordModel
import pt.iselearning.services.transfer.UpdateProfileModel

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService
) {

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

    /**
     * Method to get all users
     * @return ResponseEntity<Iterable<User>> represents a a collection that can hold zero or one elements of the type User
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<Iterable<User>> = ResponseEntity.ok().body(userService.getAllUsers())


    /**
     * Method to get a single user.
     * Path variable "id" must be present
     * @param id represens user id
     * @return ResponseEntity<User>
     */
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id : Int): ResponseEntity<User> = ResponseEntity.ok(userService.getUserById(id))


    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param createUserModel represents the user's information
     * @return ResponseEntity<User> represents the newly created user
     */
    @PostMapping
    fun createUser(@RequestBody createUserModel: CreateUserModel, ucb : UriComponentsBuilder): ResponseEntity<User> {
        val user = userService.createUser(createUserModel)
        val location = ucb.cloneBuilder().path("/${user.id}")
        return ResponseEntity.created(location.build().toUri()).body(user)
    }

    /**
     * Method to update an user.
     * A json object that represents a object of the type User must be present in the body
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    //TODO refactor once application is tracking current logged user
    @PutMapping("/me")
    fun updateUser(@RequestBody updateProfileModel: UpdateProfileModel): ResponseEntity<User> {
        val user = userService.updateUserInformation(updateProfileModel, updateProfileModel.userId)
        return ResponseEntity.ok(user)
    }

    //TODO does it make sense to return the user?
    //TODO refactor once application is tracking current logged user
    @PutMapping("/me/password")
    fun updatePassword(password: UpdatePasswordModel) : ResponseEntity<User> {
        val user = userService.updatePassword(password.password, password.userId)
        return ResponseEntity.ok(user)
    }

    /**
     * Method to delete an user
     * Path variable "id" must be present
     * @param ServerRequest represents an HTTP message
     * @return Mono<ServerResponse> represents a data stream that can hold zero or one elements of the type ServerResponse
     */
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id : Int): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}


