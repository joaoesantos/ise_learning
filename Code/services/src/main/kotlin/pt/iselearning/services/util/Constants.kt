package pt.iselearning.services.util

/**
 * Class containing auxiliary constant values
 */
class Constants private constructor() {
    companion object {
        const val VERSION = "v0"
        const val SCHEMA = "ise_learning"

        //region QUESTIONNAIRE PATH CONSTANTS
        const val QUESTIONNAIRE_PATH: String = "/$VERSION/questionnaires"
        const val QUESTIONNAIRE_INSTANCE_PATH: String = "/$VERSION/questionnaireInstances"
        const val QUESTIONNAIRE_ANSWER: String = "/$VERSION/questionnaireAnswers"
        //endregion

    }
}