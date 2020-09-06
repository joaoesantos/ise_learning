// react
import React, { Component } from 'react'
// material-ui components
import Paper from '@material-ui/core/Paper'
import { withStyles } from '@material-ui/core/styles'
// codemirror
import codemirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/markdown/markdown.js'
import 'codemirror/theme/neat.css'
import 'codemirror/theme/monokai.css'

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
    super(props);
  };

  // is invoked immediately after a component is mounted (inserted into the tree)
  componentDidMount = () => {
    this.editor = codemirror(this.instance, 
      {
        readOnly: true,
        mode: 'markdown',
        //theme:"neat"
      }
    );
    const editorHeigth = this.props.editorHeigth ? this.props.editorHeigth : 700
    const editorWidth = this.props.editorWidth ? this.props.editorWidth : 100
    this.editor.setSize(`${editorWidth}%`, editorHeigth);
  };

  // is invoked immediately after props change
  componentDidUpdate(prevProps) {
    if(prevProps !== this.props) {
      if(prevProps !== this.props && prevProps.theme.palette.type !== this.props.theme.palette.type) {
        this.editor.setOption("theme", this.props.theme.palette.type === "light" ? "neat" : "monokai")
      }
      if(this.props.textArea.toUpdate) {
        this.props.setTextArea({...this.props.textArea, toUpdate: false});
        const _this = this.props.textArea.value;
        const oldText = this.editor.doc.getValue();
        
        this.editor.setValue(`${oldText}## Finished in ${_this.executionTime} ms${_this.textSufix ? ` - ${_this.textSufix}` : ""}\n${_this.rawResult}\n\n`);
      }
      if(this.props.runState !== 'running' && this.props.textArea === 'cls') {
        this.editor.setValue('');
      }
    }
  }

  render = () => {
    const { classes } = this.props;
    return (
      <Paper variant="outlined" square elevation={1} >
        <div ref={(ref) => this.instance = ref} />
      </Paper>
    )
  }
}

export default withStyles(styles)(OutputTextEditor);