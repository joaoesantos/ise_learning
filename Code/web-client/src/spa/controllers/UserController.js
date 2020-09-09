import { apiUrlTemplates } from '../clientSideConfig'
import { httpMethods, fetchHeaders, handleFetchResponse } from '../utils/fetchUtils'

export const UserController = {

  logMein: async () => {
    let url = apiUrlTemplates.login()
    let options = {
      method: httpMethods.post,
      headers: fetchHeaders.get(),
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome back!")
  },

  createMe: async (userModel) => {
    let url = apiUrlTemplates.createUser()
    let options = {
      method: httpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome to IS E-Learning!")
  },

  getMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: httpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome back!")
  },

  updateMe: async (userModel) => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: httpMethods.patch,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Profile updated successfully!")
  },

  changeMyCredentials: async (credentials) => {
    let url = apiUrlTemplates.myCredentials()
    let options = {
      method: httpMethods.put,
      headers: fetchHeaders.get(),
      body: JSON.stringify(credentials)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Credentials updated successfully!")
  },

  deleteMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: httpMethods.delete,
      headers: fetchHeaders.get(),
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

}

