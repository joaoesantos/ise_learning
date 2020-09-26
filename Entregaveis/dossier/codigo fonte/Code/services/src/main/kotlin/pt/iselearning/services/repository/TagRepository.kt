package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.Tag
import java.util.*

/**
 * Interface that represents a repository for the ChallengeTag class
 */
@Repository
interface TagRepository : CrudRepository<Tag, Int> {
    fun findByTag(tag: String) : Optional<Tag>
}
