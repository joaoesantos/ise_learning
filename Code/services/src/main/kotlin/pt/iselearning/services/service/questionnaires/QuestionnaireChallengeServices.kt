package pt.iselearning.services.service.questionnaires

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeIdListModel
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
import javax.validation.Valid

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
     * Add one or multiple challenges to a questionnaire.
     *
     * @param listOfQuestionnaireChallenge object information
     * @return added questionnaire challenge
     */
    @Validated
    fun addChallengesByIdToQuestionnaire(@Valid listOfQuestionnaireChallenge: List<QuestionnaireChallenge>): List<QuestionnaireChallenge> {
        listOfQuestionnaireChallenge.iterator().forEach {
            val questionnaire = questionnaireRepository.findById(it.questionnaire?.questionnaireId!!)
            CustomValidators.checkIfQuestionnaireExists(questionnaire, it.questionnaire?.questionnaireId!!)
            val challenge = challengeRepository.findById(it.challenge?.challengeId!!)
            CustomValidators.checkIfChallengeExists(challenge, it.challenge?.challengeId!!)
        }
        CustomValidators.checkIfAllChallengesBelongToSameQuestionnaire(listOfQuestionnaireChallenge)

        val createdQuestionnaireChallenge = mutableListOf<QuestionnaireChallenge>()
        listOfQuestionnaireChallenge.iterator().forEach {
            createdQuestionnaireChallenge.add(questionnaireChallengeRepository.save(it))        }
        return createdQuestionnaireChallenge
    }

    /**
     * Remove a collection challenges from a questionnaire by its unique identifier
     *
     * @param questionnaireChallengeIdListModel model that groups a groups a collection of challenge ids and a questionnaire id
     */
    @Validated
    fun removeChallengesByIdFromQuestionnaire(@Valid questionnaireChallengeIdListModel: QuestionnaireChallengeIdListModel) {
        val questionnaire = questionnaireRepository.findById(questionnaireChallengeIdListModel.questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireChallengeIdListModel.questionnaireId)
        questionnaireChallengeIdListModel.challengeIds.iterator().forEach {
            val questionnaireChallenge = questionnaireChallengeRepository
                    .findByQuestionnaireIdAndChallengeId(questionnaireChallengeIdListModel.questionnaireId, it)
            if (questionnaireChallenge == null) {
                throw ServerException("Challenge not found.",
                        "Challenge  $it its not on the list of challenges for questionnaire $questionnaireChallengeIdListModel.questionnaireId", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        questionnaireChallengeIdListModel.challengeIds.iterator().forEach {
            val questionnaireChallenge = questionnaireChallengeRepository
                    .findByQuestionnaireIdAndChallengeId(questionnaireChallengeIdListModel.questionnaireId, it)
            questionnaireChallengeRepository.delete(questionnaireChallenge)
        }
    }

}