import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../../utils/fetchUtils'
import { LanguageController } from '../LanguageController'

export const QuestionnairePageController = {
    getQuestionnaire: async (uuid) => {
        let url = apiUrlTemplates.getQuestionnaireByUuid(uuid)
        let options = {
          method: HttpMethods.get,
          headers: fetchHeaders.get()
        }
        let response = await fetch(url, options)
        let handledResponse = await handleFetchResponse(response)
        if(handledResponse.severity === "error") {
            return handledResponse
        }
        const availableLanguages = await LanguageController.getAvailableLanguages()
        handledResponse.json.challenges.forEach(async (element) => {
            if(!element.languages){
                element.languages = availableLanguages.json.map(l => l.codeLanguage)
            }
        })
        return handledResponse
    },

    submitQuestionnaire: async(questionnaireInfo) => {
        let options = {
            method: HttpMethods.post,
            headers: fetchHeaders.get()
        }
        const body = {
            questionnaireInstanceId: questionnaireInfo.questionnaireInstanceId,
            questionnaireId: questionnaireInfo.questionnaireId,
            challenges: questionnaireInfo.challenges
        }
        options.body = JSON.stringify(body)
        let url = apiUrlTemplates.submitQuestionnaireAnswer
        let response = await fetch(url, options)
        return handleFetchResponse(response, "Questionnaire submitted successfully!")
    }
}