package pt.iselearning.services.repository.challenge

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.challenge.ChallengeAnswer
import java.util.*

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeAnswerRepository : CrudRepository<ChallengeAnswer, Int> {
    fun findAllByChallengeIdAndUserId(challengeId: Int, userId: Int): List<ChallengeAnswer>
    fun findAllByChallengeIdAndUserIdAndAnswerCodeLanguage(challengeId: Int, userId: Int, codeLanguage: String): List<ChallengeAnswer>
}
