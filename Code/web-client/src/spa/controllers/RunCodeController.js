import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchHeaders } from '../utils/fetchUtils'

export const RunCodeController = {

    execute: async (payload) => {
        let url = apiUrlTemplates.executeCode()
        let options = {
            method: HttpMethods.post,
            headers: fetchHeaders.get(),
            body: JSON.stringify(payload)
        }
        let response = await fetch(url, options)
        return response.json()
    }

}