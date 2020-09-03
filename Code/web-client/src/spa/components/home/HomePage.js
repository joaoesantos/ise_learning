// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Container from '@material-ui/core/Container'
import Divider from '@material-ui/core/Divider'
import Grid from '@material-ui/core/Grid'
import Link from '@material-ui/core/Link'
import { makeStyles } from '@material-ui/core/styles'
import Typography from '@material-ui/core/Typography'
// IS E-Learning images
import mainImage from '../../images/ISELearning.png'
import scriptIcon from '../../images/icons/services/color/scriptYlw.png'
// cards components
import BusinessCard from './BusinessCard.js'
import MotivationCard from './MotivationCard.js'
import ChallengesCard from './ChallengesCard.js'
import QuestionnairesCard from './QuestionnairesCard.js'
import CodeLanguagesCard from './CodeLanguagesCard.js'
import CodeExecutionEnvironmentsCard from './CodeExecutionEnvironmentsCard.js'
//import OpenSourceCard from './OpenSourceCard.js';
import '../../images/wickedcss.min.css'

const useStyles = makeStyles((theme) => ({
  layout: {
    flexGrow: 1,
    background: '#ffffff',
    textAlign: 'center',
  },
  mainLogoGrid: {
    width: '100%',
    height: '450px',
    backgroundColor: '#f0bc5e',
  },
  mainContainer: {
    maxWidth: '95%',
  },
  mainGridContainer: {  
    maxWidth: '75%',
    padding: theme.spacing(2),
  },
  motivationContainer: {
    padding: theme.spacing(10),
    maxWidth:'85%',
  },
  businessGrid: {
    padding: theme.spacing(4),
  },
  challQuestContainer: {
    padding: theme.spacing(10),
    maxWidth:'75%',
  },
  langEnvContainer: {
    padding: theme.spacing(10),
    maxWidth:'85%',
  },
  textTitle: {
    paddingBottom: theme.spacing(2),
    color:'#be5041',
  },
  textContent: {
    fontSize:15,
    color:"#808080",
  }
}));

export default function HomePage() {

  const classes = useStyles()
  const motivationRef = React.useRef(null)
  const challQuestRef = React.useRef(null)
  const langEnvRef = React.useRef(null)

  const scrollToRef = (ref) => window.scrollTo({ top:ref.current.offsetTop, behavior:'smooth' })

  return (
    <div className={classes.layout} >
        <Grid 
        className={classes.mainLogoGrid}
        container
        direction="row"
        justify="center"
        alignItems="center"
        >
          <img src={mainImage} className='slideLeft'/>
        </Grid>
        <Container className={classes.mainContainer}>
          <Container className={classes.mainGridContainer}>
              <Grid 
              container 
              direction="row"
              justify="space-evenly"
              alignItems="center"
              spacing={3}
              >
                <Grid item>
                <MotivationCard handleOnClick={()=>scrollToRef(motivationRef)} />
                </Grid>
                <Grid item>
                  <ChallengesCard handleOnClick={()=>scrollToRef(challQuestRef)}/>
                </Grid>
                <Grid item>
                  <QuestionnairesCard handleOnClick={()=>scrollToRef(challQuestRef)}/>
                </Grid>
                <Grid item>
                  <CodeLanguagesCard handleOnClick={()=>scrollToRef(langEnvRef)}/>
                </Grid>
                <Grid item>
                  <CodeExecutionEnvironmentsCard handleOnClick={()=>scrollToRef(langEnvRef)}/>
                </Grid>
              </Grid>
        </Container>
        <Divider variant="middle" />
        <Container ref={motivationRef} className={classes.motivationContainer}>
          <Typography className={classes.textTitle} variant="h4">
              Who are we?
          </Typography>
          <Typography className={classes.textContent} variant="h6">
            We are 3 friends (for most of the time, when we agree on semantic) doing our final IT project.<br/>
            Our goal is simple and honest, not only we want to provide an e-learning platform that can be useful in academic environment, professional interviews, or even for just a programming enthusiast who wants to learn more, but also we want to do it  in openly manner, so that it can be freely accessed, used, changed, and shared by everyone.
          </Typography>
          <Grid className={classes.businessGrid}
          container
          direction="row"
          justify="center"
          alignItems="center"
          spacing={3}
          >
            <Grid item>
              <BusinessCard
              avatar={require('..\\..\\images\\avatar\\AndreOliveiraAvatar.jpg')}
              name="André Oliveira"
              email="alaqo@hotmail.com"
              text="I just want to make mustaches great again."
              href="https://www.linkedin.com/in/andré-oliveira-13078a85"
              />
            </Grid>
            <Grid item>
              <BusinessCard
                avatar={require('..\\..\\images\\avatar\\RodrigoLealAvatar.jpg')}
                name="Rodrgio Leal"
                email="rodrigomfl@hotmail.com"
                text="I love programming, the challenge of solving complex problems with robust solutions is something which motivates me every day."
                href="https://www.linkedin.com/in/rodrigo-leal-0ba52452/"
              />
            </Grid>
            <Grid item>
              <BusinessCard
              avatar={require('..\\..\\images\\avatar\\JoaoSantosAvatar.jpg')}
              name="João Santos"
              email="joao.silva.santos1988@gmail.com"
              text="I'm the project leader, so i live to write documentation. I'm proficient in javadoc, cdock, kdoc. Well pretty much all docs. And LaTex"
              href="https://www.linkedin.com/in/jo%C3%A3o-santos-0449a1b8/"
              />
            </Grid>
          </Grid>
        </Container>
        <Divider variant="middle" />
        <Container ref={challQuestRef} className={classes.challQuestContainer}>
          <Typography className={classes.textTitle} variant="h4">
              {"Challenges & Questionnaires"}
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={6} style={{textAlign:'right'}}>
              <Typography className={classes.textContent} variant="h6" component="p">
                If you want to ace the coding interviews, or rock in school
                being well-versed in all common data structures and popular problem-solving methods is key. 
                We have over 10 questions for you to practice, and if that ins't enought, 
                you can always share what you learned, and come and help us build a active tech comunity.
            </Typography>
            <Typography className={classes.textContent} variant="h6" component="p">
                Create problems, solve them and challenge your peers!
            </Typography>
            <Link style={{color:"#db9e07"}} component={RouterLink} to="/listChallenges">
                {"View Challenges >"}
            </Link>
            </Grid>
            <Grid item xs={6}>
              <img src={scriptIcon} width={150} height={150} className='pulse'/>
            </Grid>
          </Grid>
        </Container>
        <Divider variant="middle" />
        <Container ref={langEnvRef} className={classes.langEnvContainer}>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Typography className={classes.textTitle} variant="h4" style={{textAlign:'left'}}>
                We Speak 5 Languages
              </Typography>
              <Typography className={classes.textContent}  variant="h6" style={{textAlign:'left'}}>
                There's nothing more frustrating than opening an interview prep book, 
                only to find a bunch of solutions in a programming language that you don't know. 
                That is you can write your solutions in 5 popular languages!
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Typography className={classes.textTitle} variant="h4" style={{textAlign:'left'}}>
                Code Execution Environment
              </Typography>
              <Typography className={classes.textContent} variant="h6" style={{textAlign:'left'}}>
                In an ideal world, you'd prepare for coding interviews by writing out solutions to problems 
                in your language of choice, getting some hints if necessary, running your code against test cases, 
                and looking at solutions when done.
                We've turned that ideal world into the real world. Pick a language. Read the prompt. 
                Write your solution. Run your code. Check the output. Pass the tests. 
                View our solution. All within the same workspace.
              </Typography>
            </Grid>
          </Grid>
        </Container>
      </Container>
    </div>
  )
}
