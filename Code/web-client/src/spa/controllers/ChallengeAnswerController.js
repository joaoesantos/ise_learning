import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'
import {LanguageController} from './LanguageController'
import {ChallengeController} from './ChallengeController'

export const ChallengeAnswerController = {
  getChallengeAndChallengeAnswerBychallengeIdAndUserId: async (challengeId, userId) => {
    let challengePromise = ChallengeController.getChallengeById(challengeId);
    let challengeAnswerPromise = ChallengeAnswerController.getChallengeAnswerByChallengeIdAndUserId(challengeId, userId);
    let responses = await Promise.all([challengePromise, challengeAnswerPromise])
    return {
      challenge: responses[0],
      challengeAnswer: responses[1]
    }
  },
  getChallengeAnswerByChallengeIdAndUserId: async (challengeId, userId) => {
    let url = apiUrlTemplates.challengeAnswerByChallengeIdAndUserId(challengeId, userId)
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders()
    }
    let response = await fetch(url, options)
    return response.json()
  },
  createChallengeAnswer: async (challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswers()
    let options = {
      method: HttpMethods.post,
      headers: defaultHeaders(),
      body: JSON.stringify(challengeAnswerModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },
  updateChallengeAnswer: async (challengeAnswerId, challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswer(challengeAnswerId)
    let options = {
      method: HttpMethods.put,
      headers: defaultHeaders(),
      body: JSON.stringify(challengeAnswerModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },
}
