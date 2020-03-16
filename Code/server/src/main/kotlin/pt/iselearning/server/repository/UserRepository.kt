package pt.iselearning.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pt.iselearning.server.domain.User

@Repository
interface UserRepository : JpaRepository<User, Int>