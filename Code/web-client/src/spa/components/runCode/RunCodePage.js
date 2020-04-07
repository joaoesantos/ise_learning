// react
import React from 'react';
// material-ui components
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
// codemirror components
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'

const useStyles = makeStyles((theme) => ({
  layout: {

  },
  container: {
    padding: theme.spacing(0),
  },
}));

export default function RunCode() {

  const [result, setResult] = React.useState();
  const [runState, setRunState] = React.useState('notRunning');
  var runCodeFuncs = {
    updateRunningState: function(runState){ setRunState({runState: runState})},
    sendResult: function(result){ setResult({result: result})}
  }

  const classes = useStyles();
  return(
    <div className={classes.layout}>
        <Container className={classes.container} maxWidth={false}>
          <Grid container>
            <Grid item xs={7}>
              <RunCodeTextEditor runCodeFuncs={runCodeFuncs}/>
            </Grid>
            <Grid item xs={5}>
              <OutputTextEditor runState={runState} output={result} runCodeFuncs={runCodeFuncs}/>
            </Grid>
          </Grid>
      </Container>
    </div>
  )
};