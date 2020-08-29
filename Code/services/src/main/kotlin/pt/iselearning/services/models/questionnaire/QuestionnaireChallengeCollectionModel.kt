package pt.iselearning.services.models.questionnaire

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Positive

/**
 * Model that contains a list of challenge ids for a questionnaire and that questionnaire id
 */
@Validated
class QuestionnaireChallengeCollectionModel(

        @field:Positive(message = "Challenge id must be positive")
        var challengeId: Int,

        var languageFilter: String?

)