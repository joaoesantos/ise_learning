package pt.iselearning.services.models.questionnaire.output

import pt.iselearning.services.models.ChallengeAnswerModel
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class QuestionnaireInstanceOutputModel(
        var questionnaireId: Int?,

        var questionnaireInstanceId: Int?,

        var questionnaireInstanceUuid: String?,

        var description: String?,

        var timer: Long?,

        var startTimestamp: Long?,

        var endTimestamp: Long?,

        var isFinish: Boolean,

        var challenges : ChallengeAnswerModel

) {
}

