package pt.iselearning.services.configuration

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import pt.iselearning.services.controller.UserController
import pt.iselearning.services.domain.User

@Configuration
class UserConfiguration {

    @FlowPreview
    @Bean
    fun userRouter(controller: UserController): RouterFunction<ServerResponse> = router{
        ("/users").nest{
            GET("") { _ ->
                ServerResponse.ok().body(controller.getAllUsers())
            }

            GET("/{id}") { req ->
                ServerResponse.ok().body(controller.getUserById(req.pathVariable("id").toInt()))
            }

            accept(MediaType.APPLICATION_JSON)
            POST("") {req ->
                ServerResponse.ok().body(req.bodyToMono(User::class.java).doOnNext { user ->
                        controller.createUser(user)                })
            }

            accept(MediaType.APPLICATION_JSON)
            PUT("") {req ->
                ServerResponse.ok().body(req.bodyToMono(User::class.java).doOnNext { user ->
                    run {
                        controller.updateUser(user)
                    }
                })
            }
        }
    }
}
