// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
// custom components
import Tabs from '../Tabs'
import ChallengeStatement from './ChallengeStatement'
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'

const useStyles = makeStyles(theme => ({
    layout: { },
}));

export default function ChallengePage(props) {
    const classes = useStyles();
    const id = props.match.params.id

    const [text1, setText1] = React.useState({ title: 'problem1', text: 'hahahahahahahahahahahahahahahahahaha' });

    const [codeLanguage, setCodeLanguage] = React.useState('java');
    const [yourSolution, setYourSolution] = React.useState('public class code { \r\n public static void main(String[] args) {  \r\n  System.out.println(\"Hello World\");  \r\n} public static void test() {System.out.println(\"<<Test>>\");}  \r\n}');
    const [ourSolution, setOurSolution] = React.useState('public class code { \r\n public static void main(String[] args) {  \r\n  System.out.println(\"Hello World\");  \r\n} public static void test() {System.out.println(\"<<Test>>\");}  \r\n}');
    const [textArea, setTextArea] = React.useState({ value: '', toUpdate: false });

    const [yourTests, setYourTests] = React.useState('import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {code.test(); Assert.assertTrue(true);}}');
    const [ourTests, setOurTests] = React.useState('import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {code.test(); Assert.assertTrue(true);}}');


    const tabComponentsChallengeStatement = [
        <ChallengeStatement challengeStatement={text1} />,
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={ourSolution} setTextEditorData={setOurSolution} readOnly={true} />
    ];

    const tabLabelsChallengeStatement = [
        "Challenge Statement", "Our Solution"
    ]

    const tabComponentsTests = [
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourTests} setTextEditorData={setYourTests}/>,
        <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={ourTests} setTextEditorData={setOurTests} readOnly={true} />
    ];

    const tabLabelsTests = [
        "Your Tests", "Our Tests"
    ]

    return (
        <React.Fragment>
            <div className={classes.layout}>
                <h1>Challenges</h1>
            </div>
            <Grid container spacing={3}>
                <Grid item xs={12} sm={6}>
                    <Tabs useStyles={useStyles} childComponents={tabComponentsChallengeStatement} tabLabels={tabLabelsChallengeStatement} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <RunCodeTextEditor codeLanguage={codeLanguage} textEditorData={yourSolution} setTextEditorData={setYourSolution} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Tabs useStyles={useStyles} childComponents={tabComponentsTests} tabLabels={tabLabelsTests} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <OutputTextEditor textArea={textArea} setTextArea={setTextArea} />
                </Grid>
            </Grid>
        </React.Fragment>
    )
};