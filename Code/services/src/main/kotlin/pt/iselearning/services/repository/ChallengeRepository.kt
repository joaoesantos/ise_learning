package pt.iselearning.services.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Challenge
import reactor.core.publisher.Flux

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeRepository : ReactiveCrudRepository<Challenge, Int> {
    @Query(value = "SELECT * FROM challenge WHERE creatorId = :userId")
    fun findAllByUser(@Param("userId")userId: Int): Flux<Challenge>
}
