// react
import React from 'react';
import { HashRouter as Router, Switch, Route} from 'react-router-dom';
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
import UserProfile from './spa/components/UserProfile';
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline';

const useStyles = makeStyles((theme) => ({
  layout: {
    minWidth: 570,
  },
}));

function App() {

  const [isAuthed, setAuth] = React.useState(false);

  const classes = useStyles();
  return (
    <Router>
      <div className={classes.layout}>
        <CssBaseline />
        <Navbar isAuthed={isAuthed} setAuth={setAuth}/>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/login" render={() => <Login isAuthed={isAuthed} setAuth={setAuth} />} />
          <Route path="/signIn" render={() => <SignIn isAuthed={isAuthed} setAuth={setAuth} />} />
          <Route exact path="/newChallenge" render={({match}) => <Challenge isAuthed={isAuthed} match={match} configKey={"newChallenge"}/>} />
          <Route exact path="/challenges/:challengeId" render={({match}) => <Challenge isAuthed={isAuthed} match={match} configKey={"challenge"}/>} />
          <Route exact path="/challengeAnswers/:challengeId/answers/users/:userId" render={({match}) => <Challenge isAuthed={isAuthed} match={match} configKey={"challengeAnswer"}/>} />
          <Route path="/questionnaires" render={() => <Questionnaire isAuthed={isAuthed} />} />
          <Route path="/runCode" component={RunCode} />
          <Route exact path="/profile" render={() => <UserProfile isAuthed={isAuthed} />} />
        </Switch>
        <Footer />
      </div>
    </Router>
  );
}

export default App;

