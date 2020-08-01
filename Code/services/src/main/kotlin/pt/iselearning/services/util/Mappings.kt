package pt.iselearning.services.util

import org.modelmapper.ModelMapper

import pt.iselearning.services.domain.questionnaires.QuestionnaireChallenge
import pt.iselearning.services.models.questionnaire.CreateQuestionnaireChallengeModel
import pt.iselearning.services.repository.ChallengeRepository
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
    mapper.typeMap(CreateQuestionnaireChallengeModel::class.java, QuestionnaireChallenge::class.java).addMappings { mapper ->
        mapper.map(
                { source: CreateQuestionnaireChallengeModel -> source.questionnaireId },
                { destination: QuestionnaireChallenge?, value: Int? -> run {
                    val temp = questionnaireRepository.findById(value?:0)
                    println("$temp")
                    destination?.questionnaire = if(temp.isEmpty) null else temp.get()
                } }
        )
        mapper.map(
                { source: CreateQuestionnaireChallengeModel -> source.challengeId },
                { destination: QuestionnaireChallenge?, value: Int? -> run {
                    val temp = challengeRepository.findById(value?:0)
                    println("$temp")
                    destination?.challenge = if(temp.isEmpty) null else temp.get()
                } }
        )
        mapper.map(
                { source: CreateQuestionnaireChallengeModel -> source.languageFilter },
                { destination: QuestionnaireChallenge?, value: String? -> destination?.languageFilter = value }
        )
    }
}
