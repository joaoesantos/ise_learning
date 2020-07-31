package pt.iselearning.services.models.questionnaire

import javax.validation.constraints.Positive

/**
 * Represents the issue state model received by HTTP Api.
 */
class QuestionnaireChallengeCollectionModel(
        @field:Positive(message = "Questionnaire id must be positive")
        val questionnaireId: Int,
        @field:Positive(message = "Challenge ids must be positive")
        val challengeIds: List<Int>
) {}