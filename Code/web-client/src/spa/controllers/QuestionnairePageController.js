import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../utils/fetchUtils'

export const QuestionnairePageController = {
    getQuestionnaire: async () => {
        let url = '/v0/questionnaireInstances/solve/dd5badcc-e286-451c-98fd-8042929c9ed8'
        let options = {
          method: HttpMethods.get,
          headers: defaultHeaders()
        }
        let response = await fetch(url, options)
        return response.json()

    },
    submitChallenge: async () => {

    },
    submitQuestionnaire: async(questionnaireInfo) => {
        let options = {
            method: HttpMethods.post,
            headers: defaultHeaders()
        }

        const body = {
            questionnaireId: questionnaireInfo.questionnaire.questionnaireId,
            questionnaireId: questionnaireInfo.questionnaire.questionnaireId,
            questionnaireInstanceId: questionnaireInfo.questionnaire.questionnaireInstanceId,
            challengesAnswers: questionnaireInfo.challengesAnswer.map(cc => {
                return {
                    challengeId: cc.id,
                    answer: {
                        codeLanguage: cc.codeLanguage,
                        answerCode: cc.answerCode,
                        unitTests: cc.unitTests
                    }
                }
            })
        }
        const url = apiUrlTemplates.createQuestionnaireAnswer
        let response = await fetch(url, options)
    }
}