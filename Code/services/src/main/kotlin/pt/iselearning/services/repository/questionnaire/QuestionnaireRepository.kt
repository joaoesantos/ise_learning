package pt.iselearning.services.repository.questionnaire

import org.springframework.data.repository.CrudRepository
import pt.iselearning.services.domain.questionnaires.Questionnaire

/**
 * Interface that represents a repository for the questionnaire class
 */
interface QuestionnaireRepository : CrudRepository<Questionnaire, Int> {
    fun findAllByCreatorId(userId: Int): List<Questionnaire>
}