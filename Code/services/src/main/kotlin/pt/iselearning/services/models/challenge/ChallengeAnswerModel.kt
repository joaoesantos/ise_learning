package pt.iselearning.services.models.challenge

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.models.AnswerModel
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * Model used for input data manipulation of ChallengeAnswer domain
 */
@Validated
class ChallengeAnswerModel(

        @field:Positive(message = "Challenge id must be positive")
        var challengeId : Int,

        @field:Valid
        var answer : AnswerModel
)