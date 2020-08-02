package pt.iselearning.services.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration.AccessLevel
import org.modelmapper.convention.MatchingStrategies
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import pt.iselearning.services.filter.AuthenticationFilter
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.resolver.UserArgumentResolver
import pt.iselearning.services.service.AuthenticationService
import pt.iselearning.services.util.addQuestionnaireChallengeMappings

@Configuration
class ApplicationConfiguration() : WebMvcConfigurer {

    @Bean
    fun createRequestMappingHandlerMapping(): RequestMappingHandlerMapping? {
        // add properties here
        return RequestMappingHandlerMapping()
    }

    @Bean
    fun createModelMapper(questionnaireRepository: QuestionnaireRepository, challengeRepository: ChallengeRepository) : ModelMapper {
        val mm = ModelMapper();

        mm.configuration.matchingStrategy = MatchingStrategies.STRICT
        mm.configuration.fieldAccessLevel = AccessLevel.PRIVATE
        mm.configuration.isFieldMatchingEnabled = true
        mm.configuration.isSkipNullEnabled = true

        addQuestionnaireChallengeMappings(mm, questionnaireRepository, challengeRepository)
        return mm
    }

    @Bean
    fun createEmailValidator() : EmailValidator = EmailValidator()

    @Bean
    fun authenticationFilterRegistration(authenticationService: AuthenticationService, objectMapper: ObjectMapper): FilterRegistrationBean<AuthenticationFilter>? {
        val registrationBean: FilterRegistrationBean<AuthenticationFilter>
                = FilterRegistrationBean()

        registrationBean.initParameters
        registrationBean.filter = AuthenticationFilter(authenticationService, objectMapper)
        registrationBean.addUrlPatterns("/v0/login") //TODO: os gets nao precisam de autenticacao ,"/v0/challenges/*"

        return registrationBean
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserArgumentResolver())
        super.addArgumentResolvers(resolvers)
    }

}