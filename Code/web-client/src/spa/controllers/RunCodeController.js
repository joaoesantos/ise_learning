import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

export const RunCodeController = {
    execute: async (payload) => {
        let url = apiUrlTemplates.executeCode()
        let options = {
            method: HttpMethods.post,
            headers: defaultHeaders(),
            body: JSON.stringify(payload)
        }
        let response = await fetch(url, options)
        return response.json()
    }
}