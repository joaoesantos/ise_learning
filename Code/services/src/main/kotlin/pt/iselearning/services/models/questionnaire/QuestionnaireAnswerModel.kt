package pt.iselearning.services.models.questionnaire

import pt.iselearning.services.models.AnswerModel
import javax.validation.Valid
import javax.validation.constraints.Positive

class QuestionnaireAnswerModel(

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId : Int,

        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId : Int,

        @field:Positive(message = "Challenge id must be positive")
        var challengeId : Int,

        @field:Valid
        var answer: AnswerModel
)