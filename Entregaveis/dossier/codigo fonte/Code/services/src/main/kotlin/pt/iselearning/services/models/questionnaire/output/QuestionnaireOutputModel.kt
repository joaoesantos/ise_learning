package pt.iselearning.services.models.questionnaire.output

class QuestionnaireOutputModel(
        var questionnaireId: Int?,
        var description: String?,
        var timer: Long?,
        var creatorId: Int?,
        var challenges : List<QuestionnaireChallengeOutputModel>?
) {
}