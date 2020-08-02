package pt.iselearning.services.util

import pt.iselearning.services.configuration.RemoteExecutionUrls

/**
 * Class containing auxiliary constant values
 */
class Constants private constructor() {

    companion object {
        private const val VERSION = "v0"
        const val SCHEMA = "ise_learning"

        //region QUESTIONNAIRE PATH CONSTANTS
        const val QUESTIONNAIRE_PATH: String = "/$VERSION/questionnaires"
        const val QUESTIONNAIRE_CHALLENGE_PATH: String = "/$VERSION/questionnaireChallenges"
        const val QUESTIONNAIRE_INSTANCE_PATH: String = "/$VERSION/questionnaireInstances"
        const val QUESTIONNAIRE_ANSWER_PATH: String = "/$VERSION/questionnaireAnswers"
        //endregion

    }
}