package pt.iselearning.services.service.challenge

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.challenge.ChallengeTagRepository
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.models.ChallengePrivacyEnum
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.models.challenge.ChallengeModel
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.service.ExecutionEnvironmentsService
import pt.iselearning.services.util.PRIVACY_REGEX_STRING
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfQuestionnaireExists
import pt.iselearning.services.util.checkIfUnitTestsPassed
import javax.validation.Valid
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the challenge object
 */
@Validated
@Service
class ChallengeService (
        private val challengeRepository: ChallengeRepository,
        private val challengeTagRepository: ChallengeTagRepository,
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository,
        private val executionEnvironmentsService: ExecutionEnvironmentsService,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a challenge.
     *
     * @param challengeModel object information
     * @param loggedUser user that is calling the service
     * @return created challenge
     */
    @Validated
    fun createChallenge(@Valid challengeModel: ChallengeModel, loggedUser: User): Challenge {
        challengeModel.solutions.forEach {
            val executableResult = executionEnvironmentsService.execute(
                    ExecutableModel(it.codeLanguage,it.challengeCode,it.unitTests,true)
            )
            checkIfUnitTestsPassed(executableResult)

        }
        val challenge = convertToEntity(challengeModel)
        challenge.creatorId = loggedUser.userId
        return challengeRepository.save(challenge)
    }

    /**
     * Get all challenges.
     *
     * @param tags that can be used as filter
     * @param loggedUser user that is calling the service
     * @return List of challenge objects
     */
    @Validated
    fun getAllChallenges(
            tags: String?,
            @Pattern(regexp = PRIVACY_REGEX_STRING) privacy: String?,
            loggedUser: User?
    ): List<Challenge> {
        if(tags != null) {
            val challengeIdList = tags.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy == ChallengePrivacyEnum.PRIVATE.value
                challengeRepository.findAllById(challengeIdList).filter { c -> c.isPrivate == isPrivate }
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            } else {
                challengeRepository.findAllById(challengeIdList).toList()
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAll().toList()
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            } else {
                val isPrivate = privacy == ChallengePrivacyEnum.PRIVATE.value
                challengeRepository.findAllByIsPrivate(isPrivate)
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            }
        }
    }

    /**
     * Get challenge by its unique identifier.
     *
     * @param challengeId identifier of challenge object
     * @param loggedUser user that is calling the service
     * @return challenge object
     */
    @Validated
    fun getChallengeById(@Positive challengeId : Int, loggedUser: User?): Challenge {
        val challenge = checkIfChallengeExists(challengeRepository, challengeId)
        if(challenge.isPrivate!! && (loggedUser == null || challenge.creatorId != loggedUser!!.userId)) {
            throw ServiceException(
                    "Cannot retrieve private challenge from other users.",
                    "Cannot retrieve private challenge from other users. Private challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        return challenge
    }

    /**
     * Get all user challenges.
     *
     * @param userId unique identifier of user object
     * @param loggedUser user that is calling the service
     * @return List of challenge objects
     */
    @Validated
    fun getUserChallenges(
            @Positive userId: Int, tags: String?,
            @Pattern(regexp = PRIVACY_REGEX_STRING) privacy: String?,
            loggedUser: User?
    ): List<Challenge> {
        if(tags != null) {
            val challengeIdList = tags.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy=="private"
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }
                        .filter { c -> c.isPrivate == isPrivate }
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            } else {
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }.toList()
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAllByCreatorId(userId)
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            } else {
                val isPrivate = privacy=="private"
                challengeRepository.findAllByCreatorIdAndIsPrivate(userId, isPrivate)
                        .filter { c -> !(c.isPrivate!! && (loggedUser == null || c.creatorId != loggedUser.userId))}
            }
        }
    }

    /**
     * Get all challenges from an questionnaire by its unique identifier.
     *
     * @param questionnaireId identifier of questionnaire object
     * @return List of challenge objects
     */
    @Validated
    fun getAllChallengesByQuestionnaireId(@Positive questionnaireId: Int): List<Challenge> {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        checkIfQuestionnaireExists(questionnaire, questionnaireId)

        val challenges = questionnaireChallengeRepository.findAllByQuestionnaireQuestionnaireId(questionnaireId)
                .filter { qc -> qc.challenge != null }
                .map { questionnaireChallenge -> questionnaireChallenge.challenge!! }
        if (challenges.isEmpty()) {
            throw ServiceException(
                    "Challenges not found.",
                    "There are no challenges for selected questionnaire $questionnaireId",
                    "/iselearning/challenge/nonexistentQuestionnaireChallenges",
                    ErrorCode.ITEM_NOT_FOUND
            )
        }
        return challenges
    }

    /**
     * Get one random challenge.
     *
     * @return challenge object
     */
    fun getRandomChallenge(): Challenge {
        val challengeId = challengeRepository.findAllPublicChallengeIds().random()
        val challenge = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challenge, challengeId)
        return challenge.get()
    }

    /**
     * Update a challenge.
     *
     * @param challengeModel information to be updated
     * @param loggedUser user that is calling the service
     * @return updated challenge
     */
    @Validated
    fun updateChallenge(@Positive challengeId: Int, @Valid challengeModel: ChallengeModel, loggedUser: User): Challenge {
        val challengeFromDb = checkIfChallengeExists(challengeRepository, challengeId)
        if(challengeFromDb.creatorId != loggedUser.userId) {
            throw ServiceException(
                    "Cannot update challenge from other users.",
                    "Cannot update challenge from other users. Challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        challengeModel.solutions.forEach {
            val executableResult = executionEnvironmentsService.execute(
                    ExecutableModel(it.codeLanguage,it.challengeCode,it.unitTests,true)
            )
            checkIfUnitTestsPassed(executableResult)

        }

        //region manipulation data for update operation
        val updatedChallenge = convertToEntity(challengeModel)
        updatedChallenge.challengeId = challengeId
        updatedChallenge.creatorId = loggedUser.userId
        //endregion

        return challengeRepository.save(updatedChallenge)
    }

    /**
     * Delete a challenge by its unique identifier.
     *
     * @param challengeId identifier of object
     * @param loggedUser user that is calling the service
     */
    @Validated
    fun deleteChallenge(@Positive challengeId: Int, loggedUser: User) {
        val challenge = checkIfChallengeExists(challengeRepository, challengeId)
        if(challenge.creatorId != loggedUser.userId) {
            throw ServiceException(
                    "Cannot delete challenge from other users.",
                    "Cannot delete challenge from other users. Challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        challengeRepository.deleteById(challengeId)
    }

    /**
     * Auxiliary function that converts Challenge model to Challenge domain
     */
    private fun convertToEntity(input: ChallengeModel) = modelMapper.map(input, Challenge::class.java)

}