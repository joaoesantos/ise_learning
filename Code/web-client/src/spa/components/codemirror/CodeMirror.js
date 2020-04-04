// react
import React, { Component } from 'react';
// codemirror
import cm from 'codemirror';
import 'codemirror/lib/codemirror.css';
// themes
import 'codemirror/theme/monokai.css';
import 'codemirror/theme/neat.css';
// languages
import 'codemirror/mode/markdown/markdown.js';
import 'codemirror/mode/clike/clike.js'; // mode: text/x-java (Java, Kotlin), csharp (C#)
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/mode/python/python.js';

export default class CodeMirror extends Component {
  constructor(props) {
    super(props);
  };

  // is invoked immediately after a component is mounted (inserted into the tree)
  componentDidMount = () => {
    this.editor = cm(this.instance, this.props.options);
  };

  componentDidUpdate = (prevProps) => {
    if(prevProps !== this.props)
      this.editor.setValue(this.props.options.value)
  };
  

  render = () => {
    console.log("options:",this.props.options)
    return (
    <div>
      <div ref={(ref) => this.instance = ref} />
    </div>
  );
  }
}
