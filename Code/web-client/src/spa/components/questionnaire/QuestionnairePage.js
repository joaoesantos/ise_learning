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

import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnairePageController } from '../../controllers/QuestionnairePageController'
import { runCodeCtrl } from '../../controllers/runCodeCtrl.js'

import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'

import { codeMirrorDefault } from '../../clientSideConfig';

const useStyles = makeStyles((theme) => ({
    appBar: {
        position: 'relative',
    },
    layout: {
        width: 'auto',
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(2) * 2)]: {
            width: 600,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        marginTop: theme.spacing(3),
        marginBottom: theme.spacing(3),
        padding: theme.spacing(2),
        [theme.breakpoints.up(600 + theme.spacing(3) * 2)]: {
            marginTop: theme.spacing(6),
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
    timer: {
        fontFamily: 'sans-serif',
        color: '#fff',
        display: 'inline-block',
        textAlign: 'center',
        fontSize: '12px',
    },
    timerElement: {
        padding: '5px',
        borderRadius: '3px',
        background: blue[600],
        display: 'inline-block',
        marginRight: theme.spacing(2)
    },
    timerValue: {
        padding: '2px',
        borderRadius: '3px',
        background: blue[900],
        display: 'inline-block'
    },
    timerText: {
        paddingTop: '5px',
        fontSize: '10px'
    }
}));



export default function QuestionnairePage() {
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [questionnaire, setQuestionnaire] = React.useState()
    const [timer, setTimer] = React.useState(5000)
    const [runState, setRunState] = React.useState('notRunning');
    const [codeLanguage, setCodeLanguage] = React.useState(codeMirrorDefault);
    const [textEditorData, setTextEditorData] = React.useState();
    const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false });

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
        if(timer > 0) {
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

    const onRunCode = async ()  => {
        if(runState !== 'running') {
          setRunState('running');
          let result = await runCodeCtrl(codeLanguage, textEditorData);
          setRunState('finished');
          setTextArea({ ...textArea, value: result, toUpdate: true });
        }
      }

    const onClearConsole = () => {
        if(runState !== 'notRunning') {
          setRunState('notRunning');
          setTextArea('cls')
        }
    }

    const renderTimer = () => {
        return (
            <div className={classes.timer}>
                <h6>Time Left</h6>
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
                {/* Time Left {hours} H : {minutes} min : {seconds} s */}
            </div>
        )

    }

    const getChallengeContent = (step) => {
        const challenge = questionnaire.challenges[step]
        return(
        <React.Fragment>
            <Container className={classes.container} maxWidth={false}>
        <Grid container>
          <Grid item xs={7}>
            <Grid>
              <Toolbar className={classes.runCodetoolbar} variant="dense">
                <Button className={classes.runButton}
                  id="runCodeButton"
                  variant="contained"
                  onClick={onRunCode}
                >
                  Run Code
                </Button>
                <FormControl variant="standard" className={classes.form}>
                  <Select
                  id="languageSelect"
                  native
                  onChange={onLanguageChange}
                  >
                    <option value={'java'}>Java</option>
                    <option value={'kotlin'}>Kotlin</option>
                    <option value={'javascript'}>JavaScript</option>
                    <option value={'csharp'}>C#</option>
                    <option value={'python'}>Python</option>
                  </Select>
                </FormControl>
              </Toolbar>
            </Grid>
            <RunCodeTextEditor codeLanguage={codeLanguage} setTextEditorData={setTextEditorData} />
          </Grid>
          <Grid item xs={5}>
                <Grid>
                <Toolbar className={classes.outputToolbar} variant="dense">
                    <Box display="flex">
                    <Typography style={{paddingRight:5}}>
                        Output:
                    </Typography>
                    {runState === 'running' && (
                        <Paper className={classes.runStatePaper} style={{color:'#ffffff',backgroundColor:'#0082C4'}}>
                        Running...
                        </Paper>
                    )}
                    {runState === 'finished' && (
                        <Paper className={classes.runStatePaper} style={{color:'#ffffff',backgroundColor:'#5cb85c'}}>
                        Finished
                        </Paper>
                    )}
                    {runState === 'compileError' && (
                        <Paper className={classes.runStatePaper} style={{color:'#d9534f',backgroundColor:'#17b033'}}>
                        Compile Error
                        </Paper>
                    )}
                    </Box>
                    <Button className={classes.submitButton}
                    id="submitAnswer"
                    variant="contained"
                    onClick={handleSubmitChallenge}
                    >
                    Submit answer
                    </Button>
                </Toolbar>
                </Grid>
                <OutputTextEditor textArea={textArea} setTextArea={setTextArea} />
            </Grid>
        </Grid>
      </Container>
            
        </React.Fragment>
        )
    }

    const handleNext = () => {
        setActiveStep(activeStep + 1);
    };

    const handleBack = () => {
        setActiveStep(activeStep - 1);
    };

    const handleSubmitChallenge = () => {
        setAction({
            function: QuestionnairePageController.submitChallenge,
            args: [],
            render: true
        })
    }

    const handleSubmitQuestionnaire = () => {
        setAction({
            function: QuestionnairePageController.submitQuestionnaire,
            args: [],
            render: true
        })
    }

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
                        <Typography component="h1" variant="h4" align="center">
                            {questionnaire.description}
                        </Typography>
                        <Stepper activeStep={activeStep} className={classes.stepper}>
                            {questionnaire.challenges.map((c, idx) => (
                                <Step key={idx}>
                                    <StepLabel>{idx}</StepLabel>
                                </Step>
                            ))}
                        </Stepper>
                        <React.Fragment>
                            {renderTimer()}
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
