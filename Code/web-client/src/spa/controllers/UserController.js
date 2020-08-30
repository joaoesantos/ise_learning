import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../utils/fetchUtils'

export const UserController = {
  
  createMe: async ({ user }) => {
    let url = apiUrlTemplates.createUser()
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders(),
      body: user
    }
    let response = await fetch(url, options)
    return response.json()
  },

  getUserMe: async () => {
    let url =apiUrlTemplates.myUserOperations()
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
  },

  changeCredentials: async (values) => {
    let url = apiUrlTemplates.myCredentials()
    let options = {
      method: HttpMethods.post,
      headers: defaultHeaders(),
      body: JSON.stringify({})//corrigir body depois de fazer eebase com serviços
    }
    //let response = await fetch(url, options)
    //return response.json()
    return {//test object
      id: 1,
      username: "mogarrio", 
      email: "rodleal@isel.pt",
      name: "Rodrigo Leal"
    }
  },
  
  updateUserData: async (values) => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.post,
      headers: defaultHeaders(),
      body: JSON.stringify({})//corrigir body depois de fazer eebase com serviços
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

