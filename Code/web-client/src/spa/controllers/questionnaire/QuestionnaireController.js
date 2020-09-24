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
      id: undefined,
      title: '',
      selectedChallenges: []
    }
    if(questionnaireId !== "undefined") {
      response = await fetch(questionnaires_url, options)
      let handledResponse = await handleFetchResponse(response)
      if(handledResponse.severity === "error") {
        return handledResponse
      }

      console.log("handledResponse.json.timer",handledResponse.json.timer, handledResponse)
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
  
  saveQuestionnaire: async (questionnaire) => {
    let url = apiUrlTemplates.saveQuestionnaire(questionnaire.id)
    let headers = fetchHeaders.get()
    let body = {
      questionnaire: {
        description: questionnaire.title,
        timer: Number(questionnaire.timer)*(1000*60)
      },
      challenges:questionnaire.selectedChallenges.map(c => {
        return {
          challengeId: c.id,
          languageFilter: c.selectedLanguages.join()
        }
      })
    }
    let options = {
      headers: headers,
      body: JSON.stringify(body)
    }
    let message = ""
    if(questionnaire.id) {
      options.method = HttpMethods.put
      message = "Questionnaire updated successfully!"
    } else {
      url = apiUrlTemplates.createQuestionnaire()
      options.method = HttpMethods.post
      message = "Questionnaire created successfully!"
    }
    let response = await fetch(url, options)
    let handledResponse = await handleFetchResponse(response)
    if(handledResponse.severity === "error") {
      return handledResponse
    } else {
      let secondResponse = await QuestionnaireController.getQuestionnaire(handledResponse.json.questionnaireId)
      secondResponse.message = message
      return secondResponse
    }
  },

  deleteQuestionnaire: async (questionnaireId) => {
    const headers = fetchHeaders.get()
    let url = apiUrlTemplates.deleteQuestionnaire(questionnaireId)
    let options = {
        method: HttpMethods.delete,
        headers: headers
    }
    let response = await fetch(url, options)
    return handleFetchResponse(response)
  },

}
