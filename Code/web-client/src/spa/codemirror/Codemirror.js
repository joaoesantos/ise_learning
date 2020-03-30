'use strict';

// react
import React, { Component } from 'react';
// codemirror
import codemirror from 'codemirror';
import 'codemirror/lib/codemirror.css';
// themes
import 'codemirror/theme/monokai.css';
import 'codemirror/theme/neat.css';
// languages
import 'codemirror/mode/clike/clike.js'; // mode: text/x-java (Java, Kotlin), csharp (C#)
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/mode/python/python.js';

export default class Codemirror extends Component {
  constructor(props) {
    super(props);
    this.codemirrorRef = React.createRef(); // access DOM nodes
  };
  componentDidMount = () => {
    this.codeMirror = codemirror(this.editor, this.props.options);
  };

  render = () => (
    <div>
      <div ref={self => this.editor = self} />
    </div>
  );
}
