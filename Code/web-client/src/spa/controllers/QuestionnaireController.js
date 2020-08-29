import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../components/fetchUtils'

const getQuestionnaire = async function(id, credentials) {
  //get challenges
  let challengesUrl = apiUrlTemplates.getChallenges()
  let headers = defaultHeaders()
  headers.append('Authorization', `Basic ${credentials}`)
  let options = {
    method: HttpMethods.get,
    headers: headers,
  }
  let challengesResponse = await fetch(challengesUrl, options)
  let challenges = await challengesResponse.json()
  const availableLanguage = challenges.map(s => s.codeLanguage)
  challenges = challenges.map(c => {
    return {
      id: c.challengeId,
      challengeText: c.challengeText,
      languages: c.solutions.map(s => s.codeLanguage)
    }
  })
  let questionnaire = {
    id: null,
    title: '',
    selectedChallenges: []
  }
  if (id) {
    let url = apiUrlTemplates.getQuestionnaireWithChallenges()
    url = url.replace("{questionnaireId}", id)
    let response = await fetch(url, options)
    let json = await response.json()
    questionnaire.id = json.questionnaireId
    questionnaire.title = json.description
    questionnaire.timer = json.timer
    questionnaire.creatorId = json.creatorId
    questionnaire.selectedChallenges = json.challenges.map(element => {
      return {
        id: element.challenge.challengeId,
        challengeText: element.challenge.challengeText,
        languages: element.challenge.solutions.map(s => s.codeLanguage),
        selectedLanguages: element.languageFilter.split(',')
      }
    })
  }
  return {
    questionnaire: questionnaire,
    challenges: challenges
  }
}

const saveQuestionnaire = async function(questionnaire, credentials) {
  let url = apiUrlTemplates.saveQuestionnaire()
  let headers = defaultHeaders()
  headers.append('Authorization', `Basic ${credentials}`)
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
  return await getQuestionnaire(json.questionnaireId, credentials)
}

export const QuestionnaireController = {
  getQuestionnaire: getQuestionnaire ,
  saveQuestionnaire: saveQuestionnaire
}

