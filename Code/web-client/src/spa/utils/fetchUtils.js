// configs
import { feloniousStatusCodes } from '../clientSideConfig' 

export const HttpMethods = {
    post: 'POST',
    get: 'GET',
    put: 'PUT',
    patch: 'PATCH',
    delete: 'DELETE'
}

export let fetchHeaders = {
    
    headers: undefined,

    get: function() {
        if(!this.headers) {
            let headers = new Headers()
            headers.append("Content-Type", "application/json")
            headers.append("Accept", "application/problem+json,application/json")
            let userString = localStorage.getItem('ISELearningLoggedUser')
            if(userString) {
                headers.append("Authorization" , JSON.parse(userString).authorization);
            }
            this.headers = headers
        }
        return this.headers
    },

    clear: function() {
        this.headers = undefined
    }

}

export const handleFetchResponse = async (response, message) => {

    let json = undefined
    
    if(response.headers.has('content-type')) {
        if (response.headers.get('content-type').startsWith('application/json')) {
            json = (response.statusText === "No Content") ? undefined : await response.json()
        }
        if(response.headers.get('content-type').startsWith('application/problem+json')) {
            json = await response.json()
        }
    }
    console.log("f", json)
    if(response.ok) {
        return {
          json: json,
          message: message ? message : undefined,
          severity: "success",
          render: true
        }
    } else {
          if(!json || feloniousStatusCodes.includes(response.status)) {
            const errorMessage = `${response.status} | ${response.statusText}`
            return {
                message: errorMessage,
                severity: "error",
                render: false
            }
          } else {
            return {
                message: json.detail,
                severity: "error",
                render: true
            }
        }
    }

}
