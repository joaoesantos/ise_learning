package pt.iselearning.services.models.questionnaire

import javax.validation.constraints.Positive

class QuestionnaireAnswerModel(

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId : Int,

        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId : Int,

        @field:Positive(message = "Challenge id must be positive")
        var challengeId : Int,

        var codeLanguage : String,

        var answerCode : String,

        var unitTests : String?

)