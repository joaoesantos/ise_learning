// react
import React from 'react';
import { BrowserRouter as Router, Switch, Route} from 'react-router-dom';
// material-ui components
import { makeStyles } from '@material-ui/core/styles';
// page components
import Navbar from './spa/components/navigation/Navbar.js'
import SignIn from './spa/components/signInLogin/SignInPage.js';
import Login from './spa/components/signInLogin/LoginPage.js';
import Home from './spa/components/home/HomePage.js';
import Challenge from './spa/components/challenge/ChallengePage.js';
import Questionnaire from './spa/components/questionnaire/QuestionnairePage.js';
import RunCode from './spa/components/runCode/RunCodePage.js';
import Footer from './spa/components/footer/Footer.js';
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline';

const useStyles = makeStyles((theme) => ({
  layout: {
    minWidth: 570,
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
          <Route path="/" exact component={Home} />
          <Route path="/login" component={Login} />
          <Route path="/signIn" component={SignIn} />
          <Route path="/challenges" component={Challenge} />
          <Route path="/questionnaires" component={Questionnaire} />
          <Route path="/runCode" component={RunCode} />
        </Switch>
        <Footer />
      </div>
    </Router>
  );
}

export default App;

