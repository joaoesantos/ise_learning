import {apiUrlTemplates} from '../clientSideConfig'
import {HttpMethods, defaultHeaders} from '../components/fetchUtils'

export const QuestionnairePageController = {
    getQuestionnaire: async () => {
        //let url = alterar url
        let options = {
          method: HttpMethods.get,
          headers: defaultHeaders()
        }
        //let response = await fetch(url, options)
        //return response.json()
        return {
            id: 5,
            description: "This is a questionnaire",
            challenges: [
                {//test object
                    id: 1,
                    description: "challenge 1", 
                    code: "blablabla1",
                    unitTests: "cenas",
                    output: ""
                },
                {//test object
                    id: 2,
                    description: "challenge 2", 
                    code: "blablabla2",
                    unitTests: "cenas",
                    output: ""
                },
                {//test object
                    id: 3,
                    description: "challenge 3", 
                    code: "blablabla3",
                    unitTests: "cenas",
                    output: ""
                }
            ] 
        } 
    },
    submitChallenge: async () => {
        console.log("submitChallenge")
    },
    submitQuestionnaire: async() => {
        console.log("submitQuestionnaire")
    }
}