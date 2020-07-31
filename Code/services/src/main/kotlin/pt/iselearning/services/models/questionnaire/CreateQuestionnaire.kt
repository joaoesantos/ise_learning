package pt.iselearning.services.models.questionnaire

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Model used for creation of questionnaire entity
 */
class CreateQuestionnaire (

        @field:Size(min = 0, max = 50, message = "Questionnaire description must less than 50 characters")
        var description: String,

        @field:Positive
        var timer: Int? = null,

        @field:Positive(message = "Creator id must be positive")
        var creatorId: Int

)