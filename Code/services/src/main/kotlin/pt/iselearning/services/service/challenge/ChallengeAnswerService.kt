package pt.iselearning.services.service.challenge

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.models.challenge.ChallengeAnswerModel
import pt.iselearning.services.repository.challenge.ChallengeAnswerRepository
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.service.ExecutionEnvironmentsService
import pt.iselearning.services.util.*
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
        private val executionEnvironmentsService: ExecutionEnvironmentsService,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a challenge answer.
     *
     * @param challengeAnswerModel object information
     * @param loggedUser user that is calling the service
     * @return created challenge answer
     */
    @Validated
    fun createChallengeAnswer(@Valid challengeAnswerModel: ChallengeAnswerModel, loggedUser: User): ChallengeAnswer {

        val challenge = challengeService.getChallengeById(challengeAnswerModel.challengeId, loggedUser)
        val createdChallengeAnswer = convertToEntity(challengeAnswerModel)
        val challengeSolution = checkIfChallengeSolutionExistsForCodeLanguage(challenge, challengeAnswerModel.answer.codeLanguage)

        // evaluates if answer is corrected based on challenge unit tests
        val executableResult = executionEnvironmentsService.execute(
                ExecutableModel(
                        challengeAnswerModel.answer.codeLanguage,
                        challengeAnswerModel.answer.answerCode,
                        challengeSolution?.unitTests!!,
                        true
                )
        )
        //region data manipulation for create operation
        createdChallengeAnswer.userId = loggedUser.userId
        createdChallengeAnswer.answer?.isCorrect = !executableResult.wasError
        //endregion

        return challengeAnswerRepository.save(createdChallengeAnswer);
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
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

    /**
     * Update a challenge answer.
     *
     * @param challengeAnswerId identifier of challenge answer object
     * @param challengeAnswerModel information to be updated
     * @param loggedUser user that is calling the service
     * @return updated challenge answer
     */
    @Validated
    fun updateChallengeAnswer(@Positive challengeAnswerId: Int, @Valid challengeAnswerModel: ChallengeAnswerModel, loggedUser: User): ChallengeAnswer {
        val challengeAnswerFromDb = checkIfChallengeAnswerExists(challengeAnswerRepository, challengeAnswerId)
        checkIfLoggedUserIsResourceOwner(loggedUser.userId!!, challengeAnswerFromDb.userId!!)
        val updatedChallengeAnswer = convertToEntity(challengeAnswerModel)

        val challenge = challengeService.getChallengeById(challengeAnswerFromDb.challengeId!!, loggedUser)

        val challengeSolution = checkIfChallengeSolutionExistsForCodeLanguage(challenge, challengeAnswerModel.answer.codeLanguage)

        // evaluates if answer is corrected based on challenge unit tests
        val executableResult = executionEnvironmentsService.execute(
                ExecutableModel(
                        challengeAnswerModel.answer.codeLanguage,
                        challengeAnswerModel.answer.answerCode,
                        challengeSolution?.unitTests!!,
                        true
                )
        )

        //region data manipulation for update operation
        updatedChallengeAnswer.userId = loggedUser.userId
        updatedChallengeAnswer.challengeAnswerId = challengeAnswerFromDb.challengeAnswerId
        updatedChallengeAnswer.answer?.answerId = challengeAnswerFromDb.answer?.answerId
        updatedChallengeAnswer.answer?.isCorrect = !executableResult.wasError
        //endregion

        return challengeAnswerRepository.save(updatedChallengeAnswer)
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

    /**
     * Auxiliary function that converts ChallengeAnswer model to ChallengeAnswer domain
     */
    private fun convertToEntity(input: ChallengeAnswerModel) = modelMapper.map(input, ChallengeAnswer::class.java)

}