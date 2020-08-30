import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, fetchHeaders } from '../utils/fetchUtils'

const _challengeCtrl = {
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
}

export default _challengeCtrl