package pt.iselearning.services.util

/**
 * Class containing auxiliary constant values
 */
class Constants private constructor() {

    companion object {
        const val VERSION = "v0"
        const val SCHEMA = "ise_learning"

        //region CHALLENGE PATTERN CONSTANTS
        const val CHALLENGE_PATTERN: String = "/$VERSION/challenges"
        const val CHALLENGE_TAG_PATTERN: String = "/$VERSION/challenges/{challengeId}/tags"
        const val CHALLENGE_ANSWER_PATTERN: String = "/$VERSION/challengeAnswers"
        //endregion

        //region QUESTIONNAIRE PATTERN CONSTANTS
        const val QUESTIONNAIRE_PATTERN: String = "/$VERSION/questionnaires"
        const val QUESTIONNAIRE_CHALLENGE_PATTERN: String = "/$VERSION/questionnaireChallenges"
        const val QUESTIONNAIRE_INSTANCE_PATTERN: String = "/$VERSION/questionnaireInstances"
        const val QUESTIONNAIRE_ANSWER_PATTERN: String = "/$VERSION/questionnaireAnswers"
        //endregion

    }
}