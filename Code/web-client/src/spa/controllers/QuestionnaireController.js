import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../components/fetchUtils'

export const QuestionnaireController = {
  getQuestionnaire: async (id) => {
    //get challenges
    let challengesUrl = apiUrlTemplates.getChallenges()
    let options = {
      method: HttpMethods.get,
      headers: defaultHeaders(),
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
    console.log('challenges:', challenges)
    // const challenges = [
    //   {
    //     id: 1,
    //     title: 'cenas1',
    //     tags: 'a, b, c',
    //     languages: ['java', 'javascript']
    //   },
    //   {
    //     id: 2,
    //     title: 'cenas2',
    //     tags: 'a, b, dad',
    //     languages: ['csharp', 'python']
    //   },
    //   {
    //     id: 3,
    //     title: 'cenas3',
    //     tags: 'a, b, oiopq',
    //     languages: ['kotlin']
    //   }
    // ]
    let questionnaire = {
      id: null,
      title: '',
      selectedChallenges: []
    }
    if (id) {
      let url = apiUrlTemplates.getQuestionnaire()
      url = url.replace("{id}", id)

      let response = await fetch(url, options)
      let json = response.json()
      console.log('json:', json)
      questionnaire.title = json.description
      questionnaire.id = json.questionnaireId
      questionnaire.timer = json.timer

      let selectedChallengesUrl = apiUrlTemplates.getQuestionnaireChallenges()
      selectedChallengesUrl = selectedChallengesUrl.replace('{questionnaireId}', id)
      console.log('selectedChallengesUrl:', selectedChallengesUrl)
      let selectedChallengesResponse = await fetch(selectedChallengesUrl, options)
      let selectedChallengesJson = await selectedChallengesResponse.json()
      // questionnaire.id = id
      // questionnaire.title = 'This is a title'
      // questionnaire.selectedChallenges = [
      //   {
      //     id: 3,
      //     title: 'cenas3',
      //     tags: 'a, b, oiopq',
      //     selectedLanguages: ['javascript']
      //   }
      // ]
      questionnaire.selectedChallenges = selectedChallengesJson
      console.log('selectedChallenges:', selectedChallengesJson)
    }

    return {
      questionnaire: questionnaire,
      challenges: challenges
    }
  },

  saveQuestionnaire: async (questionnaire) => {
    // let url = apiUrlTemplates.saveQuestionnaire()
    // let options = {
    //   method: HttpMethods.post,
    //   headers: defaultHeaders(),
    //   body: JSON.stringify(questionnaire)//corrigir body depois de fazer eebase com serviços
    // }
    // let response = await fetch(url, options)
    // return response.json()
    return Promise.resolve({
      id: 2,
      title: questionnaire.title,
      language: questionnaire.language,
      selectedChallenges: questionnaire.challenges
    })
  }
}

