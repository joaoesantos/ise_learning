package pt.iselearning.services.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.ChallengeAnswer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface ChallengeAnswerRepository : ReactiveCrudRepository<ChallengeAnswer, Int> {
    @Query(value = "SELECT * FROM challenge_answer WHERE challengeId = :challengeId AND userId = :userId")
    fun findByChallengeAndUser(@Param("challengeId")challengeId: Int, @Param("userId")userId: Int): Mono<ChallengeAnswer>
}
