import { apiUrlTemplates } from '../clientSideConfig'
import { httpMethods, fetchHeaders, handleFetchResponse } from '../utils/fetchUtils'

export const LanguageController = {

  getAvailableLanguages: async () => {
    let url = apiUrlTemplates.languages()
    let options = {
      method: httpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return response.json()
  },
  
}
