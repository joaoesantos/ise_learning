// client side configurations
import { apiVersion } from '../clientSideConfig';

export async function runCodeModel(codeLanguage, code) {
    const options = {
        method: "POST",
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        },
        body: JSON.stringify({
            language: codeLanguage,
            code: code,
            unitTests:'',
            executeTests: false
        })
    };
    let response = await fetch(`${apiVersion}/execute`, options)
        .then((res)=> res.json()).catch((err) => { throw err });

    return({
        environment: codeLanguage,
        executionTime : 15,
        result : response.rawResult
    })
}

export default { runCodeModel }