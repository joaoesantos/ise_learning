package pt.iselearning.services.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.CodeLanguage

/**
 * Interface that represents a repository for the CodeLanguage class
 */
@Repository
interface CodeLanguageRepository : CrudRepository<CodeLanguage, Int> {
}
