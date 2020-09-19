import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders, handleFetchResponse } from '../../utils/fetchUtils'
import { ChallengeController } from '../challenge/ChallengeController'

export const QuestionnaireController = {

  getAllUserQuestionnaires: async () => {
    const headers = fetchHeaders.get()
    let url = apiUrlTemplates.getAllUserQuestionnaires()
    let options = {
        method: HttpMethods.get,
        headers: headers
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

  getQuestionnaire: async (questionnaireId) => {
    let headers = fetchHeaders.get()
    let questionnaires_url = apiUrlTemplates.getQuestionnaireWithChallenges(questionnaireId)
    let options = {
      method: HttpMethods.get,
      headers: headers,
    }
    // challenges
    let response = await ChallengeController.getAllChallenges()
    if(response.severity === "error") {
      return response
    }
    let challenges = response.json.map(c => {
      return {
        id: c.challengeId,
        challengeText: c.challengeText,
        languages: c.solutions.map(s => s.codeLanguage)
      }
    })
    // questionnaire
    let questionnaire = {
      id: null,
      title: '',
      selectedChallenges: []
    }
    if(questionnaireId !== "undefined") {
      response = await fetch(questionnaires_url, options)
      let handledResponse = await handleFetchResponse(response)
      if(handledResponse.severity === "error") {
        return handledResponse
      }
      questionnaire.id = handledResponse.json.questionnaireId
      questionnaire.title = handledResponse.json.description
      questionnaire.timer = handledResponse.json.timer ? parseInt(handledResponse.json.timer)/(1000*60) : "N/A"
      questionnaire.creatorId = handledResponse.json.creatorId
      questionnaire.selectedChallenges = handledResponse.json.challenges.map(element => {
        return {
          id: element.challenge.challengeId,
          challengeText: element.challenge.challengeText,
          languages: element.challenge.solutions.map(s => s.codeLanguage),
          selectedLanguages: element.languageFilter.split(',')
        }
      })
    }
    return {
      challenges: challenges,
      questionnaire: questionnaire,
      severity: "success"
    }
  },
  
  saveQuestionnaire: async (questionnaire, credentials) => {
    let url = apiUrlTemplates.saveQuestionnaire()
    let headers = fetchHeaders.get()
    let body = {}
    let options = {
      headers: headers,
    }
  
    if(questionnaire.id){
      options.method = HttpMethods.put
      body = {
        questionnaireId: questionnaire.id,
        questionnaireModel: {
          description: questionnaire.title,
          timer: Number(questionnaire.timer),
          creatorId: questionnaire.creatorId
        }
      }
    }else{
      options.method = HttpMethods.post
      url = apiUrlTemplates.createQuestionnaire()
      body = {
        questionnaire: {
          description: questionnaire.title,
          timer: Number(questionnaire.timer)
        },
        challenges:questionnaire.selectedChallenges.map(c => {
          return {
            challengeId: c.id,
            languageFilter: c.selectedLanguages.join()
          }
        })
      }
    }
    options.body = JSON.stringify(body)
    let response = await fetch(url, options)
    let json = await response.json()
    return await this.getQuestionnaire(json.questionnaireId, credentials)
  }

}
