import { apiUrlTemplates } from '../clientSideConfig'
import { HttpMethods, defaultHeaders } from '../components/fetchUtils'

const _challengeCtrl = {
    getAllChallenges: async (credentials) => {
        const headers = defaultHeaders()
        headers.append("Authorization", `Basic ${credentials}`)
        let url = apiUrlTemplates.getAllChallenges()
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return response.json()
    },
    getRamdomChallenge: async (credentials) => {
        const headers = defaultHeaders()
        headers.append("Authorization", `Basic ${credentials}`)
        let url = apiUrlTemplates.getRandomChallenge()
        let options = {
            method: HttpMethods.get,
            headers: headers
        }
        let response = await fetch(url, options)
        return response.json()
    },
}

export default _challengeCtrl