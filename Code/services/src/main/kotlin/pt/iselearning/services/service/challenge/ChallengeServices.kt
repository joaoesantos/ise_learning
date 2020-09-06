package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.challenge.ChallengeTagRepository
import java.util.*
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.models.ChallengePrivacyEnum
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.PRIVACY_REGEX_STRING
import pt.iselearning.services.util.checkIfQuestionnaireExists
import javax.validation.Valid
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

@Validated
@Service
class ChallengeService (
        private val challengeRepository: ChallengeRepository,
        private val challengeTagRepository: ChallengeTagRepository,
        private val questionnaireRepository: QuestionnaireRepository,
        private val questionnaireChallengeRepository: QuestionnaireChallengeRepository
) {
    @Validated
    fun getAllChallenges(tags: String?,
                         @Pattern(regexp = PRIVACY_REGEX_STRING) privacy: String?,
                         loggedUser: User?): List<Challenge> {
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
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

    @Validated
    fun getChallengeById(@Positive challengeId : Int, loggedUser: User?) : Challenge {
        val challengeOpt = challengeRepository.findById(challengeId)
        val challenge = checkIfChallengeExists(challengeOpt, challengeId)
        if(challenge.isPrivate!! && (loggedUser == null || challenge.creatorId != loggedUser!!.userId)) {
            throw ServerException("Cannot retrieve private challenge from other users.",
                    "Cannot retrieve private challenge from other users. Private challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        return challenge
    }

    @Validated
    fun getUserChallenges(@Positive userId: Int, tags: String?,
                          @Pattern(regexp = PRIVACY_REGEX_STRING) privacy: String?,
                          loggedUser: User?): List<Challenge>? {
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
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
            throw ServerException("Challenges not found.",
                    "There are no challenges for selected questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
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

    @Validated
    fun createChallenge(@Valid challenge: Challenge, loggedUser: User): Challenge? {
        if(challenge.creatorId != loggedUser.userId) {
            throw ServerException("Cannot create challenge for other users.",
                    "Cannot create challenge for other users. Challenge must belong to logged user.",
                    ErrorCode.FORBIDDEN)
        }
        challenge.creatorId = loggedUser.userId
        return challengeRepository.save(challenge);
    }

    @Validated
    fun updateChallenge(@Valid challenge: Challenge, loggedUser: User): Challenge? {
        val challengeFromDb = challengeRepository.findById(challenge.challengeId!!)
        checkIfChallengeExists(challengeFromDb, challenge.challengeId!!)
        if(challenge.creatorId != loggedUser!!.userId) {
            throw ServerException("Cannot update challenge from other users.",
                    "Cannot update challenge from other users. Challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        return challengeRepository.save(challenge)
    }

    @Validated
    fun deleteChallenge(@Positive challengeId: Int, loggedUser: User) {
        val challengeFromDb = challengeRepository.findById(challengeId)
        val challenge = checkIfChallengeExists(challengeFromDb, challengeId)
        if(challenge.creatorId != loggedUser!!.userId) {
            throw ServerException("Cannot delete challenge from other users.",
                    "Cannot delete challenge from other users. Challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        challengeRepository.deleteById(challengeId)
    }

    fun checkIfChallengeExists(challenge: Optional<Challenge>, challengeId: Int) : Challenge{
        if (challenge.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge does not exist.",
                    "There is no challenge with id: $challengeId")
        }
        return challenge.get();
    }
}