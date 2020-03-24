package pt.iselearning.services.route

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import pt.iselearning.services.handler.UserHandler

@Configuration
class UserRouter {

    @FlowPreview
    @Bean
    fun route(userHandler: UserHandler) = router {
        ("/users").nest {
            GET("", userHandler::getAllUsers)
            GET("/{id}", userHandler::getUserById)
            POST("", userHandler::createUser)
            PUT("", userHandler::updateUser)
        }
    }
}