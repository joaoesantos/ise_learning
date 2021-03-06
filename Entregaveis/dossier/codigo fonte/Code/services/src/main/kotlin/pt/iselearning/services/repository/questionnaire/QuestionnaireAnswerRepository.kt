package pt.iselearning.services.repository.questionnaire

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer

/**
 * Interface that represents a repository for the questionnaire answer domain
 */
interface QuestionnaireAnswerRepository : CrudRepository<QuestionnaireAnswer, Int> {
    fun findAllByQuestionnaireInstanceId(questionnaireInstanceId: Int): List<QuestionnaireAnswer>
}