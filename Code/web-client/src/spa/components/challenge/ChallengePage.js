// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
import { Redirect, useHistory, withRouter } from 'react-router-dom'
// material-ui components
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Grid from '@material-ui/core/Grid'
import Input from '@material-ui/core/Input'
import InputLabel from '@material-ui/core/InputLabel'
import ListItemText from '@material-ui/core/ListItemText'
import MenuItem from '@material-ui/core/MenuItem'
import { makeStyles, withStyles } from '@material-ui/core/styles'
import Paper from '@material-ui/core/Paper'
import Select from '@material-ui/core/Select'
import Toolbar from '@material-ui/core/Toolbar'
// custom components
import Tabs from '../Tabs'
import ChallengeStatement from './ChallengeStatement'
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { ChallengeController } from '../../controllers/challenge/ChallengeController'
import { ChallengePageConfigs } from '../../controllers/challenge/ChallengePageConfigs'
import { genericRunCodeAction, genericSetTextEditorData } from '../../utils/ChallengeUtils'
// configs
import { CodeMirrorOptions, defaultUnitTests } from '../../clientSideConfig'


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
    },
    button: {
        margin: theme.spacing(1),
        textTransform:"none",
        color:'#ffffff',
        backgroundColor:'#5cb85c', // cor isel -> '#963727'
        '&:hover' : {
            backgroundColor: '#17b033',
        }
    },
    runButton: {
        margin: theme.spacing(1),
        textTransform:"none",
        color:'#ffffff',
        backgroundColor:'#5cb85c', // cor do isel -> '#963727'
        '&:hover' : {
            backgroundColor: '#17b033',
        }
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
        maxWidth: 300,
      },
}));

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
      style: {
        maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
        width: 250,
      },
    },
};


const GrayCheckbox = withStyles({
    root: {
        color: '#9b9b9b',
        '&$checked': {
        color: '#4a4a4a',
        },
    },
    checked: {},
})((props) => <Checkbox color="default" {...props} />);

export default withRouter(function ChallengePage(props) {
    const classes = useStyles();
    //const history = useHistory();
    const challengeId = props.match.params.challengeId
    const userId = props.match.params.userId

    const [codeLanguage, setCodeLanguage] = React.useState();
    const [challengeLanguages, setChallengeLanguages] = React.useState([]);
    const [isChallengeEditable, setIsChallengeEditable] = React.useState(false);
    const [redirectObject, setRedirectObject] = React.useState();

    const [yourSolution, setYourSolution] = React.useState({});
    const [ourSolution, setOurSolution] = React.useState({});

    const [outputText, setOutputText] = React.useState({ value: '', toUpdate: false });

    const [yourTests, setYourTests] = React.useState({});
    const [ourTests, setOurTests] = React.useState({});

    // fetch props & data
    const [action, setAction] = React.useState({});
    const [actionState, response] = UseAction(action);
    const [challenge, setChallenge] = React.useState();
    const [challengeAnswer, setChallengeAnswer] = React.useState();
    const [availableLanguages, setAvailableLanguages] = React.useState([]);

    let componentAggregateStates = {
        props: props,
        codeLanguage: { state: codeLanguage, setter: setCodeLanguage},
        challengeLanguages: { state: challengeLanguages, setter: setChallengeLanguages},
        isChallengeEditable: { state: isChallengeEditable, setter: setIsChallengeEditable},
        redirectObject: { state: redirectObject, setter: setRedirectObject},
        yourSolution: { state: yourSolution, setter: setYourSolution},
        ourSolution: { state: ourSolution, setter: setOurSolution},
        outputText: { state: outputText, setter: setOutputText},
        yourTests: { state: yourTests, setter: setYourTests},
        ourTests: { state: ourTests, setter: setOurTests},
        action: { state: action, setter: setAction},
        challenge: { state: challenge, setter: setChallenge},
        challengeAnswer: { state: challengeAnswer, setter: setChallengeAnswer},
        availableLanguages: { state: availableLanguages, setter: setAvailableLanguages}
    }
    let pageConfigs = ChallengePageConfigs(challengeId, userId, componentAggregateStates)[props.configKey]

    React.useEffect(() => {
        if (response && actionState === ActionStates.done &&
            action.render && action.render === true) {
                pageConfigs.renderizationFunction(response)
        } else if (!response && actionState === ActionStates.clear) {
            setAction(pageConfigs.pageLoadingAction())
        } else {
            //TODO
        }
    },[actionState]);

    const handleIsPrivateChange = (e) => {
        let newChallenge = Object.assign({}, challenge);
        newChallenge.isPrivate = e.target.checked;
        setChallenge(newChallenge)
    }

    const handleChallengeStatementChange = (e) => {
        let newChallenge = Object.assign({}, challenge);
        newChallenge.challengeText = e.target.value;
        setChallenge(newChallenge)
    }

    let yourSolutionRunCodeAction = genericRunCodeAction(yourSolution.value, "", false, "Your Solution", setOutputText, codeLanguage);
    let ourSolutionRunCodeAction = genericRunCodeAction(ourSolution.value, "", false, "Our Solution", setOutputText, codeLanguage);
    let yourTestsRunCodeAction = genericRunCodeAction(yourSolution.value, yourTests.value, true, "Your Tests", setOutputText, codeLanguage);
    let ourTestsRunCodeAction = genericRunCodeAction(yourSolution.value, ourTests.value, true, "Our Tests", setOutputText, codeLanguage);

    let determineDefaultTextForEditableEditors = (map, type) => {
        if(map[codeLanguage]) {
            return map[codeLanguage].value;
        } else if(!codeLanguage) {
            return "Please add a new language.";
        } else if (type === "code") {
            return CodeMirrorOptions.get(codeLanguage).value;
        } else if(type === "test") {
            return defaultUnitTests[codeLanguage];
        }
    }

    let tabs = {
        tab1: {
            components: [
                <ChallengeStatement challengeStatement={challenge ? challenge.challengeText : ""} setChallengeStatement={handleChallengeStatementChange} readOnly={!isChallengeEditable} />,
                <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={determineDefaultTextForEditableEditors(ourSolution, "code")} setTextEditorData={genericSetTextEditorData(setOurSolution, ourSolution, codeLanguage)} readOnly={!isChallengeEditable || !codeLanguage} actions={[ourSolutionRunCodeAction]} />
            ],
            labels: ["Challenge Statement", "Our Solution"]
        },
        tab2: {
            components: [
                <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourSolution[codeLanguage] ? yourSolution[codeLanguage].value : ""} setTextEditorData={genericSetTextEditorData(setYourSolution, yourSolution, codeLanguage)} actions={[yourSolutionRunCodeAction]} />
            ],
            labels: ["Your Solution"]
        },
        tab3: {
            components: [
                <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={determineDefaultTextForEditableEditors(ourTests, "test")} setTextEditorData={genericSetTextEditorData(setOurTests, ourTests, codeLanguage)} readOnly={!isChallengeEditable || !codeLanguage} actions={[ourTestsRunCodeAction]} />
            ],
            labels: ["Our Tests"]
        },
        tab4: {
            components: [
                <OutputTextEditor textArea={outputText} setTextArea={setOutputText} />
            ],
            labels: ["Execution Result"]
        }
    }
    if(pageConfigs.showYourTests) {
        tabs.tab3.components.push(<RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourTests[codeLanguage] ? yourTests[codeLanguage].value : ""} setTextEditorData={genericSetTextEditorData(setYourTests, yourTests, codeLanguage)} actions={[yourTestsRunCodeAction]} />)
        tabs.tab3.labels.push("Your Tests");
    }
    

    return (
        <React.Fragment>
            {redirectObject !== undefined && <Redirect push to={redirectObject} />}
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
                        {challengeLanguages.map(lang => {
                            return <option value={lang.value}>{lang.label}</option>
                        })}
                    </Select>
                </FormControl>
                {isChallengeEditable && <FormControlLabel
                    control={<GrayCheckbox checked={challenge ? challenge.isPrivate : true} onChange={handleIsPrivateChange} name="isPrivate" />}
                    label="Private Challenge"
                />}
                {isChallengeEditable && <FormControl className={classes.formControl}>
                    <InputLabel id="demo-mutiple-checkbox-label">Edit Available Languages</InputLabel>
                    <Select
                        labelId="language-mutiple-checkbox-label"
                        id="language-mutiple-checkbox"
                        multiple
                        value={availableLanguages.filter(lang => challengeLanguages.map(l => l.value).indexOf(lang.value) > -1)}
                        onChange={e => {
                            if(e.target.value.length < 1) {
                                alert("There must be at least 1 language selected.")
                            } else {
                                setChallengeLanguages(e.target.value)
                                if(e.target.value.length == 1) {
                                    let language = e.target.value[0].value;
                                    setCodeLanguage(language)
                                }
                            }
                        }}
                        input={<Input />}
                        renderValue={selected => selected.map(s => s.label).join(', ')}
                        MenuProps={MenuProps}
                    >
                        {availableLanguages.map((language) => (
                            <MenuItem key={language.value} value={language}>
                                <Checkbox checked={challengeLanguages.map(l => l.value).indexOf(language.value) > -1} />
                                <ListItemText primary={language.label} />
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>}
                {pageConfigs.headerButtons.filter(e => e.isVisible).map( e => {
                    return <Button className={classes.runButton}
                        id={e.id}
                        variant="contained"
                        onClick={() => e.onClick()}
                    >
                        {e.title}
                    </Button>
                })}
            </Toolbar>
            {challenge && <Grid container spacing={3}>
                {Object.values(tabs).filter(t => t.components.length > 0).map((t, i) =>
                    <Grid key={i} item xs={12} sm={6} className={classes.gridItem} >
                        <Tabs useStyles={useStyles} childComponents={t.components} tabLabels={t.labels} />
                    </Grid>
                )}
            </Grid>}
        </React.Fragment>
    )
});