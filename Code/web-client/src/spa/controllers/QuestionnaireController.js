import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

export const QuestionnaireController = {
  getQuestionnaire: async (id) => {
    //get challenges
    const challenges = [
        {
            id: 1,
            title: 'cenas1',
            tags: 'a, b, c'
        },
        {
            id: 2,
            title: 'cenas2',
            tags: 'a, b, dad'
        },
        {
            id: 3,
            title: 'cenas3',
            tags: 'a, b, oiopq'
        }
    ]
    let questionnaire = {
        id: null,
        title: '',
        language:'',
        selectedChallenges: []
    }
    if(id){
        let url = apiUrlTemplates.getQuestionnaire()
        url = url.replace(":id", id)
        let options = {
          method: HttpMethods.get,
          headers: defaultHeaders(),
        }
        //let response = await fetch(url, options)
        //return response.json()

        questionnaire.id = id
        questionnaire.title = 'This is a title'
        questionnaire.java = 'javascript'
        questionnaire.selectedChallenges = [
            {
                id: 3,
                title: 'cenas3',
                tags: 'a, b, oiopq',
                selectedLanguages: 'javascript'
            }
        ]
    }

    return Promise.resolve({
        questionnaire: questionnaire,
        challenges: challenges
    })
  },
  
  saveQuestionnaire: async (questionnaire) => {
    let url = apiUrlTemplates.saveQuestionnaire()
    let options = {
      method: HttpMethods.post,
      headers: defaultHeaders(),
      body: JSON.stringify({})//corrigir body depois de fazer eebase com serviços
    }
    //let response = await fetch(url, options)
    //return response.json()
    console.log('saveQuestionnaire')
    return Promise.resolve({
        id: 2,
        title: questionnaire.title,
        language:questionnaire.language,
        selectedChallenges: questionnaire.challenges
    })
  }
}

