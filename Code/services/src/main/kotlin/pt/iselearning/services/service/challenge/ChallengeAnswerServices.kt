package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.repository.challenge.ChallengeAnswerRepository
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.util.checkIfChallengeAnswerExists
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfUserExists
import javax.validation.Valid
import javax.validation.constraints.Positive

@Validated
@Service
class ChallengeAnswerService (
        private val challengeAnswerRepository: ChallengeAnswerRepository,
        private val challengeRepository: ChallengeRepository,
        private val userRepository: UserRepository
) {

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
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        checkIfUserExists(userRepository.findById(userId), userId)
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

}