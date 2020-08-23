package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.models.questionnaire.QuestionnaireModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireOutputModel
import pt.iselearning.services.models.questionnaire.QuestionnaireWithChallengesModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireChallengeOutputModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.checkIfQuestionnaireExists
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the questionnaire object
 */
@Validated
@Service
class QuestionnaireServices(
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionnaireChallengeServices: QuestionnaireChallengeServices,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a questionnaire.
     *
     * @param questionnaireModel object information
     * @return created questionnaire
     */
    @Validated
    fun createQuestionnaire(@Valid questionnaireModel: QuestionnaireModel): Questionnaire {
        val questionnaire = convertToEntity(questionnaireModel)
        return questionnaireRepository.save(questionnaire)
    }

    /**
     * Create a questionnaire and adds it challenges.
     *
     * @param questionnaireWithChallengesModel object information
     * @return created questionnaire
     */
    @Validated
    @Transactional
    fun createQuestionnaireWithChallenges(@Valid questionnaireWithChallengesModel: QuestionnaireWithChallengesModel): Questionnaire? {
        val questionnaire = convertToEntity(questionnaireWithChallengesModel.questionnaire)

        val createdQuestionnaire = questionnaireRepository.save(questionnaire)
        val questionnaireChallengeModel = QuestionnaireChallengeModel(
                createdQuestionnaire.questionnaireId!!,
                questionnaireWithChallengesModel.challenges
        )

        questionnaireChallengeServices.addChallengesByIdToQuestionnaire(questionnaireChallengeModel)

        return createdQuestionnaire
    }

    /**
     * Get questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of questionnaire object
     * @return questionnaire object
     */
    @Validated
    fun getQuestionnaireById(@Positive questionnaireId: Int) : Questionnaire {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)
        return questionnaire.get()
    }

    /**
     * Get all user questionnaires.
     *
     * @param userId unique identifier of user object
     * @return List of questionnaires objects
     */
    @Validated
    fun getUserAllQuestionnaires(@Positive userId: Int) : List<Questionnaire> {
        return questionnaireRepository.findAllByCreatorId(userId)
    }

    /**
     * Update a questionnaire.
     *
     * @param questionnaireModel information to be updated
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaireById(@Positive questionnaireId: Int, @Valid questionnaireModel: QuestionnaireModel): Questionnaire {
        val questionnaireFromDB = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaireFromDB, questionnaireId)

        //region data for update operation
        val updatedQuestionnaire = questionnaireFromDB.get()
        if(questionnaireModel.description != null)
            updatedQuestionnaire.description = questionnaireModel.description
        if(questionnaireModel.timer != null)
            updatedQuestionnaire.timer = questionnaireModel.timer
        //endregion

        return questionnaireRepository.save(updatedQuestionnaire)
    }

    /**
     * Delete a questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of object
     */
    @Validated
    fun deleteQuestionnaireById(@Positive questionnaireId: Int) {
        checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        questionnaireRepository.deleteById(questionnaireId)
    }

    fun getQuestionnaireInstanceByIdWithChallenge(questionnaireId: Int): QuestionnaireOutputModel? {
        val questionnaire = checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        val challenges = questionnaireChallengeServices.getAllQuestionnaireChallengeByQuestionnaireId(questionnaireId)

        return QuestionnaireOutputModel(
                questionnaireId,
                questionnaire.description,
                questionnaire.timer,
                challenges.map { QuestionnaireChallengeOutputModel(it.challenge, it.languageFilter) }
        )
    }

    /**
     * Auxiliary function that converts Questionnaire model to Questionnaire domain
     */
    private fun convertToEntity(input: QuestionnaireModel) = modelMapper.map(input, Questionnaire::class.java)

}