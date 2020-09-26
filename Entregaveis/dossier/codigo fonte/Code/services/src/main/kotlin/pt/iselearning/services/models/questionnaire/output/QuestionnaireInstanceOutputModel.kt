package pt.iselearning.services.models.questionnaire.output

import pt.iselearning.services.models.ChallengeAnswerOutputModel

class QuestionnaireInstanceOutputModel(
        var questionnaireId: Int?,

        var questionnaireInstanceId: Int?,

        var questionnaireInstanceUuid: String?,

        var description: String?,

        var timer: Long?,

        var startTimestamp: Long?,

        var endTimestamp: Long?,

        var isFinish: Boolean?,

        var challenges : List<ChallengeAnswerOutputModel>?

) {
    constructor(): this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )
}

