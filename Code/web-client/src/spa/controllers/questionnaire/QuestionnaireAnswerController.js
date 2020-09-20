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
  
    getQuestionnaireAnswers: async (questionnaireInstanceId) => {
  
        const headers = fetchHeaders.get()
        let url =  apiUrlTemplates.getAllQuestionnaireAnswersFromQuestionnaireInstanceId(questionnaireInstanceId)
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        let handledResponse = await handleFetchResponse(response)
        if(handledResponse.severity === "error") {
            return handledResponse
        } else {
            let challenges = handledResponse.json.map(el => el.answer)
            handledResponse.json.challenges = challenges
            return handledResponse
        }
    },

}