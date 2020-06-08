package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance

/**
 * Interface that represents a repository for the questionnaire instance domain
 */
interface QuestionnaireInstanceRepository : CrudRepository<QuestionnaireInstance, Int> {
    fun findAllByQuestionnaireId(questionnaireId: Int): List<QuestionnaireInstance>
}