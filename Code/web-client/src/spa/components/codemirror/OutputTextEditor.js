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
    backgroundColor:'#6dab00'
  }
});

class OutputTextEditor extends Component {
  constructor(props) {
    super(props);

    this.state = {
      finished: true,
    }
  };

  onUpdateOutpuyTextEditor = (output) => {
    this.editor.setValue(output);
    this.setState({finished: true});
    // this.setState(prevState => ({
    //   finished: !prevState.finished
    // }))
  }

  onClearConsole = () => {
    this.editor.setValue('');
    this.setState({finished: false});
  }

  // is invoked immediately after a component is mounted (inserted into the tree)
  componentDidMount = () => {
    this.editor = codemirror(this.instance, 
      {
        readOnly: true,
        value:"Hello World!",
        theme:"neat"
      }
    );
    this.editor.setSize("100%", 700);
  };

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
                {this.state.finished && (
                  <Paper className={classes.paper}>
                    Finished
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