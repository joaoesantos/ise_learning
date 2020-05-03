package pt.iselearning.services.service

import org.springframework.stereotype.Service
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.ChallengeTagRepository
import pt.iselearning.services.repository.TagRepository
import java.lang.String
import java.util.*

@Service
class ChallengeService (private val challengeRepository: ChallengeRepository,
                        private val challengeTagRepository: ChallengeTagRepository) {
    fun getAllChallenges(tags: String?, privacy: String?): List<Challenge> {
        //acrescentar validador para o privacy, pode ser "private" "public" ou null, mas nao pode ser mais nada
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy==String("private")
                challengeRepository.findAllById(challengeIdList).filter { c -> c.isPrivate == isPrivate }
            } else {
                challengeRepository.findAllById(challengeIdList).toList()
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAll().toList()
            } else {
                val isPrivate = privacy==String("private")
                challengeRepository.findAllByIsPrivate(isPrivate)
            }
        }
    }

    fun getChallengeById(challengeId : Int) : Challenge {
        val challenge = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challenge, challengeId)
        return challenge.get()
    }

    fun getChallengeByUserId(userId: Int, tags: String?, privacy: String?): List<Challenge>? {
        //acrescentar validador para o privacy, pode ser "private" "public" ou null, mas nao pode ser mais nada
        if(tags != null) {
            val challengeIdList = tags!!.split(",").map { tag -> challengeTagRepository.findAllByTag(tag) }
                    .flatMap { challengeTags -> challengeTags.map { challengeTag -> challengeTag.challengeId!! } }
                    .distinct().asIterable()
            return if(privacy != null) {
                val isPrivate = privacy==String("private")
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }
                        .filter { c -> c.isPrivate == isPrivate }
            } else {
                challengeRepository.findAllById(challengeIdList).filter{c -> c.creatorId == userId }.toList()
            }
        } else {
            return if(privacy == null) {
                challengeRepository.findAllByCreatorId(userId)
            } else {
                val isPrivate = privacy==String("private")
                challengeRepository.findAllByCreatorIdAndIsPrivate(userId, isPrivate)
            }
        }
    }

    fun createChallenge(challenge: Challenge): Challenge? {
        return challengeRepository.save(challenge);
    }

    fun updateChallenge(challenge: Challenge): Challenge? {
        val challengeFromDb = challengeRepository.findById(challenge.challengeId!!)
        checkIfChallengeExists(challengeFromDb, challenge.challengeId!!)
        return challengeRepository.save(challenge)
    }

    fun deleteChallenge(challengeId: Int) {
        val challengeFromDb = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challengeFromDb, challengeId)
        challengeRepository.findById(challengeId)
    }

    fun checkIfChallengeExists(challenge: Optional<Challenge>, challengeId: Int) {
        if (challenge.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge does not exist.", // dar fix quando houver refactoring
                    "There is no challenge with id: $challengeId")
        }
    }
}