package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Challenge

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeRepository : CrudRepository<Challenge, Int> {
    fun findAllByCreatorId(userId: Int): List<Challenge>
}
