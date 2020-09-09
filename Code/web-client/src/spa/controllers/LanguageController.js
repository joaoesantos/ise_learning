import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, FetchHeaders } from '../utils/fetchUtils'

export const LanguageController = {

  getAvailableLanguages: async () => {
    let url = apiUrlTemplates.languages()
    let options = {
      method: HttpMethods.get,
      headers: FetchHeaders.get()
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse
      }
    } else {
      return {
        message: jsonResponse.message,
        severity: 'error'
      }
    }
  },
  
}
