package pt.iselearning.services.route

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import pt.iselearning.services.handler.UserHandler

/**
 * Class configuration for the User endpoints
 */
@Configuration
class UserRouter {

    /**
     * Router function to register handler functions
     */
    @FlowPreview
    @Bean
    fun route1(userHandler: UserHandler) = router {
        ("/users").nest {
            GET("", userHandler::getAllUsers)
            GET("/{id}", userHandler::getUserById)
            POST("", userHandler::createUser)
            PUT("", userHandler::updateUser)
            DELETE("/{id}", userHandler::deleteUser)
        }
    }
}