import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchUtils } from '../components/fetchUtils'

export const LanguageController = {

  getAvailableLanguages: async () => {
    let url = apiUrlTemplates.languages()
    let options = {
      method: HttpMethods.get,
      headers: fetchUtils.get()
    }
    let response = await fetch(url, options)
    return response.json()
  }

}
