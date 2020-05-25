package pt.iselearning.services.domain

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.SupportedLanguages
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

/**
 * data class that represents solution entity
 */
@Validated
@Entity
@Table(name="challenge_solution")
data class Solution (
    @Id
    @Column(name="challenge_solution_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @field:Positive(message = "Solution id must be positive")
    var solutionId : Int? =  null,

    @Column(name="challenge_code")
    var challengeCode : String?,

    @Column(name="code_language")
    @Pattern(regexp = SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES)
    var codeLanguage : String?,

    @Column(name="solution_code")
    var solutionCode : String?,

    @Column(name="unit_tests")
    var unitTests : String?
) {
    constructor() : this(null, null, null, null, null)
}
