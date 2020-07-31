package pt.iselearning.services.util

/**
 * Class containing auxiliary constant values
 */
class Constants private constructor() {
    companion object {
        private const val VERSION = "v0"

        //region QUESTIONNAIRE PATH CONSTANTS
        const val QUESTIONNAIRE_PATH: String = "/$VERSION/questionnaires"
        const val QUESTIONNAIRE_INSTANCE_PATH: String = "/$VERSION/questionnairesInstances"
        const val QUESTIONNAIRE_ANSWERS: String = "/$VERSION/questionnairesAnswers"
        //endregion

    }
}