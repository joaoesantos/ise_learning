import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

export const LanguageController = {
  getAvailableLanguages: async () => {
    let url = apiUrlTemplates.languages()
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders()
    }
    let response = await fetch(url, options)
    return response.json()
  } 
}