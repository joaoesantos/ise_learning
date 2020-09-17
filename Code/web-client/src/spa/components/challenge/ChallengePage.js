// react
import React from 'react'
import { Redirect, withRouter } from 'react-router-dom'
// react-reflex
import { ReflexContainer, ReflexSplitter, ReflexElement } from 'react-reflex'
// material-ui components
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import Input from '@material-ui/core/Input'
import InputBase from '@material-ui/core/InputBase'
import InputLabel from '@material-ui/core/InputLabel'
import ListItemText from '@material-ui/core/ListItemText'
import MenuItem from '@material-ui/core/MenuItem'
import { makeStyles, withStyles } from '@material-ui/core/styles'
import Select from '@material-ui/core/Select'
import Toolbar from '@material-ui/core/Toolbar'
// custom components
import CustomizedTabs from '../Tabs'
import ChallengeStatement from './ChallengeStatement'
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// notifications
import CircularProgress from '../notifications/CircularProgress'
import CustomizedSnackbars from '../notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
// context
import { AuthContext } from '../../context/AuthContext'
import { ThemeContext } from '../../context/ThemeContext'
// configs
import { CodeMirrorOptions, defaultUnitTests } from '../../clientSideConfig'
import { ChallengePageConfigs } from '../../controllers/challenge/ChallengePageConfigs'
// utils
import { genericRunCodeAction, genericSetTextEditorData } from '../../utils/ChallengeUtils'


const useStyles = makeStyles(theme => ({
    layout: {
        height: "80vh"
    },
    toolbar: {
        paddingLeft: theme.spacing(1),
        borderBottom: `1px solid ${theme.palette.divider}`,
        justifyContent: "space-between"
    },
    outputToolbar: {
        paddingLeft: theme.spacing(1),
        borderBottom: `1px solid ${theme.palette.divider}`,
        justifyContent: "space-between"
    },
    button: {
        margin: theme.spacing(1),
        textTransform:"none",
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 150,
        maxWidth: 300,
    },
    title: {
        margin: theme.spacing(1.5),
        lineHeight: "32px",
        fontSize: "xx-large",
        fontWeight: "bold"
    }
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

    const classes = useStyles()
    const { theme } = React.useContext(ThemeContext)
    const { user } = React.useContext(AuthContext)
    const challengeId = props.match.params.challengeId
    const userId = props.match.params.userId

    const [codeLanguage, setCodeLanguage] = React.useState()
    const [challengeLanguages, setChallengeLanguages] = React.useState([])
    const [isChallengeEditable, setIsChallengeEditable] = React.useState(false)
    const [redirectObject, setRedirectObject] = React.useState()

    const [yourSolution, setYourSolution] = React.useState({})
    const [ourSolution, setOurSolution] = React.useState({})

    const [yourTests, setYourTests] = React.useState({})
    const [ourTests, setOurTests] = React.useState({})

    const [outputText, setOutputText] = React.useState({ value: '', toUpdate: false })

    // fetch props & data
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [challenge, setChallenge] = React.useState()
    const [challengeAnswer, setChallengeAnswer] = React.useState()
    const [availableLanguages, setAvailableLanguages] = React.useState([])

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
    let pageConfigs = ChallengePageConfigs(challengeId, userId, componentAggregateStates, user)[props.location.configKey ? props.location.configKey : props.configKey]

    React.useEffect(() => {
        // React.state evaluation for run code button action
        if(actionState === ActionStates.done && action.name === "runcode" && response.severity === "success") {
            response.textSufix = action.textSufix
            console.log("should only enter here once")
            setOutputText({ value: response.json, toUpdate: true })
        } 
        // React.state evaluation for action.done
        else if(response && actionState === ActionStates.done &&
            action.render && action.render === true && !response.severity) {
                pageConfigs.renderizationFunction(response.json)
        } 
        // React.state evaluation for action.clear
        else if(actionState === ActionStates.clear) {
            setAction(pageConfigs.pageLoadingAction())
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

    const handleChallengeTitleChange = (e) => {
        let newChallenge = Object.assign({}, challenge);
        newChallenge.challengeTitle = e.target.value;
        setChallenge(newChallenge)
    }

    function onClearConsole() {
        if(actionState !== ActionStates.inProgress) {
          setOutputText('cls')
        }
    }

    let yourSolutionRunCodeAction = genericRunCodeAction(setAction, codeLanguage, yourSolution[codeLanguage] ? yourSolution[codeLanguage].value : "", "", false, "Your Solution")
    let ourSolutionRunCodeAction = genericRunCodeAction(setAction, codeLanguage, ourSolution[codeLanguage] ? ourSolution[codeLanguage].value : "", "", false, "Our Solution")
    let yourTestsRunCodeAction = genericRunCodeAction(setAction, codeLanguage, yourSolution[codeLanguage] ? yourSolution[codeLanguage].value : "",  yourTests.value, true, "Your Tests")
    let ourTestsRunCodeAction = genericRunCodeAction(setAction, codeLanguage, yourSolution[codeLanguage] ? yourSolution[codeLanguage].value : "",  ourTests.value, true, "Our Tests")

    let determineDefaultTextForEditableEditors = (map, type) => {
        if(map[codeLanguage]) {
            return map[codeLanguage].value;
        } else if(!codeLanguage) {
            return "Please add a new language."
        } else if (type === "code") {
            return CodeMirrorOptions.get(codeLanguage).value
        } else if(type === "test") {
            return defaultUnitTests[codeLanguage]
        }
    }

    const renderChallengePage = () => {
        return (
            <div className={classes.layout}>
                <InputBase
                    className={classes.title}
                    defaultValue={challenge ? challenge.challengeTitle : 'New Challenge Title'}
                    inputProps={{ 'aria-label': 'naked' }}
                    disabled={!isChallengeEditable}
                    required={true}
                    onChange={handleChallengeTitleChange}
                    sytle={{paddingLeft:50}}
                />
                <Toolbar className={classes.toolbar} variant="dense">
                    <FormControl className={classes.formControl}>
                        <InputLabel id="avaible-languages-label" shrink>Avaiable Languages</InputLabel>
                        <Select
                            id="languageSelect"
                            native
                            value={codeLanguage}
                            onChange={event => setCodeLanguage(event.target.value)}
                        >
                            {challengeLanguages.map(lang => {
                                return <option value={lang.value} key={lang.value}>{lang.label}</option>
                            })}
                        </Select>
                    </FormControl>
                    {isChallengeEditable &&
                    <FormControlLabel
                        control={<GrayCheckbox checked={challenge ? challenge.isPrivate : true} 
                        onChange={handleIsPrivateChange} name="isPrivate" />}
                        label="Private Challenge"
                    />}
                    {isChallengeEditable && 
                    <FormControl className={classes.formControl} style={{minWidth:200}}>
                        <InputLabel id="edit-avaible-languages-label" shrink>Edit Available Languages</InputLabel>
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
                                    if(e.target.value.length === 1) {
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
                        return <Button className={classes.button}
                            color={"primary"}
                            key={e.id}
                            id={e.id}
                            variant="contained"
                            onClick={() => e.onClick()}
                        >
                            {e.title}
                        </Button>
                    })}
                </Toolbar>
                <ReflexContainer orientation="horizontal">
                    <ReflexElement>
                        <ReflexContainer orientation="vertical">
                            <ReflexElement >
                                <ReflexContainer orientation="horizontal">
                                    <ReflexElement >
                                        <CustomizedTabs
                                            childComponents={[
                                                <ChallengeStatement 
                                                    challengeStatement={challenge ? challenge.challengeText : ""} 
                                                    setChallengeStatement={handleChallengeStatementChange} 
                                                    readOnly={!isChallengeEditable} 
                                                />,
                                                <RunCodeTextEditor 
                                                    theme={theme} 
                                                    codeLanguage={codeLanguage} 
                                                    textEditorData={determineDefaultTextForEditableEditors(ourSolution, "code")} 
                                                    setTextEditorData={genericSetTextEditorData(setOurSolution, ourSolution, codeLanguage)} 
                                                    readOnly={!isChallengeEditable || !codeLanguage} 
                                                    actions={[ourSolutionRunCodeAction]} 
                                                />
                                            ]} 
                                            tabLabels={['CHALLENGE TEXT','OUR SOLUTION']}
                                        />
                                    </ReflexElement>
                                    <ReflexSplitter />
                                    <ReflexElement >
                                        <CustomizedTabs
                                            childComponents={[
                                                <RunCodeTextEditor 
                                                    theme={theme} 
                                                    codeLanguage={codeLanguage} 
                                                    textEditorData={determineDefaultTextForEditableEditors(ourTests, "test")} 
                                                    setTextEditorData={genericSetTextEditorData(setOurTests, ourTests, codeLanguage)} 
                                                    readOnly={!isChallengeEditable || !codeLanguage} 
                                                    actions={[ourTestsRunCodeAction]} 
                                                />,
                                                <RunCodeTextEditor 
                                                    theme={theme} 
                                                    codeLanguage={codeLanguage} 
                                                    textEditorData={yourTests[codeLanguage] ? yourTests[codeLanguage].value : ""} 
                                                    setTextEditorData={genericSetTextEditorData(setYourTests, yourTests, codeLanguage)} 
                                                    actions={[yourTestsRunCodeAction]} 
                                                />
                                            ]} 
                                            tabLabels={['OURS TESTS','YOURS TESTS']}
                                        />
                                    </ReflexElement>
                                </ReflexContainer>
                            </ReflexElement>
                            <ReflexSplitter />
                            <ReflexElement >
                                <ReflexContainer orientation="horizontal">
                                    <ReflexElement >
                                        <div>
                                        <ReflexContainer orientation="vertical">
                                            <ReflexElement >
                                                <CustomizedTabs
                                                    childComponents={[
                                                        <RunCodeTextEditor 
                                                            theme={theme} 
                                                            codeLanguage={codeLanguage}
                                                            textEditorData={yourSolution[codeLanguage] ? yourSolution[codeLanguage].value : ""} 
                                                            setTextEditorData={genericSetTextEditorData(setYourSolution, yourSolution, codeLanguage)} 
                                                            actions={[yourSolutionRunCodeAction]} 
                                                        />
                                                    ]} 
                                                    tabLabels={['YOUR SOLUTION']}
                                                />
                                            </ReflexElement>
                                        </ReflexContainer>
                                        </div>
                                    </ReflexElement>
                                    <ReflexSplitter />
                                    <ReflexElement >
               
                                        <CustomizedTabs
                                            childComponents={[
                                                <>
                                                    <Button className={classes.button}
                                                        id="clearConsoleButton"
                                                        variant="contained"
                                                        onClick={onClearConsole}
                                                        style={{minWidth: 125}}
                                                    >
                                                        Clear Console
                                                    </Button>
                                                    <OutputTextEditor 
                                                        theme={theme} 
                                                        textArea={outputText} 
                                                        setTextArea={setOutputText} 
                                                    />
                                                </>
                                            ]} 
                                            tabLabels={['EXECUTION RESULT']}
                                        />
                                    </ReflexElement>
                                </ReflexContainer>
                            </ReflexElement>
                        </ReflexContainer>
                    </ReflexElement>
                </ReflexContainer>
            </div>
        )
    }

    console.log(actionState,response)
    switch(props.configKey) {
        case "challenge":
            if(challenge) {
                return (
                    <>
                        {actionState === ActionStates.done && response.message && 
                            <CustomizedSnackbars message={response.message} severity={response.severity} />}
                        {renderChallengePage()}
                    </>
                )
            } else {
                if(actionState === ActionStates.clear || actionState === ActionStates.inProgress) {
                    return <CircularProgress />
                } else if(actionState === ActionStates.done && response.message) {
                    return <DefaultErrorMessage message={ response.message } />
                }
            }

        case "newChallenge":
            if(user) {
                if(actionState === ActionStates.clear) {
                    console.log("do something")
                } else if(actionState === ActionStates.inProgress) {
                    console.log("do not something")
                } else if(actionState === ActionStates.done) {
                    console.log("do not")
                }
                return (
                    <>
                        {redirectObject !== undefined && <Redirect push to={redirectObject} />}
                        {actionState === ActionStates.done && response.message && 
                            <CustomizedSnackbars message={response.message} severity={response.severity} />}
                        {renderChallengePage()}
                    </>
                )
            } else {
                return <DefaultErrorMessage message={"401 | Unauthorized"} />
            }

    
        case "challengeAnswer":
            return (
                <>
                    {redirectObject !== undefined && <Redirect push to={redirectObject} />}
                    {actionState === ActionStates.done && response.message && 
                        <CustomizedSnackbars message={response.message} severity={response.severity} />}
                    {renderChallengePage()}
                </>
            )
            break
        default:
            return <DefaultErrorMessage message={"404 | Not Found"} />
    }


});