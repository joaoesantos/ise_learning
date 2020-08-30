export const HttpMethods = {
    post: 'POST',
    get: 'GET',
    put: 'PUT',
    delete: 'DELETE'
}

export const defaultHeaders = () => {
    const headers = new Headers()
    headers.append("Content-Type", "application/json")
    //headers.append("Accept", "application/problem+json")
    return headers
}

export let fetchHeaders = {
    
    headers: undefined,

    get: function() {
        if(!this.headers) {
            let headers = new Headers()
            headers.append("Content-Type", "application/json")
            headers.append("Accept", "application/problem+json")
            this.headers = headers
        }
        return this.headers
    },

    append: function({ key, value }) {
        if(!this.headers) { this.get() }
        this.headers.append(`${key}`,`${value}`)
    }

}