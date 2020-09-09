package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Model used for creation of questionnaire instance domain
 */
@Validated
class QuestionnaireInstanceModel(

        @field:Positive(message = "Questionnaire instance id must be positive")
        var questionnaireId: Int,

        var questionnaireInstanceUuid: String?,

        @field:Size(min = 0, max = 50, message = "Questionnaire description must less than 50 characters")
        var description: String? = null,

        @field:Positive(message = "Timer value must be positive")
        var timer: Long? = null,

        var isFinished: Boolean? = false

)