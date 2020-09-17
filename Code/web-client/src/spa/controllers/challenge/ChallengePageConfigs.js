// controllers
import { LanguageController } from '../LanguageController'
import { ChallengeController } from './ChallengeController'
import { ChallengeAnswerController } from './ChallengeAnswerController'
// utils
import { convertLanguagesToObjectWithLabel } from '../../utils/ChallengeUtils'
import { reduceObjectArrayToMap } from '../../utils/utils'

export const ChallengePageConfigs = (challengeId, userId, componentAggregateStates, user) => {
    //BUTTONS - init
    let saveChallengeAsAnswerButton = {
        id: "saveChallengeAsAnswerButton",
        onClick: () => {
            let challengeAnswerModel = {};
            challengeAnswerModel.challengeId = Number(challengeId);
            challengeAnswerModel.userId = user ? user.userId : undefined;
            challengeAnswerModel.answer = {
                codeLanguage: componentAggregateStates.codeLanguage.state,
                answerCode: componentAggregateStates.yourSolution.state[componentAggregateStates.codeLanguage.state].value,
                unitTests: componentAggregateStates.yourTests.state[componentAggregateStates.codeLanguage.state].value,
                isCorrect: false,
            }
            componentAggregateStates.action.setter({
                function: async (arg) => {
                    let newChallengeAnswer = await ChallengeAnswerController.createChallengeAnswer(arg);
                    componentAggregateStates.redirectObject.setter({
                            pathname: `/challengeAnswers/${newChallengeAnswer.challengeId}/answers/users/${newChallengeAnswer.userId}`
                    })
                    return newChallengeAnswer;
                },
                args: [challengeAnswerModel]
            });
        },
        title: "Submit Answer",
        isVisible: user != undefined
    }
    let createChallenge = {
        id: "createChallenge",
        onClick: () => {
            let challengeModel = Object.assign({}, componentAggregateStates.challenge.state);
            challengeModel.creatorId = user ? user.userId : undefined;
            challengeModel.solutions = componentAggregateStates.challengeLanguages.state.map(l => {
                return {
                    challengeCode: componentAggregateStates.yourSolution.state[l.value] ? componentAggregateStates.yourSolution.state[l.value].value : "",
                    codeLanguage: l.value,
                    solutionCode: componentAggregateStates.ourSolution.state[l.value] ? componentAggregateStates.ourSolution.state[l.value].value : "",
                    unitTests: componentAggregateStates.ourTests.state[l.value] ? componentAggregateStates.ourTests.state[l.value].value : ""
                }
            });
            componentAggregateStates.action.setter({
                function: async (arg) => {
                    let response = await ChallengeController.createChallenge(arg);
                    if(response.severity === "success") {
                        componentAggregateStates.redirectObject.setter({
                            pathname: `/challenges/${response.json.challengeId}`
                        })
                        return response.json;
                    }
                },
                args: [challengeModel]
            });
        },
        title: "Create Challenge",
        isVisible: user != undefined
    }
    console.log(componentAggregateStates.challenge.creatorId, userId )
    let editChallenge = {
        id: "editChallenge",
        onClick: () => {
            componentAggregateStates.isChallengeEditable.setter(true)
        },
        title: "Edit Challenge",
        isVisible: !componentAggregateStates.isChallengeEditable.state && componentAggregateStates.challenge.creatorId === userId  // tem de ser creatorId == user.userId -> esta tudo a null no inicio...
    }
    let saveChallenge = {
        id: "saveChallenge",
        onClick: () => {
            let challengeModel = Object.assign({}, componentAggregateStates.challenge.state);
            challengeModel.solutions = componentAggregateStates.challengeLanguages.state.map(l => {
                let res = {
                    challengeCode: componentAggregateStates.yourSolution.state[l.value] ? componentAggregateStates.yourSolution.state[l.value].value : "",
                    codeLanguage: l.value,
                    solutionCode: componentAggregateStates.ourSolution.state[l.value] ? componentAggregateStates.ourSolution.state[l.value].value : "",
                    unitTests: componentAggregateStates.ourTests.state[l.value] ? componentAggregateStates.ourTests.state[l.value].value : ""
                }
                let solutionId = componentAggregateStates.ourSolution.state[l.value] ? componentAggregateStates.ourSolution.state[l.value].id : undefined;
                if(solutionId) {
                    res.solutionId = solutionId;
                }
                return res;
            });
            delete challengeModel.challengeId;
            componentAggregateStates.action.setter({
                function: ChallengeController.updateChallenge,
                args: [challengeId, challengeModel],
                render: true
            });
            componentAggregateStates.isChallengeEditable.setter(false)
        },
        title: "Save Challenge",
        isVisible: componentAggregateStates.isChallengeEditable.state && user != undefined
    }
    let saveChallengeAnswer = {
        id: "saveChallengeAnswer",
        onClick: () => {
            let challengeAnswerModel = {};
            challengeAnswerModel.challengeId = Number(challengeId);
            challengeAnswerModel.userId = user ? user.userId : undefined;
            challengeAnswerModel.answer = {
                codeLanguage: componentAggregateStates.codeLanguage.state,
                answerCode: componentAggregateStates.yourSolution.state[componentAggregateStates.codeLanguage.state].value,
                unitTests: componentAggregateStates.yourTests.state[componentAggregateStates.codeLanguage.state].value,
                isCorrect: false,
            }
            componentAggregateStates.action.setter({
                    function: ChallengeAnswerController.updateChallengeAnswer,
                    args: [componentAggregateStates.challengeAnswer.state.challengeAnswerId, challengeAnswerModel],
                    render: false
            });
        },
        title: "Save Challenge Answer",
        isVisible: user != undefined
    }
    //BUTTONS - end

    return {
        newChallenge : {
            showYourTests: false,
            renderizationFunction: (response) => {
                componentAggregateStates.isChallengeEditable.setter(true)
                componentAggregateStates.availableLanguages.setter(convertLanguagesToObjectWithLabel(response.map(s => s.codeLanguage)))
                componentAggregateStates.challenge.setter({
                    challengeText: "Insert Challenge text here.",
                    isPrivate: false,
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
                if(response.languages) {
                    componentAggregateStates.availableLanguages.setter(convertLanguagesToObjectWithLabel(response.languages.map(s => s.codeLanguage)))
                }
                componentAggregateStates.isChallengeEditable.setter(false)
                componentAggregateStates.yourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "challengeCode"))
                componentAggregateStates.ourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "solutionCode", "solutionId"))
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
                componentAggregateStates.challenge.setter(response.challenge)
                componentAggregateStates.challengeAnswer.setter(response.challengeAnswer)
                componentAggregateStates.challengeLanguages.setter(convertLanguagesToObjectWithLabel(response.challenge.solutions.map(s => s.codeLanguage)))
                componentAggregateStates.codeLanguage.setter(response.challengeAnswer.answer.codeLanguage)
                componentAggregateStates.isChallengeEditable.setter(false)
                let yourSolution = {};
                yourSolution[response.challengeAnswer.answer.codeLanguage] = { value: response.challengeAnswer.answer.answerCode }
                componentAggregateStates.yourSolution.setter(yourSolution)
                componentAggregateStates.ourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "solutionCode", "solutionId"))
                let yourTests = {};
                yourTests[response.challengeAnswer.answer.codeLanguage] = { value: response.challengeAnswer.answer.unitTests }
                componentAggregateStates.yourTests.setter(yourTests)
                componentAggregateStates.ourTests.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "unitTests"))
            },
            pageLoadingAction: () => {return {
                function: ChallengeAnswerController.getChallengeAndChallengeAnswerBychallengeIdAndUserId,
                args: [challengeId, userId],
                render: true
            }},
            headerButtons: [saveChallengeAnswer]
        }
    }
}