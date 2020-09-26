package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.service.TagService
import pt.iselearning.services.util.VERSION

/**
 * Handler responsible to respond to requests regard Tag entity
 */
@RestController
@RequestMapping("/${VERSION}/tags", produces = ["application/json"])
class TagController (private val tagService: TagService) {
    /**
     * Method to get all existing tags.
     *
     * @return ResponseEntity<ChallengeTag> represents the returned challenge tag
     */
    @GetMapping(name = "getAllTags")
    fun getAllTags(): ResponseEntity<List<Tag>> {
        return ResponseEntity.ok().body(tagService.getAllTags())
    }
}


