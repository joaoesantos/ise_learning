// react
import React, { Component } from 'react';
// material-ui components
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Select from '@material-ui/core/Select';
import Toolbar from '@material-ui/core/Toolbar';
import { withStyles } from '@material-ui/core/styles';
// codemirror
import codemirror from 'codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/clike/clike.js'; // mode: text/x-java (Java, Kotlin), csharp (C#)
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/mode/python/python.js';
import 'codemirror/theme/monokai.css';
// client side configurations
import { codeMirrorDefault, CodeMirrorOptions } from '../../clientSideConfig';
// controller
import { runCodeCtrl } from '../../controllers/runCodeCtrl.js'

const styles = theme => ({
    toolbar: {
        paddingLeft: theme.spacing(1),
        background: '#ffffff',
        borderBottom: `1px solid ${theme.palette.divider}`,
        justifyContent: "space-between"
    },
    button: {
        margin: theme.spacing(1),
        textTransform:"none",
        backgroundColor:'#6dab00', // cor isel -> '#963727'
    }
  });
  
  class RunCodeTextEditor extends Component {
    constructor(props) {
        super(props);

        this.state = {
            codeLanguage: codeMirrorDefault,
        }
    };

    onLanguageChange = event => {
        this.setState({codeLanguage: event.target.value}); // carefull, setState is asynchronous...
        this.editor.setOption("mode", CodeMirrorOptions.get(event.target.value).mode);
        this.editor.setValue(CodeMirrorOptions.get(event.target.value).value);
    }

    onRunCode = async () => {
        let result = await runCodeCtrl(this.state.codeLanguage, this.editor.doc.getValue());
        console.log(result.environment, result.executionTime, result)
    }
  
    // is invoked immediately after a component is mounted (inserted into the tree)
    componentDidMount = () => {
        this.editor = codemirror(this.instance, 
            {
                lineNumbers: true,
                matchBrackets: true,
                value:CodeMirrorOptions.get(this.state.codeLanguage).value, 
                mode:CodeMirrorOptions.get(this.state.codeLanguage).mode, 
                theme:"monokai",
                matchClosing: true, 
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
                        <Button className={classes.button}
                        id="runCodeButton"
                        variant="contained"
                        onClick={this.onRunCode}
                        >
                            Run Code
                        </Button>
                        <FormControl variant="standard" className={classes.form}>
                            <Select
                            id="languageSelect"
                            native
                            onChange={this.onLanguageChange}
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
                    <Paper variant="outlined" square elevation={1} >
                        <div ref={(ref) => this.instance = ref} />
                    </Paper>
                </Grid>
            </div>
        )
    }
}
  
export default withStyles(styles)(RunCodeTextEditor);