// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Select from '@material-ui/core/Select';
import Toolbar from '@material-ui/core/Toolbar';
// custom components
import Tabs from '../Tabs'
import ChallengeStatement from './ChallengeStatement'
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// controllers
import { ChallengeController } from '../../controllers/ChallengeController'
import { RunCodeController } from '../../controllers/RunCodeController'
import UseAction, { ActionStates } from '../../controllers/UseAction'


const useStyles = makeStyles(theme => ({
    layout: { },
    runCodetoolbar: {
        paddingLeft: theme.spacing(1),
        background: '#ffffff',
        borderBottom: `1px solid ${theme.palette.divider}`,
        justifyContent: "space-between"
    },
    gridItem: {
        //maxHeight: '50vh'
    }
}));

export default function ChallengePage(props) {
    const classes = useStyles();
    const id = props.match.params.id

    const [challengeStatement, setChallengeStatement] = React.useState();

    const [codeLanguage, setCodeLanguage] = React.useState();
    const [yourSolution, setYourSolution] = React.useState();
    const [ourSolution, setOurSolution] = React.useState();
    const [outputText, setOutputText] = React.useState({ value: '', toUpdate: false });

    const [yourTests, setYourTests] = React.useState();
    const [ourTests, setOurTests] = React.useState();

    // fetch props & data
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [challenge, setChallenge] = React.useState()

    
    React.useEffect(() => {
        if (response && actionState === ActionStates.done &&
          action.render && action.render === true) {
              setChallenge(response)
              setChallengeStatement(response.challengeText)
              setCodeLanguage(response.solutions[0].codeLanguage.toLowerCase())
              setYourSolution(response.solutions[0].challengeCode)
              setOurSolution(response.solutions[0].solutionCode)
              setYourTests(response.solutions[0].unitTests)
              setOurTests(response.solutions[0].unitTests)
        } else if (!response) {
            setAction({
                function: ChallengeController.getChallengeById,
                args: [id],
                render: true
            })
        } else {
            //TODO
        }
    },[actionState]);


    let genericRunCodeAction = (code, unitTests, executeTests) => {
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
                console.log(result)
                setOutputText({ value: result, toUpdate: true });
            }
        }
    }
    
    let yourSolutionRunCodeAction = genericRunCodeAction(yourSolution, "", false);
    let ourSolutionRunCodeAction = genericRunCodeAction(ourSolution, "", false);
    let yourTestsRunCodeAction = genericRunCodeAction(yourSolution, yourTests, true);
    let ourTestsRunCodeAction = genericRunCodeAction(yourSolution, ourTests, true);

    const tabComponentsChallengeStatement = [
        <ChallengeStatement challengeStatement={challengeStatement} />,
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={ourSolution} setTextEditorData={setOurSolution} readOnly={true} actions={[ourSolutionRunCodeAction]} />
    ];

    const tabLabelsChallengeStatement = [
        "Challenge Statement", "Our Solution"
    ]

    const tabComponentsTests = [
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourTests} setTextEditorData={setYourTests} actions={[yourTestsRunCodeAction]} />,
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={ourTests} setTextEditorData={setOurTests} readOnly={true} actions={[ourTestsRunCodeAction]} />
    ];

    const tabLabelsTests = [
        "Your Tests", "Our Tests"
    ]

    return (
        <React.Fragment>
            <div className={classes.layout}>
                <h1>Challenges</h1>
            </div>
            <Toolbar className={classes.runCodetoolbar} variant="dense">
                <FormControl variant="standard" className={classes.form}>
                    <Select
                        id="languageSelect"
                        native
                        onChange={event => setCodeLanguage(event.target.value)}
                    >
                        <option value={'java'}>Java</option>
                        <option value={'kotlin'}>Kotlin</option>
                        <option value={'javascript'}>JavaScript</option>
                        <option value={'csharp'}>C#</option>
                        <option value={'python'}>Python</option>
                    </Select>
                </FormControl>
            </Toolbar>
            {challenge && <Grid container spacing={3}>
                <Grid item xs={12} sm={6} className={classes.gridItem} >
                    <Tabs useStyles={useStyles} childComponents={tabComponentsChallengeStatement} tabLabels={tabLabelsChallengeStatement} />
                </Grid>
                <Grid item xs={12} sm={6} className={classes.gridItem} >
                    <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourSolution} setTextEditorData={setYourSolution} actions={[yourSolutionRunCodeAction]} />
                </Grid>
                <Grid item xs={12} sm={6} className={classes.gridItem} >
                    <Tabs useStyles={useStyles} childComponents={tabComponentsTests} tabLabels={tabLabelsTests} />
                </Grid>
                <Grid item xs={12} sm={6} className={classes.gridItem} >
                    <OutputTextEditor textArea={outputText} setTextArea={setOutputText} />
                </Grid>
            </Grid>}
        </React.Fragment>
    )
};