import { apiUrlTemplates } from '../clientSideConfig'
import { httpMethods, fetchHeaders, handleFetchResponse } from '../utils/fetchUtils'

export const RunCodeController = {

    execute: async (payload) => {
        let url = apiUrlTemplates.executeCode()
        let options = {
            method: httpMethods.post,
            headers: fetchHeaders.get(),
            body: JSON.stringify(payload)
        }
        let response = await fetch(url, options)
        return handleFetchResponse(response)
    }

}