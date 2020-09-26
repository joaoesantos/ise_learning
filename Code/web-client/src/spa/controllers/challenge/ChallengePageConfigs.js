// controllers
import { LanguageController } from '../LanguageController'
import { ChallengeController } from './ChallengeController'
import { ChallengeAnswerController } from './ChallengeAnswerController'
// utils
import { convertLanguagesToObjectWithLabel } from '../../utils/ChallengeUtils'
import { reduceObjectArrayToMap } from '../../utils/utils'

export const ChallengePageConfigs = (challengeId, componentAggregateStates, user) => {
    // BUTTONS - init
    if(!user){ user = JSON.parse(localStorage.getItem('ISELearningLoggedUser')) }

    let createChallenge = {
        id: "createChallenge",
        onClick: () => {
            let challengeModel = Object.assign({}, componentAggregateStates.challenge.state);
            challengeModel.creatorId = user.userId
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
                    }
                    return response
                },
                args: [challengeModel]
            });
        },
        title: "Create Challenge",
        isVisible: user !== undefined,
        disabled: componentAggregateStates.codeLanguage.state === undefined
    }
    let editChallenge = {
        id: "editChallenge",
        onClick: () => {
            componentAggregateStates.isChallengeEditable.setter(true)
        },
        title: "Edit Challenge",
        isVisible: user && !componentAggregateStates.isChallengeEditable.state && componentAggregateStates.challenge.state && componentAggregateStates.challenge.state.creatorId === user.userId
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
        isVisible: componentAggregateStates.isChallengeEditable.state && user !== undefined
    }
    let saveAnswerButton = {
        id: "saveAnswer",
        onClick: () => {
            let challengeAnswerModel = {};
            challengeAnswerModel.challengeId = Number(challengeId);
            challengeAnswerModel.answer = {
                codeLanguage: componentAggregateStates.codeLanguage.state,
                answerCode: componentAggregateStates.yourSolution.state[componentAggregateStates.codeLanguage.state].value,
                unitTests: componentAggregateStates.yourTests.state[componentAggregateStates.codeLanguage.state].value
            }
            let matchingChallengeAnswer = componentAggregateStates.challengeAnswers.state.find(ca => ca.answer.codeLanguage === componentAggregateStates.codeLanguage.state);
            if(matchingChallengeAnswer) {
                componentAggregateStates.action.setter({
                    function: async (arg1, arg2) => {
                        let newChallengeAnswer = await ChallengeAnswerController.updateChallengeAnswer(arg1, arg2);
                        if(newChallengeAnswer.severity && newChallengeAnswer.severity === 'error') {
                            return {
                              message: newChallengeAnswer.message,
                              severity: 'error'
                            }
                        }
                        return {
                            json: {
                                languages: componentAggregateStates.challengeLanguages.state,
                                challenge: componentAggregateStates.challenge.state,
                                challengeAnswers: componentAggregateStates.challengeAnswers.state.map(ca => {
                                    if(ca.answer.codeLanguage === newChallengeAnswer.json.answer.codeLanguage) {
                                        return newChallengeAnswer.json;
                                    }
                                    return ca;
                                })
                            },
                            message: newChallengeAnswer.message,
                            severity: newChallengeAnswer.severity
                        };
                    },
                    args: [matchingChallengeAnswer.challengeAnswerId, challengeAnswerModel],
                    render: true
                });
            } else {
                componentAggregateStates.action.setter({
                    function: async (arg) => {
                        let newChallengeAnswer = await ChallengeAnswerController.createChallengeAnswer(arg);
                        if(newChallengeAnswer.severity && newChallengeAnswer.severity === 'error') {
                            return newChallengeAnswer
                        }
                        componentAggregateStates.challengeAnswers.state.push(newChallengeAnswer.json)
                        return {
                            json: {
                                languages: componentAggregateStates.challengeLanguages.state,
                                challenge: componentAggregateStates.challenge.state,
                                challengeAnswers: componentAggregateStates.challengeAnswers.state
                            },
                            message: newChallengeAnswer.message,
                            severity: newChallengeAnswer.severity
                        };
                    },
                    args: [challengeAnswerModel],
                    render: true
                });
            }
        },
        title: "Save Answer",
        isVisible: user !== undefined
    }
    let deleteChallenge = {
        id: "deleteChallenge",
        onClick: () => {
            componentAggregateStates.action.setter({
                function: async (arg) => {
                    let newChallengeAnswer = await ChallengeController.deleteChallengeById(arg);
                    if(newChallengeAnswer.severity && newChallengeAnswer.severity === 'error') {
                        return {
                          message: newChallengeAnswer.message,
                          severity: 'error'
                        }
                    }
                    componentAggregateStates.redirectObject.setter({
                            pathname: `listChallenges`
                    })
                    return newChallengeAnswer;
                },
                args: [challengeId],
                render: true
            });
            componentAggregateStates.isChallengeEditable.setter(false)
        },
        title: "Delete Challenge",
        isVisible: componentAggregateStates.isChallengeEditable.state && user !== undefined
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
                if(response.challengeAnswers !== undefined && response.challengeAnswers.length > 0) {
                    componentAggregateStates.challenge.setter(response.challenge)
                    componentAggregateStates.challengeAnswers.setter(response.challengeAnswers)
                    componentAggregateStates.challengeLanguages.setter(convertLanguagesToObjectWithLabel(response.challenge.solutions.map(s => s.codeLanguage)))
                    componentAggregateStates.codeLanguage.setter(response.challengeAnswers[0].answer.codeLanguage)
                    componentAggregateStates.isChallengeEditable.setter(false)
                    let yourSolution = reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "challengeCode");
                    let yourTests = reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "unitTests");
                    response.challengeAnswers.forEach(e => {
                        yourSolution[e.answer.codeLanguage] = { value: e.answer.answerCode }
                        yourTests[e.answer.codeLanguage] = { value: e.answer.unitTests }
                    });
                    componentAggregateStates.yourSolution.setter(yourSolution)
                    componentAggregateStates.yourTests.setter(yourTests)
                    componentAggregateStates.ourSolution.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "solutionCode", "solutionId"))
                    componentAggregateStates.ourTests.setter(reduceObjectArrayToMap(response.challenge.solutions, "codeLanguage", "unitTests"))
                } else {
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
                }
            },
            pageLoadingAction: () => {return {
                function: ChallengeController.getChallengeByIdAvailableLanguagesAndChallengeAnswerIfExists,
                args: [challengeId, user ? user.userId : undefined],
                render: true
            }},
            headerButtons: [saveAnswerButton, editChallenge, saveChallenge, deleteChallenge]
        }
    }
}