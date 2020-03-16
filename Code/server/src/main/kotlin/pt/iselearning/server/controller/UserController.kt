package pt.iselearning.server.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.iselearning.server.domain.User
import pt.iselearning.server.repository.UserRepository

@RestController
@RequestMapping("/")
class UserController(private val userRepository: UserRepository){

    @GetMapping("/users")
    fun getAllUsers() : List<User> = userRepository.findAll()

    @GetMapping("/users/{id}", produces = ["application/json"])
    fun getUserById(@PathVariable(value="id") userId: Int) : ResponseEntity<User>{
        /*return userRepository.findById(userId).map {
            user -> ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
        */
        return ResponseEntity.ok(User())
    }
}