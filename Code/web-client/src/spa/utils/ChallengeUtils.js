import { languageLabelMappings } from '../clientSideConfig'
import { RunCodeController } from '../controllers/RunCodeController'

export const genericRunCodeAction = (setAction, codeLanguage, code, unitTests, executeTests, textSufix) => {
    return {
        id: "executeCode",
        title: "Run Code",
        function: async () =>
            setAction({
                function: RunCodeController.execute,
                args: [{
                    language: codeLanguage,
                    code: code,
                    unitTests: unitTests,
                    executeTests: executeTests
                }],
                name: 'runcode',
                textSufix: textSufix
        })
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