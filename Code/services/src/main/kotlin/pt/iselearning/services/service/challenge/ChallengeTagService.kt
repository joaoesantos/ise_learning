package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.challenge.ChallengeTag
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.tag.ChallengeTagModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.challenge.ChallengeTagRepository
import pt.iselearning.services.repository.TagRepository
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfChallengeTagExists
import pt.iselearning.services.util.checkIfTagExists

/**
 * This class contains the business logic associated with the actions on the challenge tag object
 */
@Validated
@Service
class ChallengeTagService (
        private val tagRepository : TagRepository,
        private val challengeTagRepository: ChallengeTagRepository,
        private val challengeRepository: ChallengeRepository,
        private val challengeService: ChallengeService
) {

    /**
     * Create a challenge tag.
     *
     * @param challengeTagModel object information
     * @param loggedUser user that is calling the service
     * @return created questionnaire
     */
    @Transactional
    fun createChallengeTag(challengeTagModel: ChallengeTagModel, challengeId: Int, loggedUser: User): ChallengeTag? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.creatorId != loggedUser.userId)  {
            throw ServiceException(
                    "Cannot create a tag for other user's challenges.",
                    "Cannot create a tag for other user's challenges. Challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        val tag = tagRepository.findByTag(challengeTagModel.tag)
        if(tag.isEmpty) {
            val newTag = Tag(null, challengeTagModel.tag)
            tagRepository.save(newTag)
        }
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val newChallengeTag = ChallengeTag(null, challengeId, tagRepository.findByTag(challengeTagModel.tag).get())
        return challengeTagRepository.save(newChallengeTag)
    }

    /**
     * Get a challenge tag by its challenge unique identifier and tag unique identifier
     *
     * @param challengeId challenge unique identifier
     * @param tagId tag unique identifier
     * @param loggedUser user that is calling the service
     */
    @Transactional
    fun getChallengeTagByChallengeIdAndTagId(challengeId: Int, tagId: Int, loggedUser: User?): ChallengeTag? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.isPrivate!! && loggedUser == null)  {
            throw ServiceException(
                    "Cannot retrieve a tag from a user's private challenges while logged out.",
                    "Cannot retrieve a tag from a user's private while logged out. Logged in user must be challenge creator.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        if(challenge.isPrivate!! && challenge.creatorId != loggedUser!!.userId)  {
            throw ServiceException(
                    "Cannot retrieve a tag from other user's private challenges.",
                    "Cannot retrieve a tag from other user's private challenges. Private challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        checkIfChallengeTagExists(challengeTag, challengeId, tagId)
        return challengeTag.get()
    }

    /**
     * Get a challenge by its unique identifier
     *
     * @param challengeId challenge unique identifier
     * @param loggedUser user that is calling the service
     */
    @Transactional
    fun getChallengeTagByChallengeId(challengeId: Int, loggedUser: User?): List<ChallengeTag>? {
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.isPrivate!! && loggedUser == null)  {
            throw ServiceException(
                    "Cannot retrieve tags from a user's private challenges while logged out.",
                    "Cannot retrieve tags from a user's private while logged out. Logged in user must be challenge creator.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        if(challenge.isPrivate!! && challenge.creatorId != loggedUser!!.userId)  {
            throw ServiceException(
                    "Cannot retrieve tags from other user's private challenges.",
                    "Cannot retrieve tags from other user's private challenges. Private challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        return challengeTagRepository.findAllByChallengeId(challengeId)
    }

    /**
     * Delete a challenge tag by its challenge unique identifier and tag unique identifier
     *
     * @param challengeId challenge unique identifier
     * @param tagId tag unique identifier
     * @param loggedUser user that is calling the service
     */
    @Transactional
    fun deleteChallengeTag(challengeId: Int, tagId: Int, loggedUser: User) {
        checkIfTagExists(tagRepository.findById(tagId), tagId)
        val challenge = challengeService.getChallengeById(challengeId, loggedUser)
        if(challenge.creatorId != loggedUser.userId)  {
            throw ServiceException(
                    "Cannot delete a tag from other user's challenges.",
                    "Cannot delete a tag from other user's challenges. Challenge belongs to other user.",
                    "/iselearning/user/notResourceOwner",
                    ErrorCode.FORBIDDEN
            )
        }
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        challengeTagRepository.delete(challengeTag.get())
    }

}
