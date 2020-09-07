package pt.iselearning.services.util

import pt.iselearning.services.domain.Tag
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.domain.challenge.ChallengeTag
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireAnswerRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireInstanceRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import java.util.*

/**
 * Custom Javax validators
 */

//region USER

/**
 * Validates if user is empty.
 *
 * @param user to be validated
 * @param userId identifier of object
 * @throws ServiceException when on failure to find user
 */
fun checkIfUserExists(
        user: Optional<User>,
        userId: Int
) {
    if (user.isEmpty) {
        throw ServiceException(
                "User not found.",
                "There is no user with id $userId",
                "/iselearning/user/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

/**
 * Validates if logged user is the same as the resource owner.
 *
 * @param loggedUserId logged user that is performing the action unique identifiers
 * @param resourceOwnerId resource owner user that is performing the action unique identifiers
 * @throws ServiceException when on failure to find user
 */
fun checkIfLoggedUserIsResourceOwner(
        loggedUserId: Int,
        resourceOwnerId: Int
) {
    if (loggedUserId != resourceOwnerId) {
        throw ServiceException(
                "Forbidden access to resource.",
                "User with id $loggedUserId it is not the resource owner",
                "/iselearning/user/notResourceOwner",
                ErrorCode.FORBIDDEN
        )
    }
}

//endregion

//region TAG

/**
 * Validates if tag is empty.
 *
 * @param tag to be validated
 * @param tagId identifier of object
 * @throws ServiceException when on failure to find tag
 */
fun checkIfTagExists(
        tag: Optional<Tag>,
        tagId: Int
) {
    if (tag.isEmpty) {
        throw ServiceException(
                "Tag not found.",
                "There is no tag with id: $tagId",
                "/iselearning/tag/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

//endregion

//region CHALLENGE

/**
 * Validates if challenge is empty.
 *
 * @param challenge to be validated
 * @param challengeId identifier of object
 * @throws ServiceException when on failure to find challenge
 */
fun checkIfChallengeExists(
        challenge: Optional<Challenge>,
        challengeId: Int
) {
    if (challenge.isEmpty) {
        throw ServiceException(
                "Challenge not found.",
                "There is no challenge with id $challengeId",
                "/iselearning/challenge/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

/**
 * Validates if challenge is empty.
 *
 * @param challengeRepository interface of repository for the questionnaire class
 * @param challengeId identifier of object
 * @throws ServiceException when on failure to find challenge
 */
fun checkIfChallengeExists(
        challengeRepository: ChallengeRepository,
        challengeId: Int
): Challenge {
    val challenge = challengeRepository.findById(challengeId)
    if (challenge.isEmpty) {
        throw ServiceException(
                "Challenge not found.",
                "There is no challenge with id $challengeId",
                "/iselearning/challenge/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
    return challenge.get()
}

//endregion



//region CHALLENGE TAG

/**
 * Validates if challenge tag is empty.
 *
 * @param challengeTag to be validated
 * @param challengeId identifier of object challenge
 * @param tagId identifier of object tag
 * @throws ServiceException when on failure to find challenge tag
 */
fun checkIfChallengeTagExists(
        challengeTag: Optional<ChallengeTag>,
        challengeId: Int,
        tagId: Int
) {
    if (challengeTag.isEmpty) {
        throw ServiceException(
                "Challenge tag not found.",
                "There is no challenge tag with challenge id: $challengeId and tag id: $tagId",
                "/iselearning/challengeTag/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

//endregion

//region CHALLENGE ANSWER

/**
 * Validates if challenge answer is empty.
 *
 * @param challengeAnswer to be validated
 * @param challengeAnswerId identifier of object
 * @throws ServiceException when on failure to find challenge answer
 */
fun checkIfChallengeAnswerExists(
        challengeAnswer: Optional<ChallengeAnswer>,
        challengeAnswerId: Int
) {
    if (challengeAnswer.isEmpty) {
        throw ServiceException(
                "Challenge answer not found.",
                "There is no challenge answer with id $challengeAnswerId",
                "/iselearning/challengeAnswer/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

//endregion

//region QUESTIONNAIRES

/**
 * Validates if questionnaire is empty.
 *
 * @param questionnaireRepository interface of repository for the questionnaire class
 * @param questionnaireId identifier of object
 * @return Questionnaire domain object if exists
 * @throws ServiceException when on failure to find questionnaire
 */
fun checkIfQuestionnaireExists(
        questionnaireRepository: QuestionnaireRepository,
        questionnaireId: Int
): Questionnaire
{
    val questionnaire = questionnaireRepository.findById(questionnaireId)
    if (questionnaire.isEmpty) {
        throw ServiceException(
                "Questionnaire not found.",
                "There is no questionnaire with id $questionnaireId",
                "/iselearning/questionnaire/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
    return questionnaire.get()
}

/**
 * Validates if questionnaire is empty.
 *
 * @param questionnaire to be validated
 * @param questionnaireId identifier of object
 * @throws ServiceException when on failure to find questionnaire
 */
fun checkIfQuestionnaireExists(
        questionnaire: Optional<Questionnaire>,
        questionnaireId: Int
) {
    if (questionnaire.isEmpty) {
        throw ServiceException(
                "Questionnaire not found.",
                "There is no questionnaire with id $questionnaireId",
                "/iselearning/questionnaire/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

//endregion

//region QUESTIONNAIRE INSTANCE

/**
 * Validates if questionnaire instance access time is timeout.
 *
 * @param questionnaireInstance interface of repository for the questionnaire class
 * @throws ServiceException when on failure to find questionnaire instance
 */
fun checkIfTimeout(questionnaireInstance: QuestionnaireInstance) {
    if (System.currentTimeMillis() > questionnaireInstance.endTimestamp!!) {
        throw ServiceException(
                "TIMEOUT.",
                "Time to complete questionnaire is over",
                "/iselearning/questionnaire/timeout",
                ErrorCode.FORBIDDEN
        )
    }
}

/**
 * Validates if questionnaire instance is empty.
 *
 * @param questionnaireInstanceRepository interface of repository for the questionnaire class
 * @param questionnaireInstanceId identifier of object
 * @return QuestionnaireInstance domain object if exists
 * @throws ServiceException when on failure to find questionnaire instance
 */
fun checkIfQuestionnaireInstanceExists(
        questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        questionnaireInstanceId: Int
) : QuestionnaireInstance
{
    val questionnaireInstance = questionnaireInstanceRepository.findById(questionnaireInstanceId)
    if (questionnaireInstance.isEmpty) {
        throw ServiceException(
                "Questionnaire instance not found.",
                "There is no questionnaire instance with id $questionnaireInstanceId",
                "/iselearning/questionnaireInstance/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
    return questionnaireInstance.get()
}

/**
 * Validates if questionnaire instance is empty.
 *
 * @param questionnaireInstance to be validated
 * @param questionnaireInstanceId identifier of domain
 * @throws ServiceException when on failure to find questionnaire instance
 */
fun checkIfQuestionnaireInstanceExists(
        questionnaireInstance: Optional<QuestionnaireInstance>,
        questionnaireInstanceId: Int
) {
    if (questionnaireInstance.isEmpty) {
        throw ServiceException(
                "Questionnaire instance not found.",
                "There is no questionnaire instance with id $questionnaireInstanceId",
                "/iselearning/questionnaireInstance/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

/**
 * Validates if questionnaire instance is empty.
 *
 * @param questionnaireInstanceRepository interface of repository for the questionnaire instance class
 * @param questionnaireInstanceUuid identifier of domain
 * @throws ServiceException when on failure to find questionnaire instance
 */
fun checkIfQuestionnaireInstanceExists(
        questionnaireInstanceRepository: QuestionnaireInstanceRepository,
        questionnaireInstanceUuid: String
): QuestionnaireInstance {
    val questionnaireInstance = questionnaireInstanceRepository.findByQuestionnaireInstanceUuid(questionnaireInstanceUuid)
    if (questionnaireInstance.isEmpty) {
        throw ServiceException(
                "Questionnaire instance not found.",
                "There are no questionnaire instances for selected questionnaire $questionnaireInstanceUuid",
                "/iselearning/questionnaireInstance/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
    return questionnaireInstance.get()
}

//endregion

//region QUESTIONNAIRE ANSWER

/**
 * Validates if questionnaire answer is empty.
 *
 * @param questionnaireAnswerRepository interface of repository for the questionnaire answer class
 * @param questionnaireAnswerId identifier of object
 * @return questionnaireAnswerdomain object if exists
 * @throws ServiceException when on failure to find questionnaire answer
 */
fun checkIfQuestionnaireAnswerExists(
        questionnaireAnswerRepository: QuestionnaireAnswerRepository,
        questionnaireAnswerId: Int
): QuestionnaireAnswer {
    val questionnaireAnswer = questionnaireAnswerRepository.findById(questionnaireAnswerId)
    if (questionnaireAnswer.isEmpty) {
        throw ServiceException(
                "Questionnaire answer not found.",
                "There is no questionnaire answer with id $questionnaireAnswerId",
                "/iselearning/questionnaireAnswer/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
    return questionnaireAnswer.get()
}

/**
 * Validates if questionnaire answer is empty.
 *
 * @param questionnaireAnswer to be validated
 * @param questionnaireAnswerId identifier of domain
 * @throws ServiceException when on failure to find questionnaire answer
 */
fun checkIfQuestionnaireAnswerExists(
        questionnaireAnswer: Optional<QuestionnaireAnswer>,
        questionnaireAnswerId: Int
) {
    if (questionnaireAnswer.isEmpty) {
        throw ServiceException(
                "Questionnaire answer not found.",
                "There is no questionnaire answer with id $questionnaireAnswerId",
                "/iselearning/questionnaireAnswer/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

//endregion

//region QUESTIONNAIRE-CHALLENGE

/**
 * Validates if questionnaire challenge is empty.
 *
 * @param questionnaireChallenge to be validated
 * @param questionnaireId identifier of questionnaire domain
 * @param challengeId identifier of questionnaire domain
 * @throws ServiceException when on failure to find questionnaire challenge
 */
fun checkIfQuestionnaireChallengeExists(
        questionnaireChallenge: Optional<QuestionnaireChallenge>,
        questionnaireId: Int,
        challengeId: Int
) {
    if (questionnaireChallenge.isEmpty) {
        throw ServiceException(
                "Questionnaire challenge not found.",
                "Challenge  $challengeId its not on the list of challenges for questionnaire $questionnaireId",
                "/iselearning/questionnaireChallenge/nonexistent",
                ErrorCode.ITEM_NOT_FOUND
        )
    }
}

/**
 * Validates if all QuestionnaireChallenge's share the same questionnaire unique identifier.
 *
 * @param listOfQuestionnaireChallenge to be validated
 * @throws ServiceException when on failure to find questionnaire answer
 */
fun checkIfAllChallengesBelongToSameQuestionnaire(listOfQuestionnaireChallenge: List<QuestionnaireChallengeModel>) {
    val qcId = listOfQuestionnaireChallenge.first().questionnaireId
    listOfQuestionnaireChallenge.iterator().forEach {
        if(!(it.questionnaireId == qcId)!!) {
            throw ServiceException(
                    "All challenges must be from the same questionnaire.",
                    "All challenges must be from the same questionnaire.",
                    "/iselearning/questionnaireChallenge/allChallengesNotBelongToSameQuestionnaire",
                    ErrorCode.BAD_REQUEST
            )
        }
    }
}

/**
 * Validates if language filter contains supported programming languages.
 *
 * @param languageFilter programming languages separated by commas
 * @throws ServiceException when on failure to match a supported language
 */
fun checkSupportedLanguagesForChallengeLanguageFilter(languageFilter: String?) {
    if(languageFilter == null) return
    val regex = SupportedLanguages.getRegexForSupportedLanguages().toRegex()
    val languages = languageFilter.split(",")
    languages.forEach {
        lang -> if(!regex.matches(lang))
        throw ServiceException(
                "Unsupported language.",
                "Language $lang is not supported",
                "/iselearning/codeLanguage/unsupportedCodeLanguage",
                ErrorCode.BAD_REQUEST
        )
    }
}

//endregion
