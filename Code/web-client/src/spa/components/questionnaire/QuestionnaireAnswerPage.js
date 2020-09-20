// react
import React from 'react'
import { useParams } from "react-router-dom"
// material-ui components
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import FormLabel from '@material-ui/core/FormLabel'
import Paper from '@material-ui/core/Paper'
import Select from '@material-ui/core/Select'
import Step from '@material-ui/core/Step'
import StepLabel from '@material-ui/core/StepLabel'
import Stepper from '@material-ui/core/Stepper'
import { makeStyles } from '@material-ui/core/styles'
import TextField from '@material-ui/core/TextField'
import Radio from '@material-ui/core/Radio'
import RadioGroup from '@material-ui/core/RadioGroup'
// custom components
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnaireAnswerController } from '../../controllers/questionnaire/QuestionnaireAnswerController'

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
}));

export default function QuestionnaireAnswerPage(props) {
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [textEditorArea, setTextEditorArea] = React.useState();
    const [textArea, setTextArea] = React.useState();
    const [questionnaire, setQuestionnaire] = React.useState()

    let { id } = useParams();
    React.useEffect(() => {
        if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnaireAnswerController.getAllQuestionnaireAnswersFromQuestionnaireInstanceId,
                args: [id],
                render: true
            })
        } else if (actionState === ActionStates.done && action.render) {
            setQuestionnaire(response)
            const selectedChallenge = response.challenges[activeStep]
            setTextEditorArea(selectedChallenge.answerCode)
            setTextArea(selectedChallenge.isCorrect)
        } else {
            //not Done || done but not rendering
        }
    }, [actionState]);

    const handleNext = () => {
        handleStepChange(activeStep + 1)
    };

    const handleBack = () => {
        handleStepChange(activeStep - 1)
    };

    const handleStepChange = (nextStep) => {
        const selectedChallenge = questionnaire.challenges[nextStep]
        setTextEditorArea(selectedChallenge.answerCode)
        setTextArea(selectedChallenge.isCorrect)
        setActiveStep(nextStep);
    }

    const getChallengeAnswerContent = (step) => {
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
                                            disabled
                                            native
                                            value={challenge.codeLanguage}>
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
                            <RunCodeTextEditor readOnly={true} value={textEditorArea} setTextEditorData={setTextEditorArea} codeLanguage={challenge.codeLanguage} />
                        </Grid>
                        <Grid item xs={5} style={{ paddingTop: 20 }}>
                            <FormLabel component="legend">Passed Challenge?</FormLabel>
                            <RadioGroup aria-label="passed" name="passed" >
                                <FormControlLabel control={<Radio checked={challenge.isCorrect} />} label="Yes" />
                                <FormControlLabel control={<Radio checked={!challenge.isCorrect}/>} label="No" />
                            </RadioGroup>
                        </Grid>
                    </Grid>
                </Container>

            </React.Fragment>
        )
    }

    if (actionState === ActionStates.clear) {
        return <p>insert URL</p>
    } else if (actionState === ActionStates.inProgress) {
        return <p>fetching...</p>
    } else if (actionState === ActionStates.done && questionnaire) {
        return (
            <React.Fragment>
                <main className={classes.layout}>
                    <Paper className={classes.paper}>
                        <Stepper nonLinear activeStep={activeStep} className={classes.stepper}>
                            {questionnaire.challenges.map((c, idx) => (
                                <Step key={idx}>
                                    <StepLabel>Challenge #{idx + 1}</StepLabel>
                                </Step>
                            ))}
                        </Stepper>
                        <React.Fragment>
                            {getChallengeAnswerContent(activeStep)}
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