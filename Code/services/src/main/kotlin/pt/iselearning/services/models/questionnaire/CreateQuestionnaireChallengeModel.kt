package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive

/**
 * Model used for creation of questionnaire-challenge entity
 */
@Validated
class CreateQuestionnaireChallengeModel (

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int,

        @field:Positive(message = "Challenge id must be positive")
        var challengeId: Int,

        var languageFilter: String?

)