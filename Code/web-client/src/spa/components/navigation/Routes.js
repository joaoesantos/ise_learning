// react
import React from 'react'
import { Switch, Route } from 'react-router-dom'
// page components
import Navbar from '../navigation/Navbar'
import Home from '../home/HomePage'
import RunCode from '../runCode/RunCodePage'
import SignIn from '../signInLogin/SignInPage'
import Login from '../signInLogin/LoginPage'
import UserProfile from '../UserProfile'
// challenges
import Challenge from '../challenge/ChallengePage'
import ChallengeList from '../challenge/ChallengeListPage'
// questionnaires
import QuestionnaireList from '../questionnaire/QuestionnaireListPage'
import CreateEditQuestionnaire from '../questionnaire/CreateEditQuestionnairePage'
import QuestionnaireInstanceList from '../questionnaire/QuestionnaireInstanceListPage'
import QuestionnaireInstance from '../questionnaire/QuestionnairePage'
import QuestionnaireAnswer from '../questionnaire/QuestionnaireAnswerPage'
// notifications
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'

export default function Routes() {
    return (
        <>
            <Navbar />
            <Switch>
                <Route exact path="/" component={Home} />
                <Route exact path="/runCode" component={RunCode} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/signIn" component={SignIn} />
                <Route exact path="/profile" component={UserProfile} />
                <Route exact path="/listChallenges" component={ChallengeList} />
                <Route exact path="/challenges/:challengeId" render={({match}) => <Challenge match={match} configKey={"challenge"} />} />
                <Route exact path="/newChallenge" render={({match}) => <Challenge match={match} configKey={"newChallenge"} />} />
                <Route exact path="/questionnaires" component={QuestionnaireList} />
                <Route exact path="/createEditQuestionnaire/:questionnaireId" render={({match}) => <CreateEditQuestionnaire match={match} />} />
                <Route exact path="/questionnaireInstances/:questionnaireId" render={({match}) => <QuestionnaireInstanceList match={match} />} />
                <Route exact path="/questionnaireInstances/solve/:uuid" component={QuestionnaireInstance} />
                <Route exact path="/questionnaireAnswer/:questionnaireInstanceId" render={({match}) => <QuestionnaireAnswer match={match} />} />
                <Route path="*" render={() => <DefaultErrorMessage message={"404 | Not Found"} /> } />
            </Switch>
        </>
    )
}

