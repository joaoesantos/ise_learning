package pt.iselearning.services.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Challenge

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeRepository : CrudRepository<Challenge, Int> {
    fun findAllByCreatorId(userId: Int): List<Challenge>
    fun findAllByIsPrivate(isPrivate : Boolean): List<Challenge>
    fun findAllByCreatorIdAndIsPrivate(userId: Int, private: Boolean): List<Challenge>
}
