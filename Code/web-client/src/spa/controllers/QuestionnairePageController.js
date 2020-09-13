import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../utils/fetchUtils'

export const QuestionnairePageController = {
    getQuestionnaire: async () => {
        let url = '/v0/questionnaireInstances/solve/0c98e06e-b5cb-4088-8fcb-88080c0a83db'
        let options = {
          method: HttpMethods.get,
          headers: defaultHeaders()
        }
        let response = await fetch(url, options)
        let json = await response.json();
        return json

    },
    submitChallenge: async () => {

    },
    submitQuestionnaire: async(questionnaireInfo) => {
        let options = {
            method: HttpMethods.post,
            headers: defaultHeaders()
        }

        console.log(questionnaireInfo.challenges)
        const body = {
            questionnaireInstanceId: questionnaireInfo.questionnaireInstanceId,
            questionnaireId: questionnaireInfo.questionnaireId,
            challenges: questionnaireInfo.challenges
        }

        options.body = JSON.stringify(body)
        const url = apiUrlTemplates.createQuestionnaireAnswer
        let response = await fetch('/v0/questionnaireAnswers', options)
        return response.json()
    }
}