package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.challenge.ChallengeAnswerRepository
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.service.UserService
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive

@Validated
@Service
class ChallengeAnswerService (private val challengeAnswerRepository: ChallengeAnswerRepository,
                              private val challengeRepository: ChallengeRepository,
                              private val challengeService: ChallengeService,
                              private val userServices: UserService,
                              private val userRepository: UserRepository) {
    @Validated
    fun createChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer? {
        challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)
        if(challengeAnswer.userId != loggedUser.userId) {
            throw ServerException("Cannot create an answer for other users",
                    "Cannot create an answer for other users. Challenge answer user different than logged user.",
                    ErrorCode.FORBIDDEN)
        }
        return challengeAnswerRepository.save(challengeAnswer);
    }

    @Validated
    fun updateChallengeAnswer(@Valid challengeAnswer: ChallengeAnswer, loggedUser: User): ChallengeAnswer? {
        challengeService.getChallengeById(challengeAnswer.challengeId!!, loggedUser)
        if(challengeAnswer.userId != loggedUser.userId) {
            throw ServerException("Cannot update other user's answers.",
                    "Cannot update other user's answers. Challenge answer user different than logged user.",
                    ErrorCode.FORBIDDEN)
        }
        val challengeFromDb = challengeAnswerRepository.findById(challengeAnswer.challengeId!!)
        checkIfChallengeAnswerExists(challengeFromDb, challengeAnswer.challengeId!!)
        return challengeAnswerRepository.save(challengeAnswer)
    }

    @Validated
    fun deleteChallengeAnswer(@Positive challengeAnswerId: Int, loggedUser: User) {
        val challengeAnswerFromDb = challengeAnswerRepository.findById(challengeAnswerId)
        checkIfChallengeAnswerExists(challengeAnswerFromDb, challengeAnswerId)
        if(challengeAnswerFromDb.get().userId != loggedUser.userId) {
            throw ServerException("Cannot delete other user's answers.",
                    "Cannot delete other user's answers. Challenge answer user different than logged user.",
                    ErrorCode.FORBIDDEN)
        }
        challengeAnswerRepository.findById(challengeAnswerId)
    }

    @Validated
    fun getChallengeAnswerByUserId(@Positive challengeId: Int, @Positive userId: Int, loggedUser: User): ChallengeAnswer? {
        challengeService.checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        userServices.checkIfUserExists(userRepository.findById(userId), userId)
        if(userId != loggedUser.userId) {
            throw ServerException("Cannot get other user's answers.",
                    "Cannot get other user's answers. Challenge answer user different than logged user.",
                    ErrorCode.FORBIDDEN)
        }
        return challengeAnswerRepository.findByChallengeIdAndUserId(challengeId, userId).get()
    }

    fun checkIfChallengeAnswerExists(challengeAnswer: Optional<ChallengeAnswer>, challengeAnswerId: Int) { //migrate function to user services
        if (challengeAnswer.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge answer does not exist.", // dar fix quando houver refactoring
                    "There is no challenge answer with id: $challengeAnswerId")
        }
    }
}