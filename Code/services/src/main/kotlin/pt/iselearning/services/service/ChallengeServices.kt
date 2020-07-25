package pt.iselearning.services.service

import org.springframework.stereotype.Service
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.ChallengeTagRepository
import java.util.*
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.repository.questionnaire.QuestionnaireChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository
import pt.iselearning.services.util.CustomValidators
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
                         @Pattern(regexp = CustomValidators.PRIVACY_REGEX_STRING) privacy: String?): List<Challenge> {
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy=="private"
                challengeRepository.findAllById(challengeIdList).filter { c -> c.isPrivate == isPrivate }
            } else {
                challengeRepository.findAllById(challengeIdList).toList()
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAll().toList()
            } else {
                val isPrivate = privacy=="private"
                challengeRepository.findAllByIsPrivate(isPrivate)
            }
        }
    }

    @Validated
    fun getChallengeById(@Positive challengeId : Int) : Challenge {
        val challenge = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challenge, challengeId)
        return challenge.get()
    }

    @Validated
    fun getUserChallenges(@Positive userId: Int, tags: String?,
                          @Pattern(regexp = CustomValidators.PRIVACY_REGEX_STRING) privacy: String?): List<Challenge>? {
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTagTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy=="private"
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }
                        .filter { c -> c.isPrivate == isPrivate }
            } else {
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }.toList()
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAllByCreatorId(userId)
            } else {
                val isPrivate = privacy=="private"
                challengeRepository.findAllByCreatorIdAndIsPrivate(userId, isPrivate)
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
    fun getAllChallengesByQuestionnaireId(@Positive questionnaireId: Int) : List<Challenge> {
        val questionnaire = questionnaireRepository.findById(questionnaireId)
        CustomValidators.checkIfQuestionnaireExists(questionnaire, questionnaireId)

        val challenges = questionnaireChallengeRepository.findAllChallengesByQuestionnaireId(questionnaireId)
        if (challenges.isEmpty()) {
            throw ServerException("Challenges not found.",
                    "There are no challenges for selected questionnaire $questionnaireId", ErrorCode.ITEM_NOT_FOUND)
        }
        return challenges
    }

    @Validated
    fun createChallenge(@Valid challenge: Challenge): Challenge? {
        return challengeRepository.save(challenge);
    }

    @Validated
    fun updateChallenge(@Valid challenge: Challenge): Challenge? {
        val challengeFromDb = challengeRepository.findById(challenge.challengeId!!)
        checkIfChallengeExists(challengeFromDb, challenge.challengeId!!)
        return challengeRepository.save(challenge)
    }

    @Validated
    fun deleteChallenge(@Positive challengeId: Int) {
        val challengeFromDb = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challengeFromDb, challengeId)
        challengeRepository.deleteById(challengeId)
    }

    fun checkIfChallengeExists(challenge: Optional<Challenge>, challengeId: Int) {
        if (challenge.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge does not exist.", // dar fix quando houver refactoring
                    "There is no challenge with id: $challengeId")
        }
    }
}