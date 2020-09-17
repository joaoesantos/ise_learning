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
import ChallengeList from '../challenge/ChallengeListPage.js'
import Questionnaire from '../questionnaire/QuestionnairePage.js'
import QuestionnaireAnswerList from '../questionnaire/QuestionnaireAnswerListPage'
import QuestionnaireAnswer from '../questionnaire/QuestionnaireAnswerPage'
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'

<<<<<<< HEAD
//TODO rever caminhos por exemplo QuestionnairePage é para responder ao questionario
=======
import SolveChallengePage from '../challenge/SolveChallengePage'

>>>>>>> update on new re-flexed ChallengePage
export default function Routes() {
    return (
        <>
            <Navbar />
            <Switch>
                <Route exact path="/test/:challengeId" render={({match}) => <SolveChallengePage match={match} configKey={"challenge"} />} />

                <Route exact path="/" component={Home} />
                <Route exact path="/runCode" component={RunCode} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/signIn" component={SignIn} />
                <Route exact path="/profile" component={UserProfile} />
                <Route exact path="/listChallenges" component={ChallengeList} />
                <Route exact path="/challenges/:challengeId" render={({match}) => <Challenge match={match} configKey={"challenge"} />} />
                <Route exact path="/newChallenge" render={({match}) => <Challenge match={match} configKey={"newChallenge"} />} />
                <Route exact path="/challengeAnswers/:challengeId/answers/users/:userId" render={({match}) => <Challenge match={match} configKey={"challengeAnswer"} />} />
                <Route exact path="/questionnaires" component={Questionnaire} />
                <Route exact path="/questionnaireInstances/solve/:uuid" component={Questionnaire} />
                <Route exact path="/questionnaireAnswer" component={QuestionnaireAnswerList} />
                <Route exact path="/questionnaireAnswer/:id" component={QuestionnaireAnswer} />
                <Route path="*" render={() => <DefaultErrorMessage message={"404 | Not Found"} /> } />
            </Switch>
            <Footer />
        </>
    )
}

