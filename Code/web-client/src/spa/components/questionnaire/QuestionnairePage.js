// react
import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';

import Challenge from '../challenge/Challenge'

// material-ui components
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import blue from '@material-ui/core/colors/blue';

import questionnaireCtrl from '../../controllers/questionnaireCtrl'

const useStyles = makeStyles(theme => ({
    layout: { },
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
      },
    button: {
       marginTop: theme.spacing(1),
       marginRight: theme.spacing(2),
    },
    stepper: {
        padding: theme.spacing(3, 1, 2, 1),
      },
    timer: {
        fontFamily: 'sans-serif',
	    color: '#fff',
	    display: 'inline-block',
	    textAlign: 'center',
	   fontSize: '12px',
    },
    timerElement: {
        padding: '5px',
	    borderRadius: '3px',
	    background: blue[600],
        display: 'inline-block',
        marginRight: theme.spacing(2)
    },
    timerValue: {
        padding: '2px',
        borderRadius: '3px',
        background: blue[900],
        display: 'inline-block'
    },
    timerText: {
        paddingTop: '5px',
        fontSize : '10px'
    }
}));

export default function QuestionnairePage(props) {
    
    const classes = useStyles();

    const [timer, setTimer] = useState(5000)
    const [activeChallenge, setActiveChallenge] = useState(0)
    const [challenges, setChallenges] = useState([
        {
            challengeId: 0,
            challengeText: 'Text0',
            unitTests: "unit tests0",
            code: "code0"
        },
        {
            challengeId: 1,
            challengeText: 'Text1',
            unitTests: "unit tests1",
            code: "code1"
        },
        {
            challengeId: 2,
            challengeText: 'Text2',
            unitTests: "unit tests2",
            code: "code2"
        }
    ])
    
    let seconds = ("0" + (Math.floor((timer / 1000) % 60) % 60)).slice(-2);
    let minutes = ("0" + Math.floor((timer / 60000) % 60)).slice(-2);
    let hours = ("0" + Math.floor((timer / 3600000) % 60)).slice(-2);

    useEffect(() => {
        let intervalId = null
        if(timer > 0) {
            intervalId = setInterval(() => setTimer(old => old - 1000), 1000);
        }

        return () => {
            clearInterval(intervalId)
        }

    }, [timer])

    

    const handlePrevious = function(){
        setActiveChallenge(old => old - 1)
    }

    const handleNext = function(){
        setActiveChallenge(old => old + 1)
    }

    return(
        <React.Fragment>
            <Paper className={classes.paper}>
            <Stepper activeStep={activeChallenge} className={classes.stepper}>
            {challenges.map((c, index) => (
              <Step key={index}>
                <StepLabel>{`Challenge #${index + 1}`}</StepLabel>
              </Step>
            ))}
          </Stepper>
          <div className={classes.timer}>
                    <h6>Time Left</h6>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{hours}</span>
                        <div className={classes.timerText}>Hours</div>
                    </div>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{minutes}</span>
                        <div className={classes.timerText}>minutes</div>
                    </div>
                    <div className={classes.timerElement}>
                        <span className={classes.timerValue}>{seconds}</span>
                        <div className={classes.timerText}>seconds</div>
                    </div>
                {/* Time Left {hours} H : {minutes} min : {seconds} s */}
            </div>
        <div className={classes.buttons}>
                  {activeChallenge !== 0 && (
                    <Button 
                        variant="contained"
                        color="primary"
                        onClick={handlePrevious} 
                        className={classes.button}
                    >
                      Previous
                    </Button>
                  )}
                  {activeChallenge < challenges.length - 1 && (
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleNext}
                        className={classes.button}
                    >
                        Next
                    </Button>
                  )}
                  
        </div>
            <Challenge challenge={challenges[activeChallenge]}></Challenge>
            {
                challenges[activeChallenge]
            }
            </Paper>
        </React.Fragment>
        // <div className={classes.layout}>
        //     <h1><a href='https://material-ui.com/getting-started/templates/checkout/'>
        //         Exemplo de como fazer mover entre challeges no questionnaire
        //     </a></h1>
        // </div>
    )
};