export const HttpMethods = {
    get: 'GET',
    put: 'PUT',
    delete: 'DELETE',
    post: 'POST'
}

export const defaultHeaders = () => {
    const headers = new Headers()
    headers.append("Content-Type", "application/json")
    //headers.append("Accept", "application/vnd.siren+json")
    //headers.append("Accept", "application/problem+json")
    return headers
}