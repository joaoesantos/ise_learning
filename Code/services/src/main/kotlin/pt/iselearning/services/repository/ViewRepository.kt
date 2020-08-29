package pt.iselearning.services.repository

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.util.*

@NoRepositoryBean
interface ViewRepository<T, ID> : Repository<T, ID> {
    fun findById(id: ID): Optional<T>
    fun existsById(id: ID): Boolean
    fun findAll(): Iterable<T>
    fun findAllById(ids: Iterable<ID>?): Iterable<T>
}