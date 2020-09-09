import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, FetchHeaders } from '../../utils/fetchUtils'
import { LanguageController } from '../LanguageController'

export const ChallengeController = {

  getAllChallenges: async () => {
    let url = apiUrlTemplates.getAllChallenges()
    let options = {
        method: HttpMethods.get,
        headers: FetchHeaders.get()
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
      }
    } else {
      return {
        message: jsonResponse.message,
        severity: 'error'
      }
    }
  },

  getRandomChallenge: async () => {
      let url = apiUrlTemplates.getRandomChallenge()
      let options = {
          method: HttpMethods.get,
          headers: FetchHeaders.get()
      }
      let response = await fetch(url, options)
      let jsonResponse = await response.json()
      if(response.ok) {
        return {
          json: jsonResponse,
        }
      } else {
        return {
          message: jsonResponse.message,
          severity: 'error'
        }
      }
  },

  getChallengeById: async (challengeId) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.get,
      headers: FetchHeaders.get()
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
      }
    } else {
      return {
        message: jsonResponse.message,
        severity: 'error'
      }
    }
  },

  createChallenge: async (challengeModel) => {
    let url = apiUrlTemplates.challenges()
    let options = {
      method: HttpMethods.post,
      headers: FetchHeaders.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: jsonResponse,
      }
    } else {
      return {
        message: jsonResponse.message,
        severity: 'error'
      }
    }
  },

  updateChallenge: async (challengeId, challengeModel) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.put,
      headers: FetchHeaders.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    let jsonResponse = await response.json()
    if(response.ok) {
      return {
        json: {challenge: jsonResponse},
      }
    } else {
      return {
        message: jsonResponse.message,
        severity: 'error'
      }
    }
  },

  getChallengeByIdAndAvailableLanguages: async (challengeId) => {
    let challengePromise = ChallengeController.getChallengeById(challengeId);
    let languagesPromise = LanguageController.getAvailableLanguages();
    let responses = await Promise.all([challengePromise, languagesPromise])
    if(responses[0].severity && responses[0].severity === 'error') {
      return {
        message: responses[0].message,
        severity: 'error'
      }
    }
    if(responses[1].severity && responses[1].severity === 'error') {
      return {
        message: responses[1].message,
        severity: 'error'
      }
    }
    return {
      json : {
        languages: responses[1].json,
        challenge: responses[0].json
      }
    }
  }

}
