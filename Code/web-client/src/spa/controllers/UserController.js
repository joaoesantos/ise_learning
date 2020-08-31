import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchHeaders } from '../utils/fetchUtils'

export const UserController = {

  login: async () => {
    let url = apiUrlTemplates.login()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        message: `Welcome ${jsonResponse.name}`,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },
  
  createMe: async (userModel) => {
    let url = apiUrlTemplates.createUser()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        message: `Welcome ${jsonResponse.name}`,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },

  getMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },

  updateMe: async (userModel) => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },

  changeCredentials: async (userModel) => {
    let url = apiUrlTemplates.myCredentials()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        message: `Credentials updated successfully`,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  }
  
}

export default UserController

