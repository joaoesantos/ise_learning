package pt.iselearning.services.models.questionnaire

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Model used for updates of the questionnaire entity
 */
class UpdateQuestionnaire(

        @field:Positive(message = "Questionnaire Answer id must be positive")
        var questionnaireId: Int,

        @field:Size(min = 0, max = 50, message = "Questionnaire description must less than 50 characters")
        var description: String? = null,

        @field:Positive
        var timer: Int? = null

)