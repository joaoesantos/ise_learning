package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import pt.iselearning.services.service.UserService
import java.util.*

/**
 * Handler responsible to respond to requests regarding authentication
 */
@Controller
class AuthenticationController(private val userService: UserService) {
    /**
     * Login action
     *
     * @param authorizationHeader http header with key equals to Authorization
     */
    @PostMapping("/login", name = "login")
    fun login(@RequestHeader("Authorization") authorizationHeader : String) : ResponseEntity<Unit> {
        val base64Decoded = String(Base64.getDecoder().decode(authorizationHeader.split(" ")[1]))
        val split = base64Decoded.split(":")
        val username = split[0]
        val password = split[1]

        return ResponseEntity.ok().build()
    }
}