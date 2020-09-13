package pt.iselearning.services.models

import javax.validation.Valid
import javax.validation.constraints.Positive

class ChallengeAnswerOutputModel (
        var challengeId : Int,
        var languages : List<String>,
        var description : String,
        var answer: AnswerModel
)