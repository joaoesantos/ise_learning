package pt.iselearning.services.route

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import pt.iselearning.services.handler.ChallengeHandler

/**
 * Class configuration for the Challenge endpoints
 */
@Configuration
class ChallengeRouter {
    @FlowPreview
    @Bean
    fun route(challengeHandler: ChallengeHandler) = router {
        ("/v0/challenges").nest {
            GET("", challengeHandler::getAllChallenges)
            POST("", challengeHandler::createChallenge)
            GET("/{challengeId}", challengeHandler::getChallengeById)
            PUT("/{challengeId}", challengeHandler::updateChallenge)
            DELETE("/{challengeId}", challengeHandler::deleteChallenge)
            GET("/{userId}", challengeHandler::getUserChallenges)
            POST("/{challengeId}/answers", challengeHandler::createChallengeAnswer)
            GET("/{challengeId}/answers/{userId}", challengeHandler::getChallengeAnswerByUser)
            PUT("/{challengeId}/answers/{userId}", challengeHandler::updateChallengeAnswerByUser)
            DELETE("/{challengeId}/answers/{userId}", challengeHandler::deleteChallengeAnswerByUser)
        }
    }
}