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
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed venenatis mi elit, sit amet pulvinar lacus convallis eu. Aliquam dolor mauris, gravida ac nulla ut, malesuada interdum elit. Nulla varius iaculis massa, id dignissim quam aliquet eu. Sed vulputate nisl vitae eros sodales egestas. Duis viverra sapien metus, eleifend dictum nibh bibendum imperdiet. Suspendisse felis neque, vehicula eu felis non, bibendum vehicula sem. Aenean quis nibh a nulla venenatis elementum et eu libero. In pharetra volutpat nibh non dapibus. Duis sodales est vitae eros blandit, et viverra eros consequat. Etiam est nibh, vulputate quis lobortis at, eleifend tincidunt mi. Vestibulum odio mauris, finibus at sapien sit amet, pharetra ultrices risus. Vivamus imperdiet viverra gravida. Vivamus dignissim tincidunt ornare. ", 
                    code: "blablabla1",
                    unitTests: "cenas",
                    output: ""
                },
                {//test object
                    id: 2,
                    description: "Nulla sit amet lobortis risus, a iaculis augue. Ut gravida justo non tortor blandit, feugiat hendrerit lectus facilisis. Aliquam erat volutpat. Mauris porta est vel hendrerit molestie. Integer pellentesque blandit pretium. Integer vel iaculis libero. Aenean bibendum lacus nec nisi mattis, et aliquet tortor lacinia. Nam vestibulum efficitur congue. Nunc viverra felis vitae erat ultricies commodo. Phasellus accumsan, leo non facilisis posuere, tortor nisl porta eros, in fringilla nibh neque in risus. ", 
                    code: "blablabla2",
                    unitTests: "cenas",
                    output: ""
                },
                {//test object
                    id: 3,
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam commodo metus at orci vestibulum blandit. Mauris eget facilisis purus. Cras vestibulum eleifend tellus eget feugiat. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed viverra lectus eu pellentesque vestibulum. Integer tempor mauris id dapibus ornare. Suspendisse tristique cursus lectus, elementum interdum lacus posuere in. Aliquam non tempus orci. Sed in dolor auctor, vulputate nunc eget, sollicitudin leo. Duis eget metus neque. Quisque accumsan lobortis congue. Vivamus ex urna, pharetra sed pretium eu, viverra ut libero. In interdum nulla sit amet velit malesuada varius. Proin at sapien nibh. Duis ac metus ultrices, facilisis tortor eget, faucibus enim. ", 
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