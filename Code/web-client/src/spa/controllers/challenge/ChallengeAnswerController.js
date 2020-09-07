import { apiUrlTemplates } from '../../clientSideConfig'
import { httpMethods, fetchHeaders } from '../../utils/fetchUtils'
import { ChallengeController } from './ChallengeController'

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
      method: httpMethods.get,
      headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return response.json()
  },
  createChallengeAnswer: async (challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswers()
    let options = {
      method: httpMethods.post,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeAnswerModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },
  updateChallengeAnswer: async (challengeAnswerId, challengeAnswerModel) => {
    let url = apiUrlTemplates.challengeAnswer(challengeAnswerId)
    let options = {
      method: httpMethods.put,
      headers: fetchHeaders.get(),
      body: JSON.stringify(challengeAnswerModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },
}
