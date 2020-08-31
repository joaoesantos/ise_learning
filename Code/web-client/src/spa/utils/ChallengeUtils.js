import { languageLabelMappings } from '../clientSideConfig'
import { RunCodeController } from '../controllers/RunCodeController'

export const genericRunCodeAction = (code, unitTests, executeTests, textSufix, setOutputText, codeLanguage) => {
    return {
        id: "executeCode",
        title: "Run Code",
        function: async () => {
            let result = await RunCodeController.execute({
                language: codeLanguage,
                code: code,
                unitTests: unitTests,
                executeTests: executeTests
            });
            result.textSufix = textSufix;
            setOutputText({ value: result, toUpdate: true });
        }
    }
}

export const genericSetTextEditorData = (setter, fullValue, codeLanguage) => {
    return (newValue) => {
        if(codeLanguage) {
            if(!fullValue[codeLanguage]) {
                fullValue[codeLanguage] = {};
            }
            fullValue[codeLanguage].value = newValue;
            setter(Object.assign({}, fullValue))
        }
    }
}

export const convertLanguagesToObjectWithLabel = (languages) => {
    return languages.map(l => {
        return {
            value: l,
            label: languageLabelMappings[l]
        }
    })
}