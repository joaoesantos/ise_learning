// react
import React from 'react'
// material-ui components
import Box from '@material-ui/core/Box'
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import FormControl from '@material-ui/core/FormControl'
import Grid from '@material-ui/core/Grid'
import Paper from '@material-ui/core/Paper'
import Select from '@material-ui/core/Select'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'
// codemirror components
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'
// controller
import { runCodeCtrl } from '../../controllers/runCodeCtrl.js'
// authentication context
import { ThemeContext } from '../../context/ThemeContext'
// client side configurations
import { defaultLanguage } from '../../clientSideConfig'

const useStyles = makeStyles((theme) => ({
  layout: {},
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
      backgroundColor:'#4caf50',
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

  const { theme } = React.useContext(ThemeContext)
  const [runState, setRunState] = React.useState('notRunning')
  const [codeLanguage, setCodeLanguage] = React.useState(defaultLanguage);
  const [textEditorData, setTextEditorData] = React.useState()
  const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false });

  function onLanguageChange(event) {
    onClearConsole()
    setCodeLanguage(event.target.value);
  }

  async function onRunCode() {
    if(runState !== 'running') {
      setRunState('running');
      let result = await runCodeCtrl(codeLanguage, textEditorData);
      setRunState('finished');
      setTextArea({ ...textArea, value: result, toUpdate: true });
    }
  }

  function onClearConsole() {
    if(runState !== 'notRunning') {
      setRunState('notRunning');
      setTextArea('cls')
    }
  }

  const classes = useStyles();
  return(
    <div className={classes.layout}>
      <Container className={classes.container} maxWidth={false}>
        <Grid container>
          <Grid item xs={7}>
            <Grid>
              <Toolbar className={classes.runCodetoolbar} variant="dense">
                <Button 
                  className={classes.runButton}
                  color="success"
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
                  onChange={event => onLanguageChange(event)}
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
            <RunCodeTextEditor theme={theme} codeLanguage={codeLanguage} setTextEditorData={setTextEditorData} />
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
                <Button className={classes.clearButton}
                  id="clearConsoleButton"
                  variant="contained"
                  onClick={onClearConsole}
                >
                  Clear Console
                </Button>
              </Toolbar>
            </Grid>
            <OutputTextEditor theme={theme} textArea={textArea} setTextArea={setTextArea} />
          </Grid>
        </Grid>
      </Container>
    </div>
  )
};