import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../utils/fetchUtils'
import { LanguageController } from './LanguageController'

export const QuestionnairePageController = {
    getQuestionnaire: async (uuid) => {
        let url = `/v0/questionnaireInstances/solve/${uuid}`
        let options = {
          method: HttpMethods.get,
          headers: defaultHeaders()
        }
        let response = await fetch(url, options)
        let json = await response.json();
        const availableLanguages = await LanguageController.getAvailableLanguages()
        json.challenges.forEach(async (element) => {
            if(!element.languages){
                element.languages = availableLanguages.json.map(l => l.codeLanguage)
            }
        });
        return json

    },
    submitChallenge: async () => {

    },
    submitQuestionnaire: async(questionnaireInfo) => {
        let options = {
            method: HttpMethods.post,
            headers: defaultHeaders()
        }
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