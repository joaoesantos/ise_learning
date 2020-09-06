package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.challenge.ChallengeTag
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.tag.ChallengeTagModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.challenge.ChallengeTagRepository
import pt.iselearning.services.repository.TagRepository
import pt.iselearning.services.service.challenge.ChallengeService
import java.util.*

@Validated
@Service
class ChallengeTagService (private val tagRepository : TagRepository,
                           private val challengeTagRepository: ChallengeTagRepository,
                           private val challengeRepository: ChallengeRepository,
                           private val challengeService: ChallengeService) {
    @Transactional
    fun createChallengeTag(challengeTagModel: ChallengeTagModel, challengeId: Int, loggedUser: User): ChallengeTag? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.creatorId != loggedUser.userId)  {
            throw ServerException("Cannot create a tag for other user's challenges.",
                    "Cannot create a tag for other user's challenges. Challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        val tag = tagRepository.findByTag(challengeTagModel.tag)
        if(tag.isEmpty) {
            val newTag = Tag(null, challengeTagModel.tag)
            tagRepository.save(newTag)
        }
        var newChallengeTag = ChallengeTag(null, challengeId, tagRepository.findByTag(challengeTagModel.tag).get())
        return challengeTagRepository.save(newChallengeTag)
    }

    @Transactional
    fun getChallengeTagByChallengeIdAndTagId(challengeId: Int, tagId: Int, loggedUser: User?): ChallengeTag? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.isPrivate!! && loggedUser == null)  {
            throw ServerException("Cannot retrieve a tag from a user's private challenges while logged out.",
                    "Cannot retrieve a tag from a user's private while logged out. Logged in user must be challenge creator.",
                    ErrorCode.FORBIDDEN)
        }
        if(challenge.isPrivate!! && challenge.creatorId != loggedUser!!.userId)  {
            throw ServerException("Cannot retrieve a tag from other user's private challenges.",
                    "Cannot retrieve a tag from other user's private challenges. Private challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        checkIfChallengeTagExists(challengeTag, challengeId, tagId)
        return challengeTag.get()
    }

    @Transactional
    fun getChallengeTagByChallengeId(challengeId: Int, loggedUser: User?): List<ChallengeTag>? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.isPrivate!! && loggedUser == null)  {
            throw ServerException("Cannot retrieve tags from a user's private challenges while logged out.",
                    "Cannot retrieve tags from a user's private while logged out. Logged in user must be challenge creator.",
                    ErrorCode.FORBIDDEN)
        }
        if(challenge.isPrivate!! && challenge.creatorId != loggedUser!!.userId)  {
            throw ServerException("Cannot retrieve tags from other user's private challenges.",
                    "Cannot retrieve tags from other user's private challenges. Private challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        return challengeTagRepository.findAllByChallengeId(challengeId)
    }

    @Transactional
    fun deleteChallengeTag(challengeId: Int, tagId: Int, loggedUser: User) {
        checkIfTagExists(tagRepository.findById(tagId), tagId)
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.creatorId != loggedUser.userId)  {
            throw ServerException("Cannot delete a tag from other user's challenges.",
                    "Cannot delete a tag from other user's challenges. Challenge belongs to other user.",
                    ErrorCode.FORBIDDEN)
        }
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        challengeTagRepository.delete(challengeTag.get())
    }

    fun checkIfChallengeTagExists(challengeTag: Optional<ChallengeTag>, challengeId: Int, tagId: Int) {
        if (challengeTag.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This challenge tag does not exist.", // dar fix quando houver refactoring
                    "There is no challenge tag with challenge id: $challengeId and tag id: $tagId")
        }
    }

    fun checkIfTagExists(tag: Optional<Tag>, tagId: Int) {
        if (tag.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This tag does not exist.", // dar fix quando houver refactoring
                    "There is no tag with tag id: $tagId")
        }
    }
}
