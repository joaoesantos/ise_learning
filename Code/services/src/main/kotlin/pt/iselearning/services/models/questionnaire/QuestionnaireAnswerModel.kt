package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.models.ChallengeAnswerModel
import javax.validation.constraints.Positive

/**
 * Model used for input data manipulation of QuestionnaireAnswer domain
 */
@Validated
class QuestionnaireAnswerModel(

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId : Int,

        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId : Int,

        var  challengeAnswers : List<ChallengeAnswerModel>
)