package pt.iselearning.services.controller.challenge

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.iselearning.services.domain.challenge.ChallengeTag
import pt.iselearning.services.models.tag.ChallengeTagModel
import pt.iselearning.services.service.challenge.ChallengeTagService
import pt.iselearning.services.util.Constants.Companion.CHALLENGE_TAG_PATTERN

/**
 * Handler responsible to respond to requests regard ChallengeTag entity
 */
@RestController
@RequestMapping(CHALLENGE_TAG_PATTERN)
class ChallengeTagController (private val challengeTagService: ChallengeTagService) {
    /**
     * Method to create a challenge tag.
     *
     * @param challengeTagModel represents the challenge tag to be created
     * @param ucb helps build URLs
     * @return ResponseEntity<ChallengeTag> represents the created challenge tag
     */
    @PostMapping(name = "createChallengeTag")
    fun createChallengeTag(
            @PathVariable challengeId: Int,
            @RequestBody challengeTagModel: ChallengeTagModel,
            ucb : UriComponentsBuilder
    ): ResponseEntity<ChallengeTag> {
        val savedChallengeTag = challengeTagService.createChallengeTag(challengeTagModel, challengeId)
        val location = ucb.path("/v0/challenges/")
                .path(java.lang.String.valueOf(savedChallengeTag!!.challengeId))
                .path("/tags/")
                .path(java.lang.String.valueOf(savedChallengeTag.tag!!.tagId))
                .build()
                .toUri()
        return ResponseEntity.created(location).body(savedChallengeTag)
    }

    /**
     * Method to get a challenge tag.
     *
     * @param challengeId represents the id of the challenge to which the tag is associated with
     * @param tagId represents the unique id of the tag
     * @return ResponseEntity<ChallengeTag> represents the returned challenge tag
     */
    @GetMapping("/{tagId}", name = "getChallengeTagByChallengeIdAndTagText")
    fun getChallengeTagByChallengeIdAndTagText(
            @PathVariable challengeId: Int,
            @PathVariable tagId: Int
    ): ResponseEntity<ChallengeTag> {
        return ResponseEntity.ok().body(challengeTagService.getChallengeTagByChallengeIdAndTagId(challengeId, tagId))
    }

    /**
     * Method to get a challenge tag.
     *
     * @param challengeId represents the id of the challenge to which the tag is associated with
     * @return ResponseEntity<List<ChallengeTag>> represents the list of the returned challenge tags
     */
    @GetMapping(name = "getChallengeTagsByChallengeId")
    fun getChallengeTagsByChallengeId(
            @PathVariable challengeId: Int
    ): ResponseEntity<List<ChallengeTag>> {
        return ResponseEntity.ok().body(challengeTagService.getChallengeTagByChallengeId(challengeId))
    }

    /**
     * Method to delete a challenge tag.
     *
     * @param tagId represents the challenge tag id to be deleted
     */
    @DeleteMapping("/{tagId}", name = "deleteChallengeTag")
    fun deleteChallengeTag(
            @PathVariable challengeId: Int,
            @PathVariable tagId: Int
    ): ResponseEntity<Void> {
        challengeTagService.deleteChallengeTag(challengeId, tagId)
        return ResponseEntity.noContent().build()
    }
}

