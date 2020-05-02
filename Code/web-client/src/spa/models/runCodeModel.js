// client side configurations
import { apiBaseUrl } from '../clientSideConfig';

export function runCodeModel(codeLanguage, code) {
    // const options = {
    //     method: "POST",
    //     headers : {
    //         "Content-Type" : "application/json",
    //         "Accept" : "application/json"
    //     },
    //     body: JSON.stringify({
    //         environment: codeLanguage,
    //         code: code,
    //         unitTests:'',
    //         executeTests: false
    //     })
    // };
    // fetch(`${apiBaseUrl}/runCode`, options)
    // .then((res)=> {
    //     if (res.ok)
    //         return res.json();
    //     else 
    //         return res.status;
    // }).catch((err) => { throw err });
    return({
        environment: codeLanguage,
        executionTime : 15,
        result : code
    })
}

export default { runCodeModel }