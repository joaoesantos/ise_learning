package pt.iselearning.services.domain.executable

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.SupportedLanguages
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
* Data class that represents the object containing code, and unit tests to be execute for a specific coding language
*/
@Validated
data class ExecutableModel(
        @Pattern(regexp = SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES)
        var language : String,

        @NotNull
        var code : String,

        @NotNull
        var unitTests : String,

        @NotNull
        var executeTests : Boolean
)