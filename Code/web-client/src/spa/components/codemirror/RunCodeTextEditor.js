// react
import React, { Component } from 'react';
// material-ui components
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import { withStyles, makeStyles } from '@material-ui/core/styles';
// codemirror
import codemirror from 'codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/clike/clike.js'; // mode: text/x-java (Java, Kotlin), csharp (C#)
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/mode/python/python.js';
import 'codemirror/theme/neat.css';
import 'codemirror/addon/edit/closebrackets.js'
import 'codemirror/addon/edit/matchbrackets.js'
// client side configurations
import { CodeMirrorOptions } from '../../clientSideConfig';

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
    }
});


class RunCodeTextEditor extends Component {
    constructor(props) {
        super(props);
    };
  
    // is invoked immediately after a component is mounted (inserted into the tree)
    componentDidMount = () => {
        
        this.editor = codemirror(this.instance, 
            {
                lineNumbers: true,
                matchBrackets: true,
                value: (this.props.textEditorData === undefined) ? CodeMirrorOptions.get(this.props.codeLanguage).value : this.props.textEditorData, 
                mode:CodeMirrorOptions.get(this.props.codeLanguage).mode, 
                theme:"neat",
                smartIndent: true,
                matchClosing: true, 
                autoCloseBrackets: true,
                readOnly: (this.props.readOnly === true) ? true: false
            }
        );
        this.props.setTextEditorData(this.editor.doc.getValue()); // after mount signal father what it's in text editor
        this.editor.setSize("100%", 700);
        this.editor.on('change', () => {
            this.props.setTextEditorData(this.editor.doc.getValue())
        })
    };

    // is invoked immediately after props change
    componentDidUpdate(prevProps) {
        if(prevProps !== this.props) {
            if(prevProps.codeLanguage !== this.props.codeLanguage) {
                this.editor.setValue(CodeMirrorOptions.get(this.props.codeLanguage).value);
            }
        }
    }
  
    render = () => {
        const { classes } = this.props;
        return (
            <Paper variant="outlined" square elevation={1} >
                <Box>
                    {this.props.actions && this.props.actions.map(a =>
                        <Button className={classes.runButton}
                            id={a.id}
                            variant="contained"
                            onClick={() => a.function()}
                        >
                            {a.title}
                        </Button>
                    )}
                </Box>
                <div ref={(ref) => this.instance = ref} />
            </Paper>
        )
    }
}
  
export default withStyles(styles)(RunCodeTextEditor);