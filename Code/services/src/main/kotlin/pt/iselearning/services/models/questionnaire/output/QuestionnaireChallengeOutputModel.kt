package pt.iselearning.services.models.questionnaire.output

import pt.iselearning.services.domain.challenge.Challenge

class QuestionnaireChallengeOutputModel(
        var challenge: Challenge?,
        var languageFilter: String?
)