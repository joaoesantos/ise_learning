import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../../utils/fetchUtils'

export const QuestionnaireInstanceController = {

    createQuestionnaireInstance: async (data) => {
        let url = apiUrlTemplates.createQuestionnaireInstance()
        let options = {
            method: HttpMethods.post,
            headers: fetchHeaders.get(),
            body: JSON.stringify(data)
        }
        let response = await fetch(url, options)
        let handledResponse = await handleFetchResponse(response)
        if(handledResponse.severity === "error") {
            return handledResponse
        } else {
            let secondResponse = await QuestionnaireInstanceController.getAllQuestionnaireInstancesByQuestionnaireId(data.questionnaireId)
            if(secondResponse.severity === "success") {
                secondResponse.message = "Questionnaire instance created successfully!"
            } 
            return secondResponse
        }
    },

    getAllQuestionnaireInstancesByQuestionnaireId: async (questionnaireId) => {
        let url = apiUrlTemplates.getAllQuestionnaireInstancesByQuestionnaireId(questionnaireId)
        let options = {
            method: HttpMethods.get,
            headers: fetchHeaders.get()
        }
        let response = await fetch(url, options)
        return handleFetchResponse(response)
    },

    updateQuestionnaireInstance: async (data) => {
        let url = apiUrlTemplates.updateQuestionnaireInstance(data.questionnaireInstanceId)
        let options = {
            method: HttpMethods.put,
            headers: fetchHeaders.get(),
            body: JSON.stringify({
                questionnaireId: data.questionnaireId,
                description: data.description,
                timer: data.timer
            })
        }
        let response = await fetch(url, options)
        let handledResponse = await handleFetchResponse(response)
        if(handledResponse.severity === "error") {
            return handledResponse
        } else {
            let secondResponse = await QuestionnaireInstanceController.getAllQuestionnaireInstancesByQuestionnaireId(data.questionnaireId)
            if(secondResponse.severity === "success") {
                secondResponse.message = "Questionnaire instance updated successfully!"
            } 
            return secondResponse
        }
    },

    deleteQuestionnaireInstance: async (data) => {
        let url = apiUrlTemplates.deleteQuestionnaireInstance(data.questionnaireInstanceId)
        let options = {
            method: HttpMethods.delete,
            headers: fetchHeaders.get(),
        }
        let response = await fetch(url, options)
        let handledResponse = await handleFetchResponse(response)
        if(handledResponse.severity === "error") {
            return handledResponse
        } else {
            let secondResponse = await QuestionnaireInstanceController.getAllQuestionnaireInstancesByQuestionnaireId(data.questionnaireId)
            if(secondResponse.severity === "success") {
                secondResponse.message = "Questionnaire instance delete successfully!"
            } 
            return secondResponse
        }
    },

}