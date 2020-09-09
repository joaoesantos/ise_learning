package pt.iselearning.services.models.challenge

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.models.SolutionModel
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Model used for input data manipulation of Challenge domain
 */
@Validated
class ChallengeModel(

        @field:Size(min = 1, max = 50, message = "Challenge description must less than 50 characters")
        var challengeTitle: String,

        @field:NotNull
        var challengeText: String,

        @field:NotNull
        var isPrivate: Boolean,

        @field:NotNull
        var solutions: MutableList<SolutionModel>

)