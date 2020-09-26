package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.models.user.UserModel
import pt.iselearning.services.service.AuthenticationService
import pt.iselearning.services.util.VERSION

/**
 * Handler responsible to respond to requests regarding authentication
 */
@Controller
class AuthenticationController(
        private val authenticationService: AuthenticationService
) {
    /**
     * Login action
     *
     * @param authorizationHeader http header with key equals to Authorization
     */
    @PostMapping("/${VERSION}/login", name = "login")
    fun login(
            @RequestHeader("Authorization") authorizationHeader: String,
            ucb: UriComponentsBuilder
    ) : ResponseEntity<UserModel> {
        val user = authenticationService.getLoggedInUser(authorizationHeader)
        val location = ucb.cloneBuilder().path("/${user.userId}")
        return ResponseEntity.created(location.build().toUri()).body(user)
    }
}