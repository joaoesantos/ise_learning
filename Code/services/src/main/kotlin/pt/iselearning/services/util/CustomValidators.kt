package pt.iselearning.services.util

import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import java.time.Instant
import java.util.*

/**
 * Custom Javax validators
 */
class CustomValidators {

    companion object {

        const val PRIVACY_REGEX_STRING = "\\b(private)\\b|\\b(public)\\b"
        const val TAGS_REGEX_STRING = "\\b([^,]+)([,][^,]+)*\\b"

        //region CHALLENGES

        /**
         * Validates if challenge is empty.
         *
         * @param challenge to be validated
         * @param challengeId identifier of object
         * @throws ServerException when on failure to find questionnaire
         */
        fun checkIfChallengeExists(challenge: Optional<Challenge>, challengeId: Int) {
            if (challenge.isEmpty) {
                throw ServerException("Challenge not found.",
                        "There is no challenge with id $challengeId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        //endregion

        //region QUESTIONNAIRES

        /**
         * Validates if questionnaire is empty.
         *
         * @param questionnaireRepository interface of repository for the questionnaire class
         * @param questionnaireId identifier of object
         * @throws ServerException when on failure to find questionnaire
         */
        fun checkIfQuestionnaireExists(questionnaireRepository: QuestionnaireRepository, questionnaireId: Int) {
            if (!questionnaireRepository.existsById(questionnaireId)) {
                throw ServerException("Questionnaire not found.",
                        "There is no questionnaire with id $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        /**
         * Validates if questionnaire is empty.
         *
         * @param questionnaire to be validated
         * @param questionnaireId identifier of object
         * @throws ServerException when on failure to find questionnaire
         */
        fun checkIfQuestionnaireExists(questionnaire: Optional<Questionnaire>, questionnaireId: Int) {
            if (questionnaire.isEmpty) {
                throw ServerException("Questionnaire not found.",
                        "There is no questionnaire with id $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        //endregion

        //region QUESTIONNAIRE INSTANCE

        /**
         * Validates if questionnaire instance access time is timeout.
         *
         * @param questionnaireInstance interface of repository for the questionnaire class
         * @throws ServerException when on failure to find questionnaire instance
         */
        fun checkIfTimeout(questionnaireInstance: QuestionnaireInstance) {
            if (Instant.now().isAfter(questionnaireInstance.endTimestamp?.toInstant()!!)) {
                throw ServerException("TIMEOUT.",
                        "Time to complete questionnaire is over", ErrorCode.FORBIDDEN)
            }
        }

        /**
         * Validates if questionnaire instance is empty.
         *
         * @param questionnaireInstanceRepository interface of repository for the questionnaire class
         * @param questionnaireInstanceId identifier of object
         * @throws ServerException when on failure to find questionnaire instance
         */
        fun checkIfQuestionnaireInstanceExists(questionnaireInstanceRepository: QuestionnaireInstanceRepository, questionnaireInstanceId: Int) {
            if (!questionnaireInstanceRepository.existsById(questionnaireInstanceId)) {
                throw ServerException("Questionnaire instance not found.",
                        "There is no questionnaire instance with id $questionnaireInstanceId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        /**
         * Validates if questionnaire instance is empty.
         *
         * @param questionnaireInstance to be validated
         * @param questionnaireInstanceId identifier of domain
         * @throws ServerException when on failure to find questionnaire instance
         */
        fun checkIfQuestionnaireInstanceExists(questionnaireInstance: Optional<QuestionnaireInstance>, questionnaireInstanceId: Int) {
            if (questionnaireInstance.isEmpty) {
                throw ServerException("Questionnaire instance not found.",
                        "There is no questionnaire instance with id $questionnaireInstanceId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        //endregion

        //region QUESTIONNAIRE ANSWER

        /**
         * Validates if questionnaire answer is empty.
         *
         * @param questionnaireAnswerRepository interface of repository for the questionnaire answer class
         * @param questionnaireAnswerId identifier of object
         * @throws ServerException when on failure to find questionnaire answer
         */
        fun checkIfQuestionnaireAnswerExists(questionnaireAnswerRepository: QuestionnaireAnswerRepository, questionnaireAnswerId: Int) {
            if (!questionnaireAnswerRepository.existsById(questionnaireAnswerId)) {
                throw ServerException("Questionnaire not found.",
                        "There is no questionnaire instance with id $questionnaireAnswerId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        /**
         * Validates if questionnaire answer is empty.
         *
         * @param questionnaireAnswer to be validated
         * @param questionnaireAnswerId identifier of domain
         * @throws ServerException when on failure to find questionnaire answer
         */
        fun checkIfQuestionnaireAnswerExists(questionnaireAnswer: Optional<QuestionnaireAnswer>, questionnaireAnswerId: Int) {
            if (questionnaireAnswer.isEmpty) {
                throw ServerException("Questionnaire answer not found.",
                        "There is no questionnaire answer with id $questionnaireAnswerId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        //endregion

        //region QUESTIONNAIRE-CHALLENGE

        /**
         * Validates if all QuestionnaireChallenge's share the same questionnaire unique identifier.
         *
         * @param listOfQuestionnaireChallenge to be validated
         * @throws ServerException when on failure to find questionnaire answer
         */
        fun checkIfAllChallengesBelongToSameQuestionnaire(listOfQuestionnaireChallenge: List<QuestionnaireChallenge>) {
            val qcId = listOfQuestionnaireChallenge.first().qcId
            listOfQuestionnaireChallenge.iterator().forEach {
                if(!it.qcId?.equals(qcId)!!) {
                    throw ServerException("All challenges must be from the same questionnaire.",
                            "All challenges must be from the same questionnaire.", ErrorCode.BAD_REQUEST)
                }
            }
        }

        /**
         * Validates if language filter contains supported programming languages.
         *
         * @param languageFilter programming languages separated by commas
         * @throws ServerException when on failure to match a supported language
         */
        fun checkSupportedLanguagesForChallengeLanguageFilter(languageFilter: String) {
            val regex = SupportedLanguages.getRegexForSupportedLanguages().toRegex()
            val languages = languageFilter.split(",")
            languages.forEach {
                lang -> if(!regex.matches(lang))
                throw ServerException("Unsupported language.",
                    "Language $lang is not supported", ErrorCode.BAD_REQUEST)
            }
        }

        //endregion

    }
}