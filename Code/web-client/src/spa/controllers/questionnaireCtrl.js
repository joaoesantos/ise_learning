import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

const questionnaireCtrl = {
    getQuestionnaireInstances: async () => {
        const headers = defaultHeaders()
        headers.append("Authorization", "Basic dXNlcjE6dXNlcjE=")
        let url = '/v0/questionnaireAnswers'
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return response.json()
    }
}

export default questionnaireCtrl