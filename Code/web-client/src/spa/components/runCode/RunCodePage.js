// react
import React from 'react'
// react-reflex
import { ReflexContainer, ReflexSplitter, ReflexElement } from 'react-reflex'
// material-ui components
import Box from '@material-ui/core/Box'
import Button from '@material-ui/core/Button'
import FormControl from '@material-ui/core/FormControl'
import Paper from '@material-ui/core/Paper'
import Select from '@material-ui/core/Select'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'
// codemirror components
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// custom components
import Footer from '../footer/Footer.js'
// notifications
import CustomizedSnackbars from '../../components/notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../../components/notifications/DefaultErrorMessage'
// authentication context
import { ThemeContext } from '../../context/ThemeContext'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { RunCodeController } from '../../controllers/RunCodeController'
// client side configurations
import { defaultLanguage, languageLabelMappings } from '../../clientSideConfig'

const useStyles = makeStyles((theme) => ({
  layout: {
    height: "90vh"
  },
  container: {
    padding: theme.spacing(0),
  },
  runCodetoolbar: {
    paddingLeft: theme.spacing(1),
    borderBottom: `1px solid ${theme.palette.divider}`,
    justifyContent: "space-between"
  },
  outputToolbar: {
    paddingLeft: theme.spacing(1),
    borderBottom: `1px solid ${theme.palette.divider}`,
    justifyContent: "space-between"
  },
  runButton: {
      margin: theme.spacing(1),
      textTransform:"none",
      color: "#ffffff",
      backgroundColor:'#5cb85c',
      '&:hover' : {
          backgroundColor: '#388e3c',
      }
  },
  clearButton: {
    margin: theme.spacing(1),
    textTransform:"none",
  },
  runStatePaper: {
    elevation:1,
    borderStyle:'solid',
  },
}));

export default function RunCodePage() {

  const classes = useStyles()
  const { theme } = React.useContext(ThemeContext)
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [runState, setRunState] = React.useState('notRunning')
  const [codeLanguage, setCodeLanguage] = React.useState(defaultLanguage)
  const [textEditorData, setTextEditorData] = React.useState()
  const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false })

  React.useEffect(() => {
    if (actionState === ActionStates.inProgress) {
      setRunState('running')
    } else if(actionState === ActionStates.done && response.severity === "success")  {
      response.json.wasError ? setRunState('error') : setRunState('finished')
      setTextArea({ ...textArea, value: response.json, toUpdate: true });
    }
  },[actionState]);

  function onLanguageChange(event) {
    onClearConsole()
    setCodeLanguage(event.target.value);
  }

  async function onRunCode() {
    setAction({
      function: RunCodeController.execute,
      args: [{
        language: codeLanguage,
        code: textEditorData,
        unitTests: "runcodepage",
        executeTests: false
      }],
    })
  }

  function onClearConsole() {
    if(runState !== 'notRunning') {
      setRunState('notRunning');
      setTextArea('cls')
    }
  }

  if(actionState === ActionStates.clear || actionState === ActionStates.inProgress ||
    actionState === ActionStates.done && response.render) {
    return(
      <div className={classes.layout}>
        {actionState === ActionStates.done && response.message && 
            <CustomizedSnackbars message={response.message} severity={response.severity} />}
        <ReflexContainer orientation="vertical" style={{height: "85vh"}}>

          <ReflexElement className="left-pane"
            minSize="265"
          >
            <Toolbar className={classes.runCodetoolbar} variant="dense">
              <Button 
                className={classes.runButton}
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
                  value={codeLanguage}
                  onChange={event => onLanguageChange(event)}
                >
                  {Object.keys(languageLabelMappings).map(key => {
                    return <option value={key} key={key}>{languageLabelMappings[key]}</option>
                  })}
                </Select>
              </FormControl>
            </Toolbar>
            <RunCodeTextEditor theme={theme} codeLanguage={codeLanguage} setTextEditorData={setTextEditorData} />
          </ReflexElement>

          <ReflexSplitter/>

          <ReflexElement className="right-pane"
            minSize="250"
          >
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
                {runState === 'error' && (
                  <Paper className={classes.runStatePaper} style={{color:'#ffffff',backgroundColor:'#d9534f'}}>
                    Compile Error
                  </Paper>
                )}
              </Box>
              <Button className={classes.clearButton}
                id="clearConsoleButton"
                variant="contained"
                onClick={onClearConsole}
                style={{minWidth: 125}}
              >
                Clear Console
              </Button>
            </Toolbar>
            <OutputTextEditor theme={theme} textArea={textArea} setTextArea={setTextArea} />
          </ReflexElement>

        </ReflexContainer>
        <Footer />
      </div>
    )
  } else {
    return <DefaultErrorMessage message={ response.message } />
  }

}