package pt.iselearning.services.models.questionnaire

import javax.validation.Valid

class QuestionnaireWithChallengesModel (

        @field:Valid
        var questionnaire: QuestionnaireModel,

        @field:Valid
        var challenges: List<QuestionnaireChallengeCollectionModel>

)