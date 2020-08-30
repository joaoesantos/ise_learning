import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../utils/fetchUtils'

const questionnaireCtrl = {
    getQuestionnaireInstances: async (credentials) => {
        const headers = defaultHeaders()
        headers.append("Authorization", `Basic ${credentials}`)
        let url = apiUrlTemplates.getQuestionnaireInstances()
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return response.json()
    },

    getQuestionnaireAnswers: async (id, credentials) => {

        const headers = defaultHeaders()
        headers.append("Authorization", `Basic ${credentials}`)
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
    }
}

export default questionnaireCtrl