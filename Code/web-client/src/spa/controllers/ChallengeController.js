import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'
import {LanguageController} from './LanguageController'

export const ChallengeController = {
  getChallengeById: async (challengeId) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders()
    }
    let response = await fetch(url, options)
    return response.json()
  },
  createChallenge: async (challengeModel) => {
    let url = apiUrlTemplates.challenges()
    let options = {
      method: HttpMethods.post,
      headers: defaultHeaders(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },
  getChallengeByIdAndAvailableLanguages: async (challengeId) => {
    let challengePromise = ChallengeController.getChallengeById(challengeId);
    let languagesPromise = LanguageController.getAvailableLanguages();
    let responses = await Promise.all([challengePromise, languagesPromise])
    return {
      challenge: responses[0],
      languages: responses[1]
    }
  } 
}
