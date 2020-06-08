package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge

/**
 * Interface that represents a repository for the questionnaire-challenge domain
 */
interface QuestionnaireChallengeRepository : CrudRepository<QuestionnaireChallenge, Int> {
    fun getAllChallengesByQuestionnaireId(questionnaireId: Int): List<Challenge>
}