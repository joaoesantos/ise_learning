// client side configurations
import { apiVersion } from '../clientSideConfig';

export async function runCodeModel(codeLanguage, code, unitTests, executeTests) {
    const options = {
        method: "POST",
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        },
        body: JSON.stringify({
            language: codeLanguage,
            code: code,
            unitTests: unitTests,
            executeTests: executeTests
        })
    };
    let response = await fetch("v0/execute", options)
        .then((res)=> res.json()).catch((err) => { throw err });

    return response
}

export default { runCodeModel }