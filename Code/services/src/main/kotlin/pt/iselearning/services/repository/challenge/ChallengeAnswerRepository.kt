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
    fun findByChallengeIdAndUserId(challengeId: Int, userId: Int): Optional<ChallengeAnswer>
}
