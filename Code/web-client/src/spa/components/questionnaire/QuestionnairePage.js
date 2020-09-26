// react
import React from 'react'
import { useParams } from 'react-router-dom'
// material-ui components
import Box from '@material-ui/core/Box'
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import Container from '@material-ui/core/Container'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Grid from '@material-ui/core/Grid'
import Paper from '@material-ui/core/Paper'
import Select from '@material-ui/core/Select'
import Step from '@material-ui/core/Step'
import StepLabel from '@material-ui/core/StepLabel'
import Stepper from '@material-ui/core/Stepper'
import { makeStyles } from '@material-ui/core/styles'
import TextField from '@material-ui/core/TextField'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
// custom components
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// notifications
import CircularProgress from '../notifications/CircularProgress'
import CustomizedSnackbars from '../notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnairePageController } from '../../controllers/questionnaire/QuestionnairePageController'
import { RunCodeController } from '../../controllers/RunCodeController'
// context
import { ThemeContext } from '../../context/ThemeContext'
// utils
import { CodeMirrorOptions, defaultUnitTests } from '../../clientSideConfig'

import blue from '@material-ui/core/colors/blue'

const useStyles = makeStyles((theme) => ({
    appBar: {
        position: 'relative',
    },
    layout: {
        width: 'auto',
        height: "80%",
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(2) * 2)]: {
            width: '80%',
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        marginTop: theme.spacing(3),
        marginBottom: theme.spacing(1),
        padding: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(3) * 2)]: {
            marginBottom: theme.spacing(6),
            padding: theme.spacing(3),
        },
    },
    stepper: {
        padding: theme.spacing(3, 0, 5),
    },
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    button: {
        marginTop: theme.spacing(3),
        marginLeft: theme.spacing(1),
    },
    runButton: {
        margin: theme.spacing(1),
        textTransform: "none",
        color: '#ffffff',
        backgroundColor: '#5cb85c',
        '&:hover': {
            backgroundColor: '#17b033',
        }
    },
    submitButton: {
        margin: theme.spacing(1),
        textTransform: "none",
    },
    questionnaireToolbar: {
        fontFamily: 'sans-serif',
        color: '#fff',
        display: 'inline-block',
        display: 'flex',
        justifyContent: 'flex-end',
        textAlign: 'center',
        fontSize: '12px',
    },
    questionnaireToolbarElement: {
        padding: '5px',
        borderRadius: '3px',
        display: 'inline-block',
        marginRight: theme.spacing(2)
    },
    timerElement: {
        padding: '5px',
        borderRadius: '3px',
        display: 'inline-block',
        marginRight: theme.spacing(2),
        marginTop: theme.spacing(3),
        background: blue[600],
    },
    timerValue: {
        padding: '2px',
        borderRadius: '3px',
        background: blue[900],
        display: 'inline-block'
    },
    timerText: {
        fontSize: '10px'
    },
    runStatePaper: {
        elevation:1,
        borderStyle:'solid',
    },
}));

export default function QuestionnairePage() {

    const classes = useStyles()
    const { theme } = React.useContext(ThemeContext)
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [runState, setRunState] = React.useState('notRunning')
    const [timer, setTimer] = React.useState()
    const [completed, setCompleted] = React.useState(new Set())
    const [questionnaire, setQuestionnaire] = React.useState()
    const [activeStep, setActiveStep] = React.useState(0)
    const [codeLanguage, setCodeLanguage] = React.useState()
    const [activeChallenge, setActiveChallenge] = React.useState()
    const [runTests, setRunTests] = React.useState(false)
    const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false })

    const { uuid } = useParams()

    let seconds = undefined, minutes = undefined, hours = undefined

    React.useEffect(() => {
        // React.state evaluation for run code button action
        if (action && action.name && action.name === "runcode") {
            if(actionState === ActionStates.inProgress) {
                setRunState('running')
            } else if(actionState === ActionStates.done && response.severity === "success") {
            response.json.wasError ? setRunState('error') : setRunState('finished')
                setTextArea({ value: response.json, toUpdate: true })
            }
        // React.state evaluation to set questionnaire
        } else if (action && action.name && action.name === "getQuestionnaireByUuid" && actionState === ActionStates.done && response.severity === "success") {
            const initChallenge = response.json.challenges[0]
            const initialLanguage = response.json.challenges[0].answer.codeLanguage || initChallenge.languages[0]
            initChallenge.answer.codeLanguage = initialLanguage
            initChallenge.answer.unitTests = defaultUnitTests[initialLanguage]
            setQuestionnaire(response.json)
            setTimer( response.json.timer === null ? null : (response.json.startTimestamp + response.json.timer) - Date.now() )
            setActiveChallenge(initChallenge)
            setCodeLanguage(initChallenge.codeLanguage || initChallenge.languages && initChallenge.languages[0])
        // React.state evaluation for action.clear
        } else if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnairePageController.getQuestionnaire,
                args: [uuid],
                name: "getQuestionnaireByUuid"
            })
        }
    }, [actionState]);

    if(timer !== null) {
        seconds = ("0" + (Math.floor((timer / 1000) % 60) % 60)).slice(-2);
        minutes = ("0" + Math.floor((timer / 60000) % 60)).slice(-2);
        hours = ("0" + Math.floor((timer / 3600000) % 60)).slice(-2)
    }

    React.useEffect(() => {
        if(timer !== null) {
            let intervalId = null
            if (timer > 0) {
                intervalId = setInterval(() => setTimer(old => old - 1000), 1000);
            } else {
                setAction({
                    function: QuestionnairePageController.getQuestionnaire,
                    args: [uuid],
                    name: "getQuestionnaireByUuid"
                })
            }
            return () => {
                clearInterval(intervalId)
            }
        }
    }, [timer])

    const deepCopy = (obj) => {
        return (JSON.parse(JSON.stringify(obj)));
    }

    const onLanguageChange = (event) => {
        onClearConsole()
        let clone = deepCopy(questionnaire)
        let challenge = clone.challenges[activeStep]
        challenge.answer.codeLanguage = event.target.value
        challenge.answer.answerCode = CodeMirrorOptions.get(event.target.value).value
        challenge.answer.unitTests = defaultUnitTests[event.target.value]
        setActiveChallenge(challenge)
        setCodeLanguage(event.target.value)
    }

    const onRunCode = async () => {
        if (runState !== 'running') {
            setRunState('running')
            let result = await RunCodeController.execute({
                language: codeLanguage,
                code: activeChallenge.answer.answerCode,
                unitTests: activeChallenge.answer.unitTests,
                executeTests: runTests
            });
            setTextArea({ value: result.json, toUpdate: true })
            setRunState('finished');
        }
    }

    // const onRunCode = async () => {
    //     if (runState !== 'running') {
    //         setRunState('running')
    //         setAction({
    //             function: RunCodeController.execute,
    //             args: [{
    //                 language: codeLanguage,
    //                 code: activeChallenge.answer.answerCode,
    //                 unitTests: unitTests,
    //                 executeTests: runTests
    //             }],
    //             name: 'runcode'
    //         })
    //     }
    // }

    const onClearConsole = () => {
        if (runState !== 'notRunning') {
            setRunState('notRunning')
            setTextArea('cls')
        }
    }

    const handleSaveAnswer = () => {
        let clone = deepCopy(questionnaire)
        clone.challenges[activeStep] = activeChallenge
        setQuestionnaire(clone)

        const newCompleted = new Set(completed)
        newCompleted.add(activeStep)
        setCompleted(newCompleted)
    }

    const setCodeArea = (text) => {
        let clone = deepCopy(activeChallenge)
        clone.answer.answerCode = text
        setActiveChallenge({...clone})
    }

    const getCodeArea = (idx) => {
        let clone = deepCopy(questionnaire)
        const selectedAnswer = clone.challenges[idx].answer
        let codeText = selectedAnswer.answerCode
        if (!codeText) {
            codeText = CodeMirrorOptions.get(selectedAnswer.codeLanguage || clone.challenges[idx].languages[0]).value
        }
        return codeText
    }

    const getActiveCodeArea = () => {
        return activeChallenge && activeChallenge.answer && activeChallenge.answer.answerCode ? activeChallenge.answer.answerCode : getCodeArea(activeStep)
    }

    const setTestArea = (text) => {
        let clone = deepCopy(activeChallenge)
        clone.answer.unitTests = text
        setActiveChallenge({...clone})
    }

    const getTestArea = (idx) => {
        let clone = deepCopy(questionnaire)
        const selectedAnswer = clone.challenges[idx].answer
        let unitTestsText = selectedAnswer.unitTests
        if (!unitTestsText) {
            unitTestsText = defaultUnitTests[selectedAnswer.codeLanguage || clone.challenges[idx].languages[0]]
        }
        return unitTestsText
    }

    const getActiveTestArea = () => {
        return activeChallenge && activeChallenge.answer && activeChallenge.answer.unitTests ? activeChallenge.answer.unitTests : getTestArea(activeStep)
    }


    const handleNext = () => {
        handleStepChange(activeStep + 1)
    }

    const handleBack = () => {
        handleStepChange(activeStep - 1)
    }

    const handleStepChange = (nextStep) => {
        let clone = deepCopy(questionnaire)
        let nextChallenge = clone.challenges[nextStep]

        if(!nextChallenge.answer.codeLanguage) {
            nextChallenge.answer.codeLanguage = nextChallenge.languages[0]
        }

        setActiveChallenge(nextChallenge)
        setTextArea({ value: '', toUpdate: false })
        setCodeLanguage(nextChallenge.answer.codeLanguage)
        setActiveStep(nextStep)
    }

    const handleRunTestsChange = (event) => {
        setRunTests(event.target.checked)
    }

    const handleSubmitQuestionnaire = () => {
        setAction({
            function: QuestionnairePageController.submitQuestionnaire,
            args: [questionnaire],
            render: false
        })
    }

    const isStepCompleted = (idx) => completed.has(idx)

    const languageOptions = () => {
        return activeChallenge.languages.map((l, index) =>
        <option key={index} value={l}>{l.toLowerCase()}</option>
        )
    }

    const renderTimer = () => {
        return (
            <div className={classes.questionnaireToolbar}>
                <div className={classes.questionnaireToolbarElement}>
                    <Button className={classes.button}
                        id="completeQuestionnaire"
                        variant="contained"
                        color="primary"
                        onClick={handleSubmitQuestionnaire}
                    >
                        Submit Questionnaire
                    </Button>
                </div>
                {questionnaire.timer !== null &&
                <>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{hours}</span>
                        <div className={classes.timerText}>Hours</div>
                    </div>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{minutes}</span>
                        <div className={classes.timerText}>minutes</div>
                    </div>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{seconds}</span>
                        <div className={classes.timerText}>seconds</div>
                    </div>
                </>
                }
            </div>
        )
    }

    const getChallengeContent = (step) => {
        return (
            <>
                <Container className={classes.container} maxWidth={false}>
                    <Grid container spacing={2}>
                        <Grid container spacing={2} >
                            <Grid container spacing={4}>
                                <Grid item xs={9}>
                                    <TextField
                                        fullWidth
                                        size='medium'
                                        multiline
                                        id="challenge-description"
                                        variant='outlined'
                                        InputProps={{ readOnly: true, value: activeChallenge.description }} />

                                    <FormControlLabel
                                        control={<Checkbox
                                            checked={runTests}
                                            onChange={handleRunTestsChange}
                                            color="primary"
                                            inputProps={{ 'aria-label': 'secondary checkbox' }}
                                        />}
                                        label="Run unit tests"
                                    />

                                </Grid>
                                <Grid item xs={1}>
                                    <FormControl variant="standard" className={classes.form} fullWidth>
                                        <Select
                                            id="languageSelect"
                                            native
                                            value={codeLanguage}
                                            onChange={onLanguageChange}
                                        >
                                            {languageOptions()}
                                        </Select>
                                    </FormControl>
                                </Grid>
                            </Grid>
                        </Grid>
                        <Grid item xs={7} style={{ paddingTop: 20 }}>
                            <Grid>
                                <Toolbar className={classes.runCodetoolbar} variant="dense">
                                    <Button className={classes.runButton}
                                        id="runCodeButton"
                                        variant="contained"
                                        onClick={onRunCode}>
                                        Run Code
                                    </Button>
                                </Toolbar>
                            </Grid>
                            <RunCodeTextEditor theme={theme} textEditorData={getActiveCodeArea()} codeLanguage={activeChallenge.answer.codeLanguage || activeChallenge.languages[0]} setTextEditorData={setCodeArea} />
                        </Grid>
                        <Grid item xs={5}>
                            <Grid style={{ paddingTop: 50 }}>
                                <Toolbar className={classes.outputToolbar} variant="dense">
                                    <Box display="flex">
                                        <Typography style={{ paddingRight: 5 }}>
                                            Output:
                                        </Typography>
                                        {runState === 'running' && (
                                            <Paper className={classes.runStatePaper} style={{ color: '#ffffff', backgroundColor: '#0082C4' }}>
                                                Running...
                                            </Paper>
                                        )}
                                        {runState === 'finished' && (
                                            <Paper className={classes.runStatePaper} style={{ color: '#ffffff', backgroundColor: '#5cb85c' }}>
                                                Finished
                                            </Paper>
                                        )}
                                        {runState === 'compileError' && (
                                            <Paper className={classes.runStatePaper} style={{ color: '#d9534f', backgroundColor: '#17b033' }}>
                                                Compile Error
                                            </Paper>
                                        )}
                                    </Box>
                                </Toolbar>
                            </Grid>
                            <OutputTextEditor theme={theme} textArea={textArea} setTextArea={setTextArea} editorHeigth='300' />
                            <Grid>
                                <Toolbar className={classes.outputToolbar} variant="dense">
                                    <Box display="flex">
                                        <Typography style={{ paddingRight: 5 }}>
                                            Tests:
                                        </Typography>
                                    </Box>
                                </Toolbar>
                            </Grid>
                            <RunCodeTextEditor theme={theme} textEditorData={getActiveTestArea()} codeLanguage={activeChallenge.answer.codeLanguage || activeChallenge.languages[0]} setTextEditorData={setTestArea} editorHeigth='300' />
                        </Grid>
                    </Grid>
                </Container>
            </>
        )
    }

    const renderQuestionnairePage = () => {
        return(
            <>
                <main className={classes.layout}>
                    <Paper className={classes.paper}>
                        <>
                            {renderTimer()}
                        </>
                        <Stepper nonLinear activeStep={activeStep} className={classes.stepper}>
                            {questionnaire.challenges.map((c, idx) => (
                                <Step key={idx} completed={isStepCompleted(idx)}>
                                    <StepLabel>Challenge #{idx + 1}</StepLabel>
                                </Step>
                            ))}
                        </Stepper>
                        <>
                            {getChallengeContent(activeStep)}
                            <div className={classes.buttons}>
                                {activeStep !== 0 && (
                                    <Button onClick={handleBack} className={classes.button}>
                                        Back
                                    </Button>
                                )}
                                {
                                    activeStep !== questionnaire.challenges.length - 1 && (
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={handleNext}
                                            className={classes.button}
                                        >
                                            Next
                                        </Button>
                                    )
                                }
                                <Button className={classes.button}
                                    id="submitAnswer"
                                    variant="contained"
                                    color="primary"
                                    onClick={handleSaveAnswer}
                                >
                                    Save answer
                                </Button>
                            </div>
                        </>
                    </Paper>
                </main>
            </>
        )
    }

    if(actionState === ActionStates.clear) {
        return <CircularProgress />
    } else if(questionnaire) {
        if(actionState === ActionStates.done && response.severity === "success") {
            return(
                <>
                    {actionState === ActionStates.done && response && response.message &&
                        <CustomizedSnackbars message={response.message} severity={response.severity} />}
                    {renderQuestionnairePage()}
                </>
            )
        } else if(actionState === ActionStates.done && response.severity === "error" && response.message) {
            return <DefaultErrorMessage message={ response.message } />
        }
    } else if(actionState === ActionStates.done && response.severity === "error" && response.message) {
        return <DefaultErrorMessage message={ response.message } />
    } else if(actionState === ActionStates.inProgress) {
        return <CircularProgress />
    } else {
        return <DefaultErrorMessage message={"404 | Not Found"} />
    }

}
