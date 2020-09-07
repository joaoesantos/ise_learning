package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.repository.challenge.ChallengeAnswerRepository
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.util.checkIfChallengeAnswerExists
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfLoggedUserIsResourceOwner
import pt.iselearning.services.util.checkIfUserExists
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the challenge answer object
 */
@Validated
@Service
class ChallengeAnswerService (
        private val challengeAnswerRepository: ChallengeAnswerRepository,
        private val challengeRepository: ChallengeRepository,
        private val userRepository: UserRepository,
        private val challengeService: ChallengeService
) {

    /**
     * Create a challenge answer.
     *
     * @param challengeAnswer object information
     * @param loggedUser user that is calling the service
     * @return created challenge answer
     */
    @Validated
    fun createChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer? {
        challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswer.userId!!)
        return challengeAnswerRepository.save(challengeAnswer);
    }

    /**
     * Get challenge answer by its unique identifier.
     *
     * @param challengeId identifier of challenge object
     * @param userId identifier of user answer object
     * @param loggedUser user that is calling the service
     * @return challenge answer object
     */
    @Validated
    fun getChallengeAnswerByChallengeIdAndUserId(@Positive challengeId: Int, @Positive userId: Int, loggedUser: User): ChallengeAnswer? {
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        checkIfUserExists(userRepository.findById(userId), userId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, userId)
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

    /**
     * Update a challenge answer.
     *
     * @param challengeAnswer information to be updated
     * @param loggedUser user that is calling the service
     * @return updated challenge answer
     */
    @Validated
    fun updateChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer? {
        challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswer.userId!!)
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswer.challengeId!!)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswer.challengeId!!)
        return challengeAnswerRepository.save(challengeAnswer)
    }

    /**
     * Delete a challenge answer by its unique identifier.
     *
     * @param challengeAnswerId identifier of object
     * @param loggedUser user that is calling the service
     */
    @Validated
    fun deleteChallengeAnswer(@Positive challengeAnswerId: Int, loggedUser: User) {
        val challengeAnswerFromDb = challengeAnswerRepository.findById(challengeAnswerId)
        checkIfChallengeAnswerExists(challengeAnswerFromDb, challengeAnswerId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswerId)
        challengeAnswerRepository.findById(challengeAnswerId)
    }

}