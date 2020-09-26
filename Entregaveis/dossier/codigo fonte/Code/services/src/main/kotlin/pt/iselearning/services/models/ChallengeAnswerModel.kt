package pt.iselearning.services.models

import javax.validation.Valid
import javax.validation.constraints.Positive

class ChallengeAnswerModel (
        @field:Positive(message = "Challenge id must be positive")
        var challengeId : Int,

        @field:Valid
        var answer: AnswerModel
)