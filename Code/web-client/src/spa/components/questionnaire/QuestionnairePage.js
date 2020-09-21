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
import { defaultLanguage, CodeMirrorOptions, defaultUnitTests } from '../../clientSideConfig'

import blue from '@material-ui/core/colors/blue'

const useStyles = makeStyles((theme) => ({
    appBar: {
        position: 'relative',
    },
    layout: {
        width: 'auto',
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
    const [activeStep, setActiveStep] = React.useState(0);
    const [completed, setCompleted] = React.useState(new Set());
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [questionnaire, setQuestionnaire] = React.useState()
    const [timer, setTimer] = React.useState(0)
    const [runState, setRunState] = React.useState('notRunning');
    const [runTests, setRunTests] = React.useState(false)
    const [textEditorArea, setTextEditorArea] = React.useState();
    const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false });
    const [unitTests, setUnitTests] = React.useState('');
    const [activeChallenge, setActiveChallenge] = React.useState()
    const [codeLanguage, setCodeLanguage] = React.useState()

    const { uuid } = useParams()

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
            const initialLanguage = response.json.challenges[0].answer.codeLanguage || defaultLanguage
            initChallenge.answer.codeLanguage = initialLanguage
            initChallenge.answer.unitTests = defaultUnitTests[initialLanguage]
            setQuestionnaire(response.json)
            setTimer( (response.json.startTimestamp + response.json.timer) -Date.now() )
            setActiveChallenge(initChallenge)
            setCodeLanguage(initChallenge.codeLanguage || initChallenge.languages && initChallenge.languages[0])
            setUnitTests(initChallenge.answer.unitTests || '')
        // React.state evaluation for action.clear
        } else if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnairePageController.getQuestionnaire,
                args: [uuid],
                name: "getQuestionnaireByUuid"
            })
        }
    }, [actionState]);

    let seconds = new Date(timer).getSeconds()
    let minutes = new Date(timer).getMinutes()
    let hours = new Date(timer).getHours()

    React.useEffect(() => {
        let intervalId = null
        if (timer > 0) {
            intervalId = setInterval(() => setTimer(old => old - 1000), 1000);
        }

        return () => {
            clearInterval(intervalId)
        }

    }, [timer])

    const onLanguageChange = (event) => {
        onClearConsole()
        let clone = { ...questionnaire }
        const cha = clone.challenges[activeStep]
        cha.answer.codeLanguage = event.target.value
        cha.answer.codeText = CodeMirrorOptions.get(event.target.value).value
        cha.answer.unitTests = defaultUnitTests[event.target.value]
        setQuestionnaire(clone);
        setActiveChallenge(cha)
        setCodeLanguage(event.target.value)
    }

    const onRunCode = async () => {
        if (runState !== 'running') {
            setRunState('running')
            const selectedChallengeAnswer = questionnaire.challenges[activeStep].answer
            setAction({
                function: RunCodeController.execute,
                args: [{
                    language: codeLanguage,
                    code: selectedChallengeAnswer.answerCode,
                    unitTests: unitTests,
                    executeTests: "runTests"
                }],
                name: 'runcode'
            })
        }
    }

    const onClearConsole = () => {
        if (runState !== 'notRunning') {
            setRunState('notRunning');
            setTextArea('cls')
        }
    }

    const handleComplete = () => {
        const newCompleted = new Set(completed);
        newCompleted.add(activeStep);
        setCompleted(newCompleted);
    }

    const setTestArea = (text) => {
        const clone = { ...questionnaire }
        const selectedAnswer = clone.challenges[activeStep].answer
        selectedAnswer.unitTests = text
        setQuestionnaire(clone)
        setUnitTests(text)
    }

    const setCodeArea = (text) => {
        const clone = { ...questionnaire }
        clone.challenges[activeStep].answer.answerCode = text
        setQuestionnaire(clone)
    }

    const getCodeArea = (idx) => {
        console.log(questionnaire)
        const selectedAnswer = questionnaire.challenges[idx].answer
        let codeText = selectedAnswer.answerCode
        if (!codeText) {
            codeText = CodeMirrorOptions.get(selectedAnswer.codeLanguage || defaultLanguage).value
        }
        return codeText
    }

    const handleNext = () => {
        handleStepChange(activeStep + 1)
    }

    const handleBack = () => {
        handleStepChange(activeStep - 1)
    }

    const handleStepChange = (nextStep) => {
        const nextChallenge = questionnaire.challenges[nextStep]
        
        if(!nextChallenge.answer.codeLanguage || nextChallenge.answer.codeLanguage == '') {
            nextChallenge.answer.codeLanguage = defaultLanguage
        }

        if(!nextChallenge.unitTests){
            nextChallenge.answer.unitTests = defaultUnitTests[nextChallenge.answer.codeLanguage]
        }

        setActiveChallenge(nextChallenge)
        setTextEditorArea(getCodeArea(nextStep))
        setTextArea({ value: '', toUpdate: false })
        setCodeLanguage(nextChallenge.answer.codeLanguage)
        setActiveStep(nextStep);
    }

    const handleRunTestsChange = (event) => {
        setRunTests(event.target.checked)
    }

    const handleSubmitChallenge = () => {
        handleComplete()
    }

    const handleSubmitQuestionnaire = () => {
        setAction({
            function: QuestionnairePageController.submitQuestionnaire,
            args: [questionnaire],
            render: false
        })
    }

    const isStepCompleted = (idx) => completed.has(idx)

    const renderTimer = () => {
        return (
            <div className={classes.questionnaireToolbar}>
                <div className={classes.questionnaireToolbarElement}>
                    <Button className={classes.button}
                        id="completeQuestionnaire"
                        variant="contained"
                        color="primary"
                        onClick={handleSubmitQuestionnaire}>
                        Submit Questionnaire
                    </Button>
                </div>

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
            </div>
        )
    }

    const languageOptions = () => {
        return activeChallenge.languages.map( (l, index) => 
        <option key={index} value={l}>{l.toLowerCase()}</option>   
        )
    }

    const getChallengeContent = (step) => {
        const challenge = activeChallenge.description
        return (
            <React.Fragment>
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
                                        label="Run unit tests?"
                                    />

                                </Grid>
                                <Grid item xs={1}>
                                    <FormControl variant="standard" className={classes.form} fullWidth>
                                        <Select
                                            id="languageSelect"
                                            native
                                            value={codeLanguage}
                                            onChange={onLanguageChange}>
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
                            <RunCodeTextEditor theme={theme} textEditorData={textEditorArea} codeLanguage={activeChallenge.answer.codeLanguage || defaultLanguage} setTextEditorData={setCodeArea} />
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
                            <RunCodeTextEditor theme={theme} textEditorData={unitTests} codeLanguage={activeChallenge.answer.codeLanguage || defaultLanguage} setTextEditorData={setUnitTests} editorHeigth='300' />
                        </Grid>
                    </Grid>
                </Container>
            </React.Fragment>
        )
    }

    const renderQuestionnairePage = () => {
        return(
            <React.Fragment>
                <main className={classes.layout}>
                    <Paper className={classes.paper}>
                        <React.Fragment>
                            {renderTimer()}
                        </React.Fragment>
                        <Stepper nonLinear activeStep={activeStep} className={classes.stepper}>
                            {questionnaire.challenges.map((c, idx) => (
                                <Step key={idx} completed={isStepCompleted(idx)}>
                                    <StepLabel>Challenge #{idx + 1}</StepLabel>
                                </Step>
                            ))}
                        </Stepper>
                        <React.Fragment>
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
                                            variant="contained" A
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
                                    onClick={handleSubmitChallenge}
                                >
                                    Save answer
                                </Button>
                            </div>
                        </React.Fragment>
                    </Paper>
                </main>
            </React.Fragment>
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
    } else {
        return <DefaultErrorMessage message={"404 | Not Found"} />
    }

}
