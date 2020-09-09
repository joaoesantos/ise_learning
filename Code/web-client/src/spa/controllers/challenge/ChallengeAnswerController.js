import { apiUrlTemplates } from '../../clientSideConfig'
import { httpMethods, fetchHeaders } from '../../utils/fetchUtils'
import { ChallengeController } from './ChallengeController'

export const ChallengeAnswerController = {
  getChallengeAndChallengeAnswerBychallengeIdAndUserId: async (challengeId, userId) => {
    let challengePromise = ChallengeController.getChallengeById(challengeId);
    let challengeAnswerPromise = ChallengeAnswerController.getChallengeAnswerByChallengeIdAndUserId(challengeId, userId);
    let responses = await Promise.all([challengePromise, challengeAnswerPromise])
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
  getChallengeAnswerByChallengeIdAndUserId: async (challengeId, userId) => {
    let url = apiUrlTemplates.challengeAnswerByChallengeIdAndUserId(challengeId, userId)
    let options = {
      method: httpMethods.get,
      headers: fetchHeaders.get()
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
  createChallengeAnswer: async (challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswers()
    let options = {
      method: httpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeAnswerModel)
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
  updateChallengeAnswer: async (challengeAnswerId, challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswer(challengeAnswerId)
    let options = {
      method: httpMethods.put,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeAnswerModel)
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
}
