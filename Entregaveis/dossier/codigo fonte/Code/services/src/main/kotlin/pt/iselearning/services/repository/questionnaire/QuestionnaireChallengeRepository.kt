package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import java.util.*

/**
 * Interface that represents a repository for the questionnaire-challenge domain
 */
interface QuestionnaireChallengeRepository : CrudRepository<QuestionnaireChallenge, Int> {
    fun findByQuestionnaireQuestionnaireIdAndChallengeChallengeId(questionnaireId: Int, challengeId: Int): Optional<QuestionnaireChallenge>
    fun findAllByQuestionnaireQuestionnaireId(questionnaireId: Int): List<QuestionnaireChallenge>
}