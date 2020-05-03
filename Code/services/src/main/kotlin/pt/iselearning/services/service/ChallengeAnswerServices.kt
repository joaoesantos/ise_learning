package pt.iselearning.services.service

import org.springframework.stereotype.Service
import pt.iselearning.services.domain.ChallengeAnswer
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeAnswerRepository
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import java.util.*

@Service
class ChallengeAnswerService (private val challengeAnswerRepository: ChallengeAnswerRepository,
                              private val challengeRepository: ChallengeRepository,
                              private val challengeService: ChallengeService,
                              private val userRepository: UserRepository) {
    fun createChallengeAnswer(challengeAnswer: ChallengeAnswer): ChallengeAnswer? {
        return challengeAnswerRepository.save(challengeAnswer);
    }

    fun updateChallengeAnswer(challengeAnswer: ChallengeAnswer): ChallengeAnswer? {
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswer.challengeId!!)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswer.challengeId!!)
        return challengeAnswerRepository.save(challengeAnswer)
    }

    fun deleteChallengeAnswer(challengeAnswerId: Int) {
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswerId)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswerId)
        challengeAnswerRepository.findById(challengeAnswerId)
    }

    fun getChallengeAnswerByUserId(challengeId: Int, userId: Int): ChallengeAnswer? {
        challengeService.checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        checkIfUserExists(userRepository.findById(userId), userId)
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

    fun checkIfChallengeAnswerExists(challengeAnswer: Optional<ChallengeAnswer>, challengeAnswerId: Int) { //migrate function to user services
        if (challengeAnswer.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge answer does not exist.", // dar fix quando houver refactoring
                    "There is no challenge answer with id: $challengeAnswerId")
        }
    }

    fun checkIfUserExists(user: Optional<User>, userId: Int) { //migrate function to user services
        if (user.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This user does not exist.", // dar fix quando houver refactoring
                    "There is no user with id: $userId")
        }
    }
}