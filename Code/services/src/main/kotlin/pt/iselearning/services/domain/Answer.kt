package pt.iselearning.services.domain

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.SupportedLanguages
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

/**
 * data class that represents answer entity
 */
@Validated
@Entity
@Table(name="answer")
data class Answer (
    @Id
    @Column(name="answer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @field:Positive(message = "Answer id must be positive")
    var id : Int? =  null,

    @Column(name="code_language")
    @Pattern(regexp = SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES)
    var codeLanguage : String?,

    @Column(name="answer_code")
    var answerCode : String?,

    @Column(name="unit_tests")
    var unitTests : String?,

    @Column(name="is_correct")
    var creatorId : Boolean?
) {
    constructor() : this(null, null, null, null, null)
}
