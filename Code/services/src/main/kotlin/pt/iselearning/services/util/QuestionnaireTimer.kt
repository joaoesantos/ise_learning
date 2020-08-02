package pt.iselearning.services.util

import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository

class QuestionnaireTimer {

    companion object {

        /**
         * Checks if questionnaire instance can be access due to timer variables and updates database according
         */
        fun checkQuestionnaireInstanceTimeout(
                questionnaireInstance: QuestionnaireInstance,
                questionnaireInstanceRepository: QuestionnaireInstanceRepository
        ) {

            // if questionnaire is finish can no longer be accessed
            if (questionnaireInstance.isFinish) {
                throw ServerException("Closed.",
                        "Questionnaire ${questionnaireInstance.questionnaireInstanceUuid} is closed.", ErrorCode.FORBIDDEN)
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
                    questionnaireInstanceRepository.save(questionnaireInstance)
                    throw ServerException("Timeout.",
                            "Questionnaire ${questionnaireInstance.questionnaireInstanceUuid}  is closed", ErrorCode.FORBIDDEN)
                }
            }
        }
    }

}