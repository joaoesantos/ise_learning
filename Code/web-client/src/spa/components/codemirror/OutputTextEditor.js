// react
import React, { Component } from 'react';
// material-ui components
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
// codemirror
import codemirror from 'codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/markdown/markdown.js';
import 'codemirror/theme/neat.css';

const styles = theme => ({
  toolbar: {
    paddingLeft: theme.spacing(1),
    background: '#f5f5f5',
    borderBottom: `1px solid ${theme.palette.divider}`,
    justifyContent: "space-between"
  },
  button: {
    margin: theme.spacing(1),
    textTransform:"none",
  },
  paper: {
    elevation:1,
    borderStyle:'solid',
  },
});

class OutputTextEditor extends Component {
  constructor(props) {
    super(props); // props are read-only
  };

  onClearConsole = () => {
    if(this.props.runState.runState !== 'notRunning') {
      this.props.runCodeFuncs.updateRunningState.call(this,'notRunning');
      this.editor.setValue('');
    }
  }

  // is invoked immediately after a component is mounted (inserted into the tree)
  componentDidMount = () => {
    this.editor = codemirror(this.instance, 
      {
        readOnly: true,
        mode: 'markdown',
        //theme:"neat"
      }
    );
    this.editor.setSize("100%", 700);
  };

  componentDidUpdate(prevProps) {
    if(prevProps.runState !== this.props.runState) {
      console.log('i was here')
      if(this.props.runState.runState === 'finished' && this.props.output.result) {
        const _this = this.props.output.result;
        const oldText = this.editor.doc.getValue();
        this.editor.setValue(oldText === '' ? `## Finished in ${String(_this.executionTime)} ms\n${_this.result}\n\n` 
                                            : `${oldText} ## Finished in ${_this.executionTime} ms\n${_this.result}\n\n`);
      }
    }
  }

  render = () => {
    const { classes } = this.props;
    return (
      <div>
          <Grid>
            <Toolbar className={classes.toolbar} variant="dense">
              <Box display="flex">
                <Typography style={{paddingRight:5}}>
                  Output:
                </Typography>
                {this.props.runState.runState === 'running' && (
                  <Paper className={classes.paper} style={{color:'#ffffff',backgroundColor:'#0082C4'}}>
                    Running...
                  </Paper>
                )}
                {this.props.runState.runState === 'finished' && (
                  <Paper className={classes.paper} style={{color:'#ffffff',backgroundColor:'#5cb85c'}}>
                    Finished
                  </Paper>
                )}
                {this.props.runState.runState === 'compileError' && (
                  <Paper className={classes.paper} style={{color:'#d9534f',backgroundColor:'#17b033'}}>
                    Compile Error
                  </Paper>
                )}
              </Box>
              <Button className={classes.button}
              id="clearConsoleButton"
              variant="contained"
              onClick={this.onClearConsole}
              >
                Clear Console
              </Button>
            </Toolbar>
          </Grid>
          <Grid>
            <Paper variant="outlined" square elevation={1} >
              <div ref={(ref) => this.instance = ref} />
            </Paper>
          </Grid>
      </div>
    )
  }
}

export default withStyles(styles)(OutputTextEditor);