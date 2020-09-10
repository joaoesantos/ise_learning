package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.challenge.ChallengeAnswerRepository
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.service.ExecutionEnvironmentsService
import pt.iselearning.services.util.*
import java.rmi.ServerException
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
        private val challengeService: ChallengeService,
        private val executionEnvironmentsService: ExecutionEnvironmentsService
) {

    /**
     * Create a challenge answer.
     *
     * @param challengeAnswer object information
     * @param loggedUser user that is calling the service
     * @return created challenge answer
     */
    @Validated
    fun createChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer {

        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswer.userId!!)

        val challenge = challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)

        val challengeUnitTests = challenge.solutions?.first { solution ->
            solution.codeLanguage == challengeAnswer.answer?.codeLanguage
        }

        // evaluates if answer is corrected based on challenge unit tests
        val executableResult = executionEnvironmentsService.execute(
                ExecutableModel(
                        challengeAnswer.answer?.codeLanguage!!,
                        challengeAnswer.answer?.answerCode!!,
                        challengeUnitTests?.unitTests!!,
                        true
                )
        )
        if(!executableResult.wasError) {
            challengeAnswer.answer!!.isCorrect = true
        }

        return challengeAnswerRepository.save(challengeAnswer);
    }

    /**
     * Get user challenge answer by its unique identifier.
     *
     * @param challengeId identifier of challenge object
     * @param userId identifier of user answer object
     * @return challenge answer object
     */
    @Validated
    fun getChallengeAnswerByUserId(@Positive challengeId: Int, @Positive userId: Int): ChallengeAnswer {
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        checkIfUserExists(userRepository.findById(userId), userId)
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
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
    fun getChallengeAnswerByChallengeIdAndUserId(@Positive challengeId: Int, @Positive userId: Int, loggedUser: User): ChallengeAnswer {
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, userId)
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        checkIfUserExists(userRepository.findById(userId), userId)
        if(userId != loggedUser.userId) {
            throw ServiceException("Cannot get other user's answers.",
                    "Cannot get other user's answers. Challenge answer user different than logged user.",
                    "/iselearning/challengeAnswer/notResourceOwner",
                    ErrorCode.FORBIDDEN)
        }
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
    fun updateChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer {
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswer.userId!!)
        val challenge = challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)
        val challengeAnswerFromDb = challengeAnswerRepository.findById(challengeAnswer.challengeId!!)
        checkIfChallengeAnswerExists(challengeAnswerFromDb, challengeAnswer.challengeId!!)

        val challengeUnitTests = challenge.solutions?.first { solution ->
            solution.codeLanguage == challengeAnswer.answer?.codeLanguage
        }

        // evaluates if answer is corrected based on challenge unit tests
        val executableResult = executionEnvironmentsService.execute(
                ExecutableModel(
                        challengeAnswer.answer?.codeLanguage!!,
                        challengeAnswer.answer?.answerCode!!,
                        challengeUnitTests?.unitTests!!,
                        true
                )
        )
        if(!executableResult.wasError) {
            challengeAnswer.answer!!.isCorrect = true
        }

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
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswerId)
        val challengeAnswer = challengeAnswerRepository.findById(challengeAnswerId)
        checkIfChallengeAnswerExists(challengeAnswer, challengeAnswerId)
        challengeAnswerRepository.findById(challengeAnswerId)
    }

}