package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.ChallengeAnswer
import java.util.*

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeAnswerRepository : CrudRepository<ChallengeAnswer, Int> {
    fun findByChallengeIdAndUserId(challengeId: Int, userId: Int): Optional<ChallengeAnswer>
}
