package pt.iselearning.services.service

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.ChallengeAnswer
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.ChallengeAnswerRepository
import pt.iselearning.services.repository.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive

@Validated
@Service
class ChallengeAnswerService (private val challengeAnswerRepository: ChallengeAnswerRepository,
                              private val challengeRepository: ChallengeRepository,
                              private val challengeService: ChallengeService,
                              private val userServices: UserServices,
                              private val userRepository: UserRepository) {
    @Validated
    fun createChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer): ChallengeAnswer? {
        return challengeAnswerRepository.save(challengeAnswer);
    }

    @Validated
    fun updateChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer): ChallengeAnswer? {
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswer.challengeId!!)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswer.challengeId!!)
        return challengeAnswerRepository.save(challengeAnswer)
    }

    @Validated
    fun deleteChallengeAnswer(@Positive challengeAnswerId: Int) {
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswerId)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswerId)
        challengeAnswerRepository.findById(challengeAnswerId)
    }

    @Validated
    fun getChallengeAnswerByUserId(@Positive challengeId: Int, @Positive userId: Int): ChallengeAnswer? {
        challengeService.checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        userServices.checkIfUserExists(userRepository.findById(userId), userId)
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

    fun checkIfChallengeAnswerExists(challengeAnswer: Optional<ChallengeAnswer>, challengeAnswerId: Int) { //migrate function to user services
        if (challengeAnswer.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge answer does not exist.", // dar fix quando houver refactoring
                    "There is no challenge answer with id: $challengeAnswerId")
        }
    }
}