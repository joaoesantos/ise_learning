// react
import React from 'react';
import { BrowserRouter as Router, Switch, Route} from 'react-router-dom';
// navbar function
import Navbar from './spa/navigation/Navbar.js'
// pages function
import Home from './spa/home/HomePage.js';
import Challenge from './spa/challenge/ChallengePage.js';
import Questionnaire from './spa/questionnaire/QuestionnairePage.js';
import RunCode from './spa/runCode/RunCodePage.js';
// footer function
import Footer from './spa/Footer.js';
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
  layout: {
    height:'100vh',
  },
}));

function App() {
  const classes = useStyles();
  return (
    <Router>
      <div className={classes.layout}>
        <CssBaseline />
        <Navbar />
        <Switch>
          < Route path="/" exact component={Home} />
          < Route path="/challenges" component={Challenge} />
          < Route path="/questionnaires" component={Questionnaire} />
          < Route path="/runCode" component={RunCode} />
        </Switch>
        <Footer />
      </div>
    </Router>
  );
}

export default App;

