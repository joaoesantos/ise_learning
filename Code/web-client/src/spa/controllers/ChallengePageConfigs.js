import {convertLanguagesToObjectWithLabel} from '../utils/ChallengeUtils'
import {reduceObjectArrayToMap} from '../utils/utils'
// controllers
import { ChallengeController } from '../controllers/ChallengeController'
import { LanguageController } from '../controllers/LanguageController'

export const ChallengePageConfigs = (challengeId, userId, componentAggregateStates) => {
    //BUTTONS - init
    let saveChallengeAsAnswerButton = {
        id: "saveChallengeAsAnswerButton",
        onClick: () => alert("not implemented"),
        title: "Save Challenge's Answer",
        isVisible: true
    }
    let createChallenge = {
        id: "createChallenge",
        onClick: () => {
            let challengeModel = Object.assign({}, componentAggregateStates.challenge.state);
            challengeModel.creatorId = 1;//TODO harcoded value to be changed once authentication is integrated
            challengeModel.solutions = componentAggregateStates.challengeLanguages.state.map(l => {
                return {
                    challengeCode: componentAggregateStates.yourSolution.state[l.value] ? componentAggregateStates.yourSolution.state[l.value] : "",
                    codeLanguage: l.value,
                    solutionCode: componentAggregateStates.ourSolution.state[l.value] ? componentAggregateStates.ourSolution.state[l.value] : "",
                    unitTests: componentAggregateStates.ourTests.state[l.value] ? componentAggregateStates.ourTests.state[l.value] : ""
                }
            });
            componentAggregateStates.action.setter({
                function: async (arg) => {
                    let newChallenge = await ChallengeController.createChallenge(arg);
                    componentAggregateStates.redirectObject.setter({
                            pathname: `/challenges/${newChallenge.challengeId}`
                    })
                    return newChallenge;
                },
                args: [challengeModel]
            });
        },
        title: "Create Challenge",
        isVisible: true
    }
    let editChallenge = {
        id: "editChallenge",
        onClick: () => {
            componentAggregateStates.isChallengeEditable.setter(true)
        },
        title: "Edit Challenge",
        isVisible: !componentAggregateStates.isChallengeEditable.state
    }
    let saveChallenge = {
        id: "saveChallenge",
        onClick: () => {
            componentAggregateStates.isChallengeEditable.setter(false)
        },
        title: "Save Challenge",
        isVisible: componentAggregateStates.isChallengeEditable.state
    }
    //BUTTONS - end

    return {
        newChallenge : {
            showYourTests: false,
            renderizationFunction: (response) => {
                componentAggregateStates.isChallengeEditable.setter(true)
                componentAggregateStates.availableLanguages.setter(convertLanguagesToObjectWithLabel(response.map(s => s.codeLanguage)))
                componentAggregateStates.challenge.setter({
                    challengeText: "Insert Challenge text her.",
                    isPrivate: true,
                    solutions: []
                })
            },
            pageLoadingAction: () => {return {
                function: LanguageController.getAvailableLanguages,
                args: [],
                render: true
            }},
            headerButtons: [createChallenge]
        },
        challenge : {
            showYourTests: true,
            renderizationFunction: (response) => {
                componentAggregateStates.challenge.setter(response.challenge)
                componentAggregateStates.codeLanguage.setter(response.challenge.solutions[0].codeLanguage)
                componentAggregateStates.challengeLanguages.setter(convertLanguagesToObjectWithLabel(response.challenge.solutions.map(s => s.codeLanguage)))
                componentAggregateStates.availableLanguages.setter(convertLanguagesToObjectWithLabel(response.languages.map(s => s.codeLanguage)))
                componentAggregateStates.isChallengeEditable.setter(false)
                componentAggregateStates.yourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "challengeCode"))
                componentAggregateStates.ourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "solutionCode"))
                componentAggregateStates.yourTests.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "unitTests"))
                componentAggregateStates.ourTests.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "unitTests"))
            },
            pageLoadingAction: () => {return {
                function: ChallengeController.getChallengeByIdAndAvailableLanguages,
                args: [challengeId],
                render: true
            }},
            headerButtons: [saveChallengeAsAnswerButton, editChallenge, saveChallenge]
        },
        challengeAnswer: {
            showYourTests: true,
            renderizationFunction: (response) => {
                componentAggregateStates.isChallengeEditable.setter(false)
            },
            pageLoadingAction: () => {},
            headerButtons: []
        }
    }
}