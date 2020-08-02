package pt.iselearning.services.util

import org.modelmapper.ModelMapper

import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.models.questionnaire.QuestionnaireChallengeModel
import pt.iselearning.services.repository.challenge.ChallengeRepository
import pt.iselearning.services.repository.questionnaire.QuestionnaireRepository

/**
 * Method responsible to add the necessary Questionnaire-Challenge mapping to the given ModelMapper instance
 * @param mapper instance of ModelMapper
 */
fun addQuestionnaireChallengeMappings(
        mapper: ModelMapper,
        questionnaireRepository: QuestionnaireRepository,
        challengeRepository: ChallengeRepository
) {
    mapper.typeMap(QuestionnaireChallengeModel::class.java, QuestionnaireChallenge::class.java).addMappings { mapper ->
        mapper.map(
                { source: QuestionnaireChallengeModel -> source.questionnaireId },
                { destination: QuestionnaireChallenge?, value: Int? -> run {
                    val temp = questionnaireRepository.findById(value?:0)
                    destination?.questionnaire = if(temp.isEmpty) null else temp.get()
                } }
        )
        mapper.map(
                { source: QuestionnaireChallengeModel -> source.challengeId },
                { destination: QuestionnaireChallenge?, value: Int? -> run {
                    val temp = challengeRepository.findById(value?:0)
                    destination?.challenge = if(temp.isEmpty) null else temp.get()
                } }
        )
        mapper.map(
                { source: QuestionnaireChallengeModel -> source.languageFilter },
                { destination: QuestionnaireChallenge?, value: String? -> destination?.languageFilter = value }
        )
    }
}
