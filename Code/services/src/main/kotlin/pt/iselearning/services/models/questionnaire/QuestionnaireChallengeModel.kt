package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * Model used for creation of questionnaire-challenge domain
 */
@Validated
class QuestionnaireChallengeModel (

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int,

        @field:Valid
        var challenges: List<QuestionnaireChallengeCollectionModel>

)