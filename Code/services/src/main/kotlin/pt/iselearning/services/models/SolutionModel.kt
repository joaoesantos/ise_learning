package pt.iselearning.services.models

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.SupportedLanguages
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

/**
 * Model used for input data manipulation of Solution domain
 */
@Validated
class SolutionModel(

        @field:Positive
        var solutionId: Int?,

        @field:NotNull
        var challengeCode: String,

        @Pattern(regexp = SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES)
        var codeLanguage: String,

        @field:NotNull
        var solutionCode: String,

        @field:NotNull
        var unitTests : String

)