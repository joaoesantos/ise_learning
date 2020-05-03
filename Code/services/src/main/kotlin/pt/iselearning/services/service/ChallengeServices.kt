package pt.iselearning.services.service

import org.springframework.stereotype.Service
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeRepository
import java.lang.String
import java.util.*

@Service
class ChallengeService (private val challengeRepository: ChallengeRepository) {
    fun getAllChallenges() : List<Challenge> {
        return challengeRepository.findAll().toList()
    }

    fun getChallengeById(challengeId : Int) : Challenge {
        val challenge = challengeRepository.findById(challengeId)
        checkIfChallengeExists(challenge, challengeId)
        return challenge.get()
    }

    fun getChallengeByUserId(userId: Int, tags: String?, privacy: String?): List<Challenge>? {
        //incluir parte das tags e privacy
        return challengeRepository.findAllByCreatorId(userId)
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