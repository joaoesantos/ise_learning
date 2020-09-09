import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../utils/fetchUtils'

export const UserController = {

  logMein: async () => {
    let url = apiUrlTemplates.login()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome back!")
  },

  createMe: async (userModel) => {
    let url = apiUrlTemplates.createUser()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome to IS E-Learning!")
  },

  getMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Welcome back!")
  },

  updateMe: async (userModel) => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.patch,
      headers: fetchHeaders.get(),
      body: JSON.stringify(userModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Profile updated successfully!")
  },

  changeMyCredentials: async (credentials) => {
    let url = apiUrlTemplates.myCredentials()
    let options = {
      method: HttpMethods.put,
      headers: fetchHeaders.get(),
      body: JSON.stringify(credentials)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Credentials updated successfully!")
  },

  deleteMe: async () => {
    let url = apiUrlTemplates.myUserOperations()
    let options = {
      method: HttpMethods.delete,
      headers: fetchHeaders.get(),
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

}

