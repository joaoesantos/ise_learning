package pt.iselearning.services.service.challenge

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.challenge.ChallengeTag
import pt.iselearning.services.domain.Tag
import pt.iselearning.services.models.tag.ChallengeTagModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.challenge.ChallengeTagRepository
import pt.iselearning.services.repository.TagRepository
import pt.iselearning.services.util.checkIfChallengeExists
import pt.iselearning.services.util.checkIfChallengeTagExists
import pt.iselearning.services.util.checkIfTagExists

@Validated
@Service
class ChallengeTagService (
        private val tagRepository : TagRepository,
        private val challengeTagRepository: ChallengeTagRepository,
        private val challengeRepository: ChallengeRepository
) {
    @Transactional
    fun createChallengeTag(challengeTagModel: ChallengeTagModel, challengeId: Int): ChallengeTag? {
        val tag = tagRepository.findByTag(challengeTagModel.tag)
        if(tag.isEmpty) {
            val newTag = Tag(null, challengeTagModel.tag)
            tagRepository.save(newTag)
        }
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val newChallengeTag = ChallengeTag(null, challengeId, tagRepository.findByTag(challengeTagModel.tag).get())
        return challengeTagRepository.save(newChallengeTag)
    }

    @Transactional
    fun getChallengeTagByChallengeIdAndTagId(challengeId: Int, tagId: Int): ChallengeTag? {
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        checkIfChallengeTagExists(challengeTag, challengeId, tagId)
        return challengeTag.get()
    }

    @Transactional
    fun getChallengeTagByChallengeId(challengeId: Int): List<ChallengeTag>? {
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        return challengeTagRepository.findAllByChallengeId(challengeId)
    }

    @Transactional
    fun deleteChallengeTag(challengeId: Int, tagId: Int) {
        checkIfTagExists(tagRepository.findById(tagId), tagId)
        checkIfChallengeExists(challengeRepository.findById(challengeId), challengeId)
        val challengeTag = challengeTagRepository.findByChallengeIdAndTagTagId(challengeId, tagId)
        checkIfChallengeTagExists(challengeTag, challengeId, tagId)
        challengeTagRepository.delete(challengeTag.get())
    }

}
