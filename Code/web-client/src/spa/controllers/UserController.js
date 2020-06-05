import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

export const UserController = {
  getUserMe: async () => {
    let url = "v0/users/1"//apiUrlTemplates.profile() - descomentar quando autenticação tiver implementada
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders()
    }
    //let response = await fetch(url, options)
    //return response.json()
    return {//test object
      id: 1,
      username: "mogarrio", 
      email: "rodleal@isel.pt",
      name: "Rodrigo Leal"
    }
  }
}
