package pt.iselearning.services.models

import javax.validation.constraints.Positive

class ChallengeAnswerOutputModel (

        @field:Positive(message = "Challenge id must be positive")
        var challengeId : Int,

        var languages : List<String>?,

        var description : String,

        var answer: AnswerModel
)