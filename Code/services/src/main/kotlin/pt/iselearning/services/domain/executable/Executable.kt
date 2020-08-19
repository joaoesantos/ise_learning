package pt.iselearning.services.domain.executable

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.SupportedLanguages
import javax.validation.constraints.Pattern

/**
* data class that represents the result of an execution
*/
@Validated
data class Executable(
        @Pattern(regexp = SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES)
        var language : String,
        var code : String,
        var unitTests : String,
        var executeTests : Boolean
) {
}