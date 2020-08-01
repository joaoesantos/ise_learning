package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Model used for updates of the questionnaire and questionnaire instance entities
 */
@Validated
class UpdateQuestionnaireModel(

        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int,

        @field:Size(min = 0, max = 50, message = "Questionnaire description must less than 50 characters")
        var description: String? = null,

        @field:Positive(message = "Timer value must be NULL or positive")
        var timer: Int? = null

)