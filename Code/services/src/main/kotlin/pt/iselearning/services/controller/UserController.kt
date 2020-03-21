package pt.iselearning.services.controller

import kotlinx.coroutines.FlowPreview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//@RestController
//@RequestMapping("/users")
@Component
class UserController{

    @Autowired
    private lateinit var userRepository : UserRepository

    //A usar caso estejamos a tempo de usar microservices
    /*@Autowired
    private lateinit var webClient : WebClient*/

   // @GetMapping
    //@FlowPreview
    fun getAllUsers() : Flux<User> = userRepository.findAll()

    // @GetMapping("/{id}")
    //fun getUserById(@PathVariable(value = "id") userId: Int) : Mono<User> =
    fun getUserById( userId: Int) : Mono<User> =
            userRepository.findById(userId)


   // @PostMapping
   // fun createUser(@RequestBody user: User): Mono<User> {
    fun createUser(user: User): Mono<User> {
        println("running createUser")
        val u = userRepository
                .save(user)
         return u
    }

    //@PutMapping
   // fun updateUser(@RequestBody user: User) : Mono<User>{
    fun updateUser(user: User) : Mono<User>{
        println("running updateUser")
        return userRepository.save(user)
    }

}