package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.Valid

/**
 * Model used for
 */
@Validated
class QuestionnaireWithChallengesModel (

        @field:Valid
        var questionnaire: QuestionnaireModel,

        @field:Valid
        var challenges: List<QuestionnaireChallengeCollectionModel>

)