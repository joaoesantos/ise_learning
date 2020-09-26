package pt.iselearning.services.util

import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository

/**
 * Checks if questionnaire instance can be access due to timer variables and updates database according
 */
fun checkQuestionnaireInstanceTimeout(
        questionnaireInstance: QuestionnaireInstance,
        questionnaireInstanceRepository: QuestionnaireInstanceRepository
) {

    // if questionnaire is finish can no longer be accessed
    if (questionnaireInstance.isFinish) {
        throw ServiceException(
                "Closed.",
                "Questionnaire ${questionnaireInstance.questionnaireInstanceUuid} is closed.",
                "/iserlearning/questionnaireInstance/closed",
                ErrorCode.FORBIDDEN
        )
    }

    //if first access to resource, starts timer and updates database
    if (questionnaireInstance.startTimestamp == null) {
        questionnaireInstance.startTimestamp = System.currentTimeMillis()
        questionnaireInstanceRepository.save(questionnaireInstance)
    }

    //if timeout expires forbids action and updates database
    if (questionnaireInstance.timer != null) {
        val expireTimeMillis = questionnaireInstance.startTimestamp!! + questionnaireInstance.timer!!
        if (System.currentTimeMillis() > expireTimeMillis) {
            questionnaireInstance.endTimestamp = expireTimeMillis
            questionnaireInstance.isFinish = true
            questionnaireInstanceRepository.save(questionnaireInstance)
            throw ServiceException(
                    "Timeout.",
                    "Questionnaire ${questionnaireInstance.questionnaireInstanceUuid} is closed.",
                    "/iserlearning/questionnaireInstance/timeout",
                    ErrorCode.FORBIDDEN
            )
        }
    }
}
