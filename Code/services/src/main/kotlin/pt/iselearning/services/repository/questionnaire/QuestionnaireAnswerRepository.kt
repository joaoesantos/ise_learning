package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer

/**
 * Interface that represents a repository for the questionnaire class
 */
interface QuestionnaireAnswerRepository : CrudRepository<QuestionnaireAnswer, Int> {
    fun findAllByQuestionnaireInstanceId(userId: Int): List<QuestionnaireAnswer>
}