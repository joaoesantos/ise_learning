package pt.iselearning.services.configuration

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.modelmapper.config.Configuration.*
import org.modelmapper.convention.MatchingStrategies
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@Configuration
class ApplicationConfiguration {

    @Bean
    fun createModelMapper() : ModelMapper {
        val mm = ModelMapper();

        mm.configuration.matchingStrategy = MatchingStrategies.STRICT
        mm.configuration.fieldAccessLevel = AccessLevel.PRIVATE
        mm.configuration.isFieldMatchingEnabled = true
        mm.configuration.isSkipNullEnabled = true

        return mm
    }

    @Bean
    fun createEmailValidator() : EmailValidator = EmailValidator()

}