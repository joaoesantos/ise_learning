package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.service.TagService

/**
 * Handler responsible to respond to requests regard Tag entity
 */
@RestController
@RequestMapping("/v0/tags")
class TagController (private val tagService: TagService) {
    /**
     * Method to get all existing tags.
     */
    @GetMapping
    fun getAllTags(): ResponseEntity<List<Tag>> {
        return ResponseEntity.ok().body(tagService.getAllTags())
    }
}


