import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchHeaders } from '../utils/fetchUtils'

export const LanguageController = {
  getAvailableLanguages: async () => {
    let url = apiUrlTemplates.languages()
    let options = {
      method: HttpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return response.json()
  },
}
