package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.questionnaire.CreateQuestionnaireChallengeModel
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeCollectionModel
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
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Add one or multiple challenges to a questionnaire.
     *
     * @param listOfQuestionnaireChallenge object information
     * @return added questionnaire challenge
     */
    @Validated
    fun addChallengesByIdToQuestionnaire(@Valid listOfQuestionnaireChallenge: List<CreateQuestionnaireChallengeModel> ): List<QuestionnaireChallenge> {
        listOfQuestionnaireChallenge.iterator().forEach {
            val questionnaire = questionnaireRepository.findById(it.questionnaireId)
            CustomValidators.checkIfQuestionnaireExists(questionnaire, it.questionnaireId)
            val challenge = challengeRepository.findById(it.challengeId)
            CustomValidators.checkIfChallengeExists(challenge, it.challengeId)
            CustomValidators.checkSupportedLanguagesForChallengeLanguageFilter(it.languageFilter)
        }
        CustomValidators.checkIfAllChallengesBelongToSameQuestionnaire(listOfQuestionnaireChallenge)

        val createdQuestionnaireChallenge = mutableListOf<QuestionnaireChallenge>()
        listOfQuestionnaireChallenge.iterator().forEach {
            val questionnaireChallenge = convertToEntity(it)
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
    fun getQuestionnaireChallengeByChallengeIdAndQuestionnaireId(@Positive questionnaireId: Int, @Positive challengeId: Int): QuestionnaireChallenge {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireId)
        val questionnaireChallenge = questionnaireChallengeRepository
                .findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(questionnaireId, challengeId)

        if (questionnaireChallenge.isEmpty) {
            throw ServerException("Challenge not found.",
                    "Challenge  $challengeId its not on the list of challenges for questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }

        return questionnaireChallenge.get()
    }

    /**
     * Remove a collection challenges from a questionnaire by its unique identifiers
     *
     * @param questionnaireChallengeIdListModel model that groups a groups a collection of challenge ids and a questionnaire id
     */
    @Validated
    fun removeChallengesByIdFromQuestionnaire(@Valid questionnaireChallengeIdListModel: QuestionnaireChallengeCollectionModel) {
        val questionnaire = questionnaireRepository.findById(questionnaireChallengeIdListModel.questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireChallengeIdListModel.questionnaireId)
        questionnaireChallengeIdListModel.listOfChallengeIds.iterator().forEach {
            val questionnaireChallenge = questionnaireChallengeRepository
                    .findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(questionnaireChallengeIdListModel.questionnaireId, it)
            if (questionnaireChallenge.isEmpty) {
                throw ServerException("Challenge not found.",
                        "Challenge  $it its not on the list of challenges for questionnaire ${questionnaireChallengeIdListModel.questionnaireId}", ErrorCode.ITEM_NOT_FOUND)
            }
        }

        questionnaireChallengeIdListModel.listOfChallengeIds.iterator().forEach {
            val questionnaireChallenge = questionnaireChallengeRepository
                    .findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(questionnaireChallengeIdListModel.questionnaireId, it)
            questionnaireChallengeRepository.delete(questionnaireChallenge.get())
        }
    }

    //private fun convertToEntity(input : Any) = modelMapper.map(input, QuestionnaireChallenge::class.java)

    //TODO: tentar meter o mapper a funcionar em vez desta função auxiliar
    private fun convertToEntity(input : CreateQuestionnaireChallengeModel): QuestionnaireChallenge {
        val questionnaireChallenge = QuestionnaireChallenge()
        questionnaireChallenge.questionnaire = questionnaireRepository.findById(input.questionnaireId).get()
        questionnaireChallenge.challenge = challengeRepository.findById(input.challengeId).get()
        questionnaireChallenge.languageFilter = input.languageFilter
        return questionnaireChallenge
    }

}