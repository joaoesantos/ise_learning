package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire instance object
 */
@Validated
@Service
class QuestionnaireChallengeServices(
        private val questionnaireRepository: QuestionnaireRepository,
        private val challengeRepository: ChallengeRepository,
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository
) {

    /**
     * Add a challenge to a questionnaire
     * @param questionnaireChallenge object information
     * @return added questionnaire challenge
     */
    @Validated
    fun createQuestionnaireChallenge(@Valid questionnaireChallenge: QuestionnaireChallenge): QuestionnaireChallenge {
        val questionnaire = questionnaireRepository.findById(questionnaireChallenge.questionnaire?.questionnaireId!!)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireChallenge.questionnaire?.questionnaireId!!)
        val challenge = challengeRepository.findById(questionnaireChallenge.challenge?.challengeId!!)
        CustomValidators.checkIfChallengeExists(challenge, questionnaireChallenge.challenge?.challengeId!!)

        return questionnaireChallengeRepository.save(questionnaireChallenge)
    }

    /**
     * Get all challenges from an questionnaire by its unique identifier
     * @param questionnaireId identifier of challenge object
     * @return List of challenge objects
     */
    @Validated
    fun getAllChallengesByQuestionnaireId(@Positive questionnaireId: Int) : List<Challenge> {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireId)

        val challenges = questionnaireChallengeRepository.getAllChallengesByQuestionnaireId(questionnaireId)
        if (challenges.isEmpty()) {
            throw ServerException("Questionnaire instances not found.",
                    "There are no questionnaire instances for selected questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }

        return challenges
    }

    /**
     * Remove a challenge from a questionnaire by its unique identifier
     * @param questionnaireId identifier of object questionnaire
     * @param challengeId identifier of object challenge
     */
    @Validated
    fun removeChallengeFromQuestionnaireById(@Positive questionnaireId: Int, @Positive challengeId: Int) {

    }

}