import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchUtils } from '../components/fetchUtils'
import { LanguageController } from './LanguageController'

export default ChallengeController = {

  getAllChallenges: async () => {
    let url = apiUrlTemplates.getAllChallenges()
    let options = {
        method: HttpMethods.get,
        headers: fetchHeaders.get()
    }
    let response = await fetch(url, options)
    return response.json()
  },

  getRandomChallenge: async () => {
      let url = apiUrlTemplates.getRandomChallenge()
      let options = {
          method: HttpMethods.get,
          headers: fetchHeaders.get()
      }
      let response = await fetch(url, options)
      return response.json()
  },

  getChallengeById: async (challengeId) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.get,
      headers: fetchUtils.get()
    }
    let response = await fetch(url, options)
    return response.json()
  },

  createChallenge: async (challengeModel) => {
    let url = apiUrlTemplates.challenges()
    let options = {
      method: HttpMethods.post,
      headers: fetchUtils.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    return response.json();
  },

  updateChallenge: async (challengeId, challengeModel) => {
    let url = apiUrlTemplates.challenge(challengeId)
    let options = {
      method: HttpMethods.put,
      headers: fetchUtils.get(),
      body: JSON.stringify(challengeModel)
    }
    let response = await fetch(url, options)
    return {challenge: await response.json()};
  },

  getChallengeByIdAndAvailableLanguages: async (challengeId) => {
    let challengePromise = ChallengeController.getChallengeById(challengeId);
    let languagesPromise = LanguageController.getAvailableLanguages();
    let responses = await Promise.all([challengePromise, languagesPromise])
    return {
      languages: responses[1],
      challenge: responses[0]
    }
  }

}
