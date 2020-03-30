'use strict';

// react
import React from 'react';
// material-ui components
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Toolbar from '@material-ui/core/Toolbar';
// styles
import { makeStyles } from '@material-ui/core/styles';
// codemirror
import Codemirror from '../codemirror/Codemirror.js';
import {map as HelloWorld } from '../codemirror/HelloWorld.js';

const useStyles = makeStyles((theme) => ({
  layout: {
    flexGrow: 1,
    height:'85vh',
  },
  container: {
    padding: theme.spacing(0),
  },
  editorToolbar: {
    paddingLeft: theme.spacing(1),
    background: '#ffffff',
    borderBottom: `1px solid ${theme.palette.divider}`,
  },
  outputToolbar: {
    paddingLeft: theme.spacing(1),
    background: '#f5f5f5',
    borderBottom: `1px solid ${theme.palette.divider}`,
  },
  button: {
    margin: theme.spacing(1),
    textTransform:"none",
  }
}));

export default function RunCode() {

  const [language, setLanguage] = React.useState('java');
  const handleChangeLanguage = event => {
    setLanguage(event.target.value);
    console.log(language)
  }

  const classes = useStyles();
  return(
    <div className={classes.layout}>
        <Container className={classes.container} maxWidth={false}>
          <Grid
            container
            direction="row"
            justify="center"
            alignItems="flex-start"
            spacing={0}
            >
              {/* text editor */}
              <Grid item xs={7}> {/* text editor grid size */}
                <Grid>
                  <Toolbar className={classes.editorToolbar} variant="dense">
                    <Button className={classes.button}
                    id="runCode"
                    variant="contained"
                    style={{backgroundColor:'#6dab00'}}
                    >
                      Run Code
                    </Button>
                    <FormControl variant="standard" >
                      <Select
                      id="languageSelect"
                      native
                      onChange={handleChangeLanguage}
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
                <Grid>
                  <Paper variant="outlined" square elevation={1}>
                    <Codemirror id="textEditor" options={{
                    value: HelloWorld.get(language).value,
                    lineNumbers: true,
                    matchBrackets: true,
                    mode:HelloWorld.get(language).mode, 
                    theme:"monokai",
                    }} />
                  </Paper>
                </Grid>
              </Grid>
              {/* output */}
              <Grid item xs={5}> {/* output grid size */}
                <Grid>
                  <Toolbar className={classes.outputToolbar} variant="dense">
                    <Typography align="left" color="textPrimary">
                      Output:
                    </Typography>
                    <Paper
                    elevation={1}
                    style={{backgroundColor:'#6dab00'}}
                    >
                      Finished
                    </Paper>
                    <Button className={classes.button} 
                    id="clearConsole"
                    variant="contained"
                    >
                      Clear Console
                    </Button>
                  </Toolbar>
                </Grid>
                <Grid>
                  <Paper variant="outlined" square elevation={1} >
                    <Codemirror id="outputEditor" options={{
                    readOnly: true,
                    value:"Hello World!",
                    theme:"neat"
                    }} />
                  </Paper>
                </Grid>
              </Grid>
          </Grid>
      </Container>
    </div>
  )
};