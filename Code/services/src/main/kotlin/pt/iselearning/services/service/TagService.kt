package pt.iselearning.services.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.repository.TagRepository

@Validated
@Service
class TagService (private val tagRepository : TagRepository) {

    @Transactional
    fun getAllTags(): List<Tag> {
        return tagRepository.findAll().toList()
    }
}
