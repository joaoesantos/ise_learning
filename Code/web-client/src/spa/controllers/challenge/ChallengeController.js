import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../../utils/fetchUtils'
import { LanguageController } from '../LanguageController'
import { ChallengeAnswerController } from '../challenge/ChallengeAnswerController'

export const ChallengeController = {

  getAllChallenges: async () => {
    let url = apiUrlTemplates.getAllChallenges()
    let options = {
        method: HttpMethods.get,
        headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

  getRandomChallenge: async () => {
      let url = apiUrlTemplates.getRandomChallenge()
      let options = {
          method: HttpMethods.get,
          headers: fetchHeaders.get()
      }
      let response = await fetch(url, options)
      return handleFetchResponse(response)
  },

  getChallengeById: async (challengeId) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

  deleteChallengeById: async (challengeId) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.delete,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

  createChallenge: async (challengeModel) => {
    let url = apiUrlTemplates.challenges()
    let options = {
      method: HttpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response,"Challenge created successfully!")
  },

  updateChallenge: async (challengeId, challengeModel) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.put,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response, "Challenge updated successfully!")
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
  },

  getChallengeByIdAvailableLanguagesAndChallengeAnswerIfExists: async(challengeId, userId) => {
    let challengeAndLanguagesPromisse = ChallengeController.getChallengeByIdAndAvailableLanguages(challengeId)
    let challengeAnswersPromisse = (userId === undefined) ? Promise.resolve({json: []}) : ChallengeAnswerController.getChallengeAnswerByChallengeIdAndUserId(challengeId, userId)

    let responses = await Promise.all([challengeAndLanguagesPromisse, challengeAnswersPromisse])
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
        languages: responses[0].json.languages,
        challenge: responses[0].json.challenge,
        challengeAnswers: responses[1].json
      }
    }
  }

}
