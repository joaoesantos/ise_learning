import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, FetchHeaders } from '../utils/fetchUtils'

export const UserController = {

  logMein: async () => {
    let url = apiUrlTemplates.login()
    let options = {
      method: HttpMethods.post,
      headers: FetchHeaders.get(),
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
      headers: FetchHeaders.get(),
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
      headers: FetchHeaders.get()
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

  updateMe: async (userModel) => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.patch,
      headers: FetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
        message: `Profile updated successfully`,
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },

  changeMyCredentials: async (credentials) => {
    let url = apiUrlTemplates.myCredentials()
    let options = {
      method: HttpMethods.put,
      headers: FetchHeaders.get(),
      body: JSON.stringify(credentials)
    }
    console.log('pass',options.body)
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
  },

  deleteMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.delete,
      headers: FetchHeaders.get(),
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        severity: 'success'
      }
    } else {
      return {
        message: jsonResponse.detail,
        severity: 'error'
      }
    }
  },

}

