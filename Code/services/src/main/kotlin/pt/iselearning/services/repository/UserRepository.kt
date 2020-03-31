package pt.iselearning.services.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.User

/**
 * Interface that represents a repository for the User class
 */
@Repository
interface UserRepository : CrudRepository<User, Int>
