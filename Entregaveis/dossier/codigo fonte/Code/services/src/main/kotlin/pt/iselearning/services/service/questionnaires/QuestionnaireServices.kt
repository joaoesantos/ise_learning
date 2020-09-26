package pt.iselearning.services.service.questionnaires

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.questionnaires.Questionnaire
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.models.questionnaire.QuestionnaireModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireOutputModel
import pt.iselearning.services.models.questionnaire.QuestionnaireWithChallengesModel
import pt.iselearning.services.models.questionnaire.output.QuestionnaireChallengeOutputModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.checkIfLoggedUserIsResourceOwner
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
     * @param loggedUser user that is calling the service
     * @return created questionnaire
     */
    @Validated
    @Transactional
    fun createQuestionnaireWithChallenges(@Valid questionnaireWithChallengesModel: QuestionnaireWithChallengesModel, loggedUser: User): Questionnaire? {
        val questionnaire = convertToEntity(questionnaireWithChallengesModel.questionnaire)
        questionnaire.creatorId = loggedUser.userId
        val createdQuestionnaire = questionnaireRepository.save(questionnaire)
        
        val questionnaireChallengeModel = QuestionnaireChallengeModel(
                createdQuestionnaire.questionnaireId!!,
                questionnaireWithChallengesModel.challenges
        )

        questionnaireChallengeServices.addChallengesToQuestionnaire(questionnaireChallengeModel)

        return createdQuestionnaire
    }

    /**
     * Get questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of questionnaire object
     * @return questionnaire object
     */
    @Validated
    fun getQuestionnaireById(@Positive questionnaireId: Int): Questionnaire {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)
        return questionnaire.get()
    }

    /**
     * Get all user questionnaires.
     *
     * @param loggedUser user that is calling the service
     * @return List of questionnaires objects
     */
    @Validated
    fun getUserAllQuestionnaires(@Valid loggedUser: User): List<Questionnaire> {
        return questionnaireRepository.findAllByCreatorId(loggedUser.userId!!)
    }

    /**
     * Get questionnaire with its challenges by its unique identifier
     *
     * @param questionnaireId unique identifier of user object
     * @return List of QuestionnaireOutputModel objects
     */
    @Validated
    fun getQuestionnaireWithChallengeById(@Positive questionnaireId: Int): QuestionnaireOutputModel? {
        val questionnaire = checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        val challenges = questionnaireChallengeServices.getAllQuestionnaireChallengeByQuestionnaireId(questionnaireId)

        return QuestionnaireOutputModel(
                questionnaireId,
                questionnaire.description,
                questionnaire.timer,
                questionnaire.creatorId,
                challenges.map { QuestionnaireChallengeOutputModel(it.challenge, it.languageFilter) }
        )
    }

    /**
     * Update a questionnaire and its challenges.
     *
     * @param questionnaireWithChallengesModel object information
     * @param loggedUser user that is calling the service
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaireWithChallengesById(@Positive questionnaireId: Int, @Valid questionnaireWithChallengesModel: QuestionnaireWithChallengesModel, @Valid loggedUser: User): Questionnaire {
        var updatedQuestionnaire = checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, updatedQuestionnaire.creatorId!!)

        //region data for update operation
        if(questionnaireWithChallengesModel.questionnaire.description != null)
            updatedQuestionnaire.description = questionnaireWithChallengesModel.questionnaire.description
        if(questionnaireWithChallengesModel.questionnaire.timer != null)
            updatedQuestionnaire.timer = questionnaireWithChallengesModel.questionnaire.timer
        //endregion

        updatedQuestionnaire = questionnaireRepository.save(updatedQuestionnaire)

        val questionnaireChallengeModel = QuestionnaireChallengeModel(
                updatedQuestionnaire.questionnaireId!!,
                questionnaireWithChallengesModel.challenges
        )
        questionnaireChallengeServices.updateChallengesOnQuestionnaire(questionnaireChallengeModel)

        return updatedQuestionnaire
    }

    /**
     * Update a questionnaire.
     *
     * @param questionnaireModel information to be updated
     * @param loggedUser user that is calling the service
     * @return updated questionnaire
     */
    @Validated
    fun updateQuestionnaireById(@Positive questionnaireId: Int, @Valid questionnaireModel: QuestionnaireModel, @Valid loggedUser: User): Questionnaire {
        val updatedQuestionnaire = checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, updatedQuestionnaire.creatorId!!)

        //region data for update operation
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
     * @param loggedUser user that is calling the service
     */
    @Validated
    fun deleteQuestionnaireById(@Positive questionnaireId: Int, @Valid loggedUser: User) {
        val questionnaire = checkIfQuestionnaireExists(questionnaireRepository, questionnaireId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, questionnaire.creatorId!!)

        questionnaireRepository.deleteById(questionnaireId)
    }

    /**
     * Auxiliary function that converts Questionnaire model to Questionnaire domain
     */
    private fun convertToEntity(input: QuestionnaireModel) = modelMapper.map(input, Questionnaire::class.java)

}