// react
import React from 'react'
import { Switch, Route } from 'react-router-dom'

// page components
import Navbar from '../navigation/Navbar'
import Home from '../home/HomePage.js'
import Footer from '../footer/Footer.js'
import RunCode from '../runCode/RunCodePage.js'
import SignIn from '../signInLogin/SignInPage.js'
import Login from '../signInLogin/LoginPage.js'
import UserProfile from '../UserProfile'
import Challenge from '../challenge/ChallengePage.js'
import ChallengeListPage from '../challenge/ChallengeListPage.js'
import Questionnaire from '../questionnaire/QuestionnairePage.js'
import QuestionnaireAnswerListPage from '../questionnaire/QuestionnaireAnswerListPage'
import QuestionnaireAnswerPage from '../questionnaire/QuestionnaireAnswerPage'

export default function Routes() {
    return (
        <>
            <Navbar />
            <Switch>
                <Route exact path="/" component={Home} />
                <Route path="/runCode" component={RunCode} />
                <Route path="/login" component={Login} />
                <Route path="/signIn" component={SignIn} />
                <Route exact path="/profile" component={UserProfile} />
                <Route path="/challenges" component={Challenge} />
                <Route path="/listChallenges" component={ChallengeListPage} />
                <Route path="/questionnaires" component={Questionnaire} />
                <Route exact path="/questionnaireAnswer" component={QuestionnaireAnswerListPage} />
                <Route path="/questionnaireAnswer/:id" component={QuestionnaireAnswerPage} />
            </Switch>
            <Footer />
        </>
    )
}

