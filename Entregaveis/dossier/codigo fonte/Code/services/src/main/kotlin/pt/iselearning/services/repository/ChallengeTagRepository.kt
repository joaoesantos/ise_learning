package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.ChallengeTag
import java.util.*

/**
 * Interface that represents a repository for the ChallengeTag class
 */
@Repository
interface ChallengeTagRepository : CrudRepository<ChallengeTag, Int> {
    fun findAllByChallengeId(challengeId: Int): List<ChallengeTag>
    fun findAllByTag(tag: String): List<ChallengeTag>
    fun findByChallengeIdAndTagTagId(challengeId: Int, tagText: Int): Optional<ChallengeTag>
}