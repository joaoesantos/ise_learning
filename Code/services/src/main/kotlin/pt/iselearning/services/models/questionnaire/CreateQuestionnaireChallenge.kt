package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive

@Validated
class CreateQuestionnaireChallenge (

    @Positive
    var questionnaireId: Int,

    @Positive
    var challengeId : Int,

    var languageFilter : String?

)