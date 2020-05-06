package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.ChallengeTag

/**
 * Interface that represents a repository for the ChallengeTag class
 */
@Repository
interface ChallengeTagRepository : CrudRepository<ChallengeTag, Int> {
    fun findAllByChallengeId(challengeId: Int): List<ChallengeTag>
    fun findAllByTag(tagList: String): List<ChallengeTag>
}
