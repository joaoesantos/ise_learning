package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeCollectionModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfQuestionnaireExists
import pt.iselearning.services.util.checkSupportedLanguagesForChallengeLanguageFilter
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
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Add one or multiple challenges to a questionnaire.
     *
     * @param questionnaireChallengeModel object information
     * @return added questionnaire challenge
     */
    @Validated
    fun addChallengesToQuestionnaire(@Valid questionnaireChallengeModel: QuestionnaireChallengeModel): List<QuestionnaireChallenge> {
        val questionnaire = questionnaireRepository.findById(questionnaireChallengeModel.questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireChallengeModel.questionnaireId)
        questionnaireChallengeModel.challenges.iterator().forEach {
            val challenge = challengeRepository.findById(it.challengeId)
            checkIfChallengeExists(challenge, it.challengeId)
            checkSupportedLanguagesForChallengeLanguageFilter(it.languageFilter)
        }

        val createdQuestionnaireChallenge = mutableListOf<QuestionnaireChallenge>()
        questionnaireChallengeModel.challenges.iterator().forEach {
            val questionnaireChallenge = convertToEntity(questionnaireChallengeModel.questionnaireId, it)
            createdQuestionnaireChallenge.add(questionnaireChallengeRepository.save(questionnaireChallenge))
        }
        return createdQuestionnaireChallenge
    }

    /**
     * get a QuestionnaireChallenge by its questionnaire unique identifier and challenge unique identifier
     *
     * @param questionnaireId questionnaire unique identifier
     * @param challengeId challenge unique identifier
     */
    @Validated
    fun getQuestionnaireChallengeByQuestionnaireIdAndChallengeId(@Positive questionnaireId: Int, @Positive challengeId: Int): QuestionnaireChallenge {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)
        val questionnaireChallenge = questionnaireChallengeRepository
                .findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(questionnaireId, challengeId)

        if (questionnaireChallenge.isEmpty) {
            throw ServerException("Challenge not found.",
                    "Challenge  $challengeId its not on the list of challenges for questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }

        return questionnaireChallenge.get()
    }

    /**
     * Update challenges on a questionnaire.
     *
     * @param questionnaireChallengeModel object information
     * @return added questionnaire challenge
     */
    @Validated
    fun updateChallengesOnQuestionnaire(@Valid questionnaireChallengeModel: QuestionnaireChallengeModel): List<QuestionnaireChallenge> {
        removeAllChallengesFromQuestionnaire(questionnaireChallengeModel.questionnaireId)
        questionnaireChallengeModel.challenges.iterator().forEach {
            val challenge = challengeRepository.findById(it.challengeId)
            checkIfChallengeExists(challenge, it.challengeId)
            checkSupportedLanguagesForChallengeLanguageFilter(it.languageFilter)
        }

        val createdQuestionnaireChallenge = mutableListOf<QuestionnaireChallenge>()
        questionnaireChallengeModel.challenges.iterator().forEach {
            val questionnaireChallenge = convertToEntity(questionnaireChallengeModel.questionnaireId, it)
            createdQuestionnaireChallenge.add(questionnaireChallengeRepository.save(questionnaireChallenge))
        }
        return createdQuestionnaireChallenge
    }

    /**
     * Remove a collection challenges from a questionnaire by its unique identifiers
     *
     * @param questionnaireId questionnaire unique identifier
     */
    @Validated
    fun removeAllChallengesFromQuestionnaire(@Positive questionnaireId: Int) {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)

        val questionnaireChallenges = questionnaireChallengeRepository.findAllByQuestionnaireQuestionnaireId(questionnaireId)
        questionnaireChallenges.iterator().forEach {
            questionnaireChallengeRepository.delete(it)
        }
    }

    /**
     * Auxiliary function that converts QuestionnaireChallenge model to QuestionnaireChallenge domain
     */
    //private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireChallenge::class.java)
    //TODO: usar o mapper a funcionar em vez desta função auxiliar ??
    private fun convertToEntity(
            questionnaireId: Int,
            questionnaireChallengeCollectionModel: QuestionnaireChallengeCollectionModel
    ): QuestionnaireChallenge {
        val questionnaireChallenge = QuestionnaireChallenge()
        questionnaireChallenge.questionnaire = questionnaireRepository.findById(questionnaireId).get()
        questionnaireChallenge.challenge = challengeRepository.findById(questionnaireChallengeCollectionModel.challengeId).get()
        questionnaireChallenge.languageFilter = questionnaireChallengeCollectionModel.languageFilter
        return questionnaireChallenge
    }

}