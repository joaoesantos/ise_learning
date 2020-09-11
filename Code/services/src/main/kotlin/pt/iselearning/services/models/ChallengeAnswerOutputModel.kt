package pt.iselearning.services.models

import javax.validation.Valid
import javax.validation.constraints.Positive

class ChallengeAnswerOutputModel (
        var challengeId : Int,
        var description : String,
        var answer: AnswerModel
)