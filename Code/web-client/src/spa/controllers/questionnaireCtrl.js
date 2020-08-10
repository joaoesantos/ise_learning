import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

const questionnaireCtrl = {
    getQuestionnaireInstances: async () => {
        let url = '/v0/questionnaireAnswers'
        let options = {
            method: HttpMethods.get,
            headers: defaultHeaders()
        }
        let response = await fetch(url, options)
        return response.json()
    }
}

export default questionnaireCtrl