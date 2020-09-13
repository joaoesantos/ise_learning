package pt.iselearning.services.controller.questionnaire.input

import pt.iselearning.services.models.ChallengeAnswerOutputModel
import javax.validation.constraints.Positive

class QuestionnaireAnswerInputModel (

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireInstanceId: Int,
        var questionnaireId : Int,
        var challenges : List<ChallengeAnswerOutputModel>?

)