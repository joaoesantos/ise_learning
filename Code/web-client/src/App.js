// react
import React from 'react';
import { HashRouter as Router, Switch, Route} from 'react-router-dom'
// material-ui components
import { makeStyles } from '@material-ui/core/styles'
// page components
import Navbar from './spa/components/navigation/Navbar.js'
import Home from './spa/components/home/HomePage.js'
import Footer from './spa/components/footer/Footer.js'
import RunCode from './spa/components/runCode/RunCodePage.js'
import SignIn from './spa/components/signInLogin/SignInPage.js'
import Login from './spa/components/signInLogin/LoginPage.js'
import UserProfile from './spa/components/UserProfile'
import Challenge from './spa/components/challenge/ChallengePage.js'
import ChallengeListPage from './spa/components/challenge/ChallengeListPage.js'
import Questionnaire from './spa/components/questionnaire/QuestionnairePage.js'
import QuestionnaireAnswerListPage from './spa/components/questionnaire/QuestionnaireAnswerListPage'
import QuestionnaireAnswerPage from './spa/components/questionnaire/QuestionnaireAnswerPage'
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline'

const useStyles = makeStyles((theme) => ({
  layout: {
    minWidth: 570,
  },
}));

function App() {

  const [isAuthed, setAuth] = React.useState(true);
  const [credentials, setCredentials] = React.useState('dXNlcjE6dXNlcjE=');

  const classes = useStyles();
  return (
    <Router>
      <div className={classes.layout}>
        <CssBaseline />
        <Navbar isAuthed={isAuthed} setAuth={setAuth}/>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/runCode" component={RunCode}/>
          <Route path="/login" render={() => <Login isAuthed={isAuthed} setAuth={setAuth} setCredentials={setCredentials}/>} />
          <Route path="/signIn" render={() => <SignIn isAuthed={isAuthed} setAuth={setAuth} setCredentials={setCredentials}/>} />
          <Route exact path="/profile" render={() => <UserProfile isAuthed={isAuthed} credentials={credentials}/>} />
          <Route path="/challenges" render={() => <Challenge isAuthed={isAuthed} />} credentials={credentials}/>
          <Route path="/listChallenges" render={() => <ChallengeListPage isAuthed={isAuthed} />} credentials={credentials}/>
          <Route path="/questionnaires" render={() => <Questionnaire isAuthed={isAuthed} credentials={credentials}/>} />
          <Route exact path="/questionnaireAnswer" render={() => <QuestionnaireAnswerListPage isAuthed={isAuthed} credentials={credentials}/>} />
          <Route path="/questionnaireAnswer/:id" render={() => <QuestionnaireAnswerPage isAuthed={isAuthed} credentials={credentials}/>} />
        </Switch>
        <Footer />
      </div>
    </Router>
  );
}

export default App;

