package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.service.UserService
import pt.iselearning.services.models.user.CreateUserModel
import pt.iselearning.services.models.user.UserPasswordModel
import pt.iselearning.services.models.user.UserProfileModel
import pt.iselearning.services.models.user.UserModel
import pt.iselearning.services.service.AuthenticationService
import pt.iselearning.services.util.VERSION

/**
 * Handler responsible to respond to requests regard User entity
 */
@RestController
@RequestMapping("/${VERSION}/users", produces = ["application/json"])
class UserController(
        private val userService: UserService,
        private val authenticationService: AuthenticationService
) {

    /**
     * Method to create an user.
     *
     * A json object that represents a object of the type User must be present in the body
     * @param createUserModel represents the user's information
     * @return ResponseEntity<UserModel> represents the newly created user
     */
    @PostMapping(name = "createUser")
    fun createUser(
            @RequestBody createUserModel: CreateUserModel,
            ucb: UriComponentsBuilder
    ): ResponseEntity<UserModel> {
        val user = userService.createUser(createUserModel)
        val location = ucb.cloneBuilder().path("/${user.userId}")
        return ResponseEntity.created(location.build().toUri()).body(user)
    }

    /**
     * Method to get all users
     *
     * @return ResponseEntity<Iterable<UserModel>> represents a a collection that can hold zero or one elements of the type User
     */
    @GetMapping(name = "getAllUsers")
    fun getAllUsers(): ResponseEntity<Iterable<UserModel>> = ResponseEntity.ok().body(userService.getAllUsers())

    /**
     * Method to get a single user by its unique identifier.
     *
     * Path variable "id" must be present
     * @param id represents user id
     * @return ResponseEntity<UserModel>
     */
    @GetMapping("/{id}", name = "getUserById")
    fun getUserById(
            @PathVariable id: Int
    ): ResponseEntity<UserModel> = ResponseEntity.ok(userService.getUserById(id))

    /**
     * Method to get logged user.
     *
     * Path variable "id" must be present
     * @return ResponseEntity<UserModel>
     */
    @GetMapping("/me", name = "getMe")
    fun getMe(
            @RequestHeader(value = "Authorization") authorization : String
    ): ResponseEntity<UserModel> {
        val loggedUser = authenticationService.getLoggedInUser(authorization)
        return ResponseEntity.ok(userService.getUserById(loggedUser.userId!!))
}
    /**
     * Method to update logged user information.
     *
     * A json object that represents a object of the type User must be present in the body
     * @param userProfileModel represents the information an user can update
     * @return ResponseEntity<UserModel>
     */
    @PatchMapping("/me", name = "updateMe")
    fun updateUser(
            @RequestBody userProfileModel: UserProfileModel,
            @RequestHeader(value = "Authorization") authorization: String
    ): ResponseEntity<UserModel> {
        val loggedUser = authenticationService.getLoggedInUser(authorization)
        val user = userService.updateUserInformation(userProfileModel, loggedUser.userId!!)
        return ResponseEntity.ok(user)
    }

    /**
     * Method to update an user credentials.
     *
     * A json object that represents a object of the type UserPasswordModel must be present in the body
     * @param password represents a UserPasswordModel
     * @return ResponseEntity<UserModel>
     */
    @PutMapping("/me/password", name = "updatePassword")
    fun updatePassword(
            @RequestBody password: UserPasswordModel,
            @RequestHeader(value = "Authorization") authorization: String
    ): ResponseEntity<UserModel> {
        val loggedUser = authenticationService.getLoggedInUser(authorization)
        val user = userService.updatePassword(password.password, loggedUser.userId!!)
        return ResponseEntity.ok(user)
    }

    /**
     * Method to delete an user
     *
     * @param id represents user id
     * @return No Content
     */
    @DeleteMapping("/{userId}", name = "deleteUserById")
    fun deleteUserById(
            @PathVariable userId: Int
    ): ResponseEntity<Unit> {
        userService.deleteUserById(userId)
        return ResponseEntity.noContent().build()
    }

    /**
     * Method to delete logged user
     *
     * @return No Content
     */
    @DeleteMapping("/me", name = "deleteMe")
    fun deleteMe(
            @RequestHeader(value = "Authorization") authorization : String
    ): ResponseEntity<Unit> {
        val loggedUser = authenticationService.getLoggedInUser(authorization)
        userService.deleteUserById(loggedUser.userId!!)
        return ResponseEntity.noContent().build()
    }

}


