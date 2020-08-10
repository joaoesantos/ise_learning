package pt.iselearning.services.repository.questionnaire

import pt.iselearning.services.domain.questionnaires.QuestionnaireInstanceQuestionnaireView
import pt.iselearning.services.repository.ViewRepository

interface QuestionnaireInstanceQuestionnaireViewRepository : ViewRepository<QuestionnaireInstanceQuestionnaireView, Int>{
    fun findAllByCreatorId(userId : Int) : List<QuestionnaireInstanceQuestionnaireView>
}