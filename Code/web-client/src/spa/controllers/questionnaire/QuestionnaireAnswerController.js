import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../../utils/fetchUtils'

export const QuestionnaireAnswerController = {

    getAllQuestionnaireAnswersFromQuestionnaireCreator: async () => {
        const headers = fetchHeaders.get()
        let url = apiUrlTemplates.getAllQuestionnaireAnswersFromQuestionnaireCreator()
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return handleFetchResponse(response)
    },

    getAllQuestionnaireAnswersFromQuestionnaireCreator: async () => {
        const headers = fetchHeaders.get()
        let url = apiUrlTemplates.getAllQuestionnaireAnswersFromQuestionnaireCreator()
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return handleFetchResponse(response)
    },
  
    getQuestionnaireAnswers: async (id) => {
  
        const headers = fetchHeaders.get()
        let url =  apiUrlTemplates.getQuestionnaireAnswers(id)
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        const responseJson = await response.json()
        const questionnaire = {
            challenges: responseJson.map(el => el.answer)
        }
        return questionnaire
    },

}