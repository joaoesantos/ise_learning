import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Toolbar from '@material-ui/core/Toolbar';
import Paper from '@material-ui/core/Paper';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import Link from '@material-ui/core/Link';
import blue from '@material-ui/core/colors/blue';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Container from '@material-ui/core/Container';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import TextField from '@material-ui/core/TextField';
import { createMuiTheme } from '@material-ui/core/styles';

import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnairePageController } from '../../controllers/QuestionnairePageController'
import { runCodeCtrl } from '../../controllers/runCodeCtrl.js'

import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'

import { defaultLanguage } from '../../clientSideConfig';

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
            //marginTop: theme.spacing(6),
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
        backgroundColor: '#5cb85c', // cor do isel -> '#963727'
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
    }
}));



export default function QuestionnairePage() {
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);
    const [completed, setCompleted] = React.useState(new Set());
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [questionnaire, setQuestionnaire] = React.useState()
    const [timer, setTimer] = React.useState(5000)
    const [runState, setRunState] = React.useState('notRunning');
    const [codeLanguage, setCodeLanguage] = React.useState(defaultLanguage);
    //const [textEditorData, setTextEditorData] = React.useState([]);
    //const [unitTestsData, setUnitTestsData] = React.useState([])
    const [textEditorData, setTextEditorData] = React.useState();
    const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false });
    const [unitTests, setUnitTests] = React.useState({ value: '', toUpdate: true });

    React.useEffect(() => {
        if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnairePageController.getQuestionnaire,
                args: [],
                render: true
            })
        } else if (actionState === ActionStates.done && action.render) {
            setQuestionnaire(response)
        } else {
            //not Done || done but not rendering
        }
    }, [actionState]);

    let seconds = ("0" + (Math.floor((timer / 1000) % 60) % 60)).slice(-2);
    let minutes = ("0" + Math.floor((timer / 60000) % 60)).slice(-2);
    let hours = ("0" + Math.floor((timer / 3600000) % 60)).slice(-2);

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
        setCodeLanguage(event.target.value);
    }

    const onRunCode = async () => {
        if (runState !== 'running') {
            setRunState('running');
            let result = await runCodeCtrl(codeLanguage, textEditorData);
            setRunState('finished');
            setTextArea({ ...textArea, value: result, toUpdate: true });
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
    };

    // const setTestsArea = (text) => {
    //     const newTests = [...unitTestsData]
    //     newTests[activeStep] = text
    //     setUnitTestsData(newTests)
    //     setUnitTests(text)
    // }

    // const setCodeArea = (text) => {
    //     const newCodes = [...textEditorData]
    //     newCodes[activeStep] = text
    //     setTextEditorData(newCodes)
    // }

    const renderTimer = () => {
        return (
            <div className={classes.questionnaireToolbar}>
                <div className={classes.questionnaireToolbarElement}>
                    <Button className={classes.button}
                        id="completeQuestionnaire"
                        variant="contained"
                        color="primary"
                        onClick={handleSubmitQuestionnaire}>
                        Complete
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

    const getChallengeContent = (step) => {
        const challenge = questionnaire.challenges[step]
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
                                        defaultValue={challenge.description}
                                        variant='outlined'
                                        InputProps={{ readOnly: true, }} />
                                </Grid>
                                <Grid item xs={1}>
                                    <FormControl variant="standard" className={classes.form} fullWidth>
                                        <Select
                                            id="languageSelect"
                                            native
                                            onChange={onLanguageChange}>
                                            <option value={'java'}>Java</option>
                                            <option value={'kotlin'}>Kotlin</option>
                                            <option value={'javascript'}>JavaScript</option>
                                            <option value={'csharp'}>C#</option>
                                            <option value={'python'}>Python</option>
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
                            <RunCodeTextEditor codeLanguage={codeLanguage} setTextEditorData={setTextEditorData} />
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
                            <OutputTextEditor textArea={textArea} setTextArea={setTextArea} editorHeigth='300'/>
                            <Grid>
                                <Toolbar className={classes.outputToolbar} variant="dense">
                                    <Box display="flex">
                                        <Typography style={{ paddingRight: 5 }}>
                                            Tests:
                                        </Typography>
                                    </Box>

                                </Toolbar>
                            </Grid>
                            <RunCodeTextEditor codeLanguage={codeLanguage} setTextEditorData={setTextEditorData} editorHeigth='300'/>
                            {/* <OutputTextEditor textArea={unitTests} setTextArea={setUnitTests} editorHeigth='300' /> */}
                        </Grid>
                    </Grid>
                </Container>

            </React.Fragment>
        )
    }

    const handleNext = () => {
        setTextArea({ value: '', toUpdate: false })
        setActiveStep(activeStep + 1);
    };

    const handleBack = () => {
        setTextArea({ value: '', toUpdate: false })
        setActiveStep(activeStep - 1);
    };

    const handleSubmitChallenge = () => {
        handleComplete();
    }

    const handleSubmitQuestionnaire = () => {
        setAction({
            function: QuestionnairePageController.submitQuestionnaire,
            args: [],
            render: true
        })
    }

    const isStepCompleted = (idx) => completed.has(idx)

    if (actionState === ActionStates.clear) {
        return <p>insert URL</p>
    } else if (actionState === ActionStates.inProgress) {
        return <p>fetching...</p>
    } else if (actionState === ActionStates.done && questionnaire) {
        return (
            <React.Fragment>
                <CssBaseline />
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
                                            className={classes.button}>
                                            Next
                                        </Button>
                                    )
                                }
                                <Button className={classes.button}
                                    id="submitAnswer"
                                    variant="contained"
                                    color="primary"
                                    onClick={handleSubmitChallenge}>
                                    Submit answer
                                </Button>
                            </div>
                        </React.Fragment>
                    </Paper>
                </main>
            </React.Fragment>
        )
    } else {
        return <p>error...</p>
    }
}
