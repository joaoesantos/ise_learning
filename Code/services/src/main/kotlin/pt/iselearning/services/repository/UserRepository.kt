package pt.iselearning.services.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pt.iselearning.services.domain.User

@Repository
interface UserRepository : ReactiveCrudRepository<User, Int> {

}