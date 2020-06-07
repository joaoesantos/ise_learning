package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.QuestionnaireInstance

/**
 * Interface that represents a repository for the questionnaire class
 */
interface QuestionnaireInstanceRepository : CrudRepository<QuestionnaireInstance, Int> {
    fun findAllByQuestionnaireId(userId: Int): List<QuestionnaireInstance>
}