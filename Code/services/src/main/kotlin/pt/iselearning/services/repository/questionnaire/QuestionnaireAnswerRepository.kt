package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireAnswer

/**
 * Interface that represents a repository for the questionnaire domain
 */
interface QuestionnaireAnswerRepository : CrudRepository<QuestionnaireAnswer, Int> {
    fun getAllQuestionnaireAnswersFromQuestionnaireInstanceId(questionnaireInstanceId: Int): List<QuestionnaireAnswer>
}