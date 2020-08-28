// react
import React, {useState} from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import Link from '@material-ui/core/Link'
import Paper from '@material-ui/core/Paper'
import Toolbar from '@material-ui/core/Toolbar'
import { makeStyles } from '@material-ui/core/styles'
import RepeatOne from '@material-ui/icons/RepeatOne'
// components
import ChallengeListTable from './ChallengeListTable'

import UseAction, { ActionStates } from '../../controllers/UseAction'
import challengeCtrl from '../../controllers/_challengeCtrl'

const useStyles = makeStyles((theme) => ({
  layout: {

  },
  mainContainer: {
    maxWidth: '90%',
    padding: theme.spacing(2),
  },
  toolbar: {
    background: '#ffffff',
    borderBottom: `1px solid ${theme.palette.divider}`,
    justifyContent: "space-between"
  },
  link: {
    paddingLeft: 15,
    color: '#db9e07',
    textDecoration: 'none',
    '&:hover, &:focus' : {
      color: '#be5041',
      textDecoration: 'none',
    }
  },
  randomChallengeButton: {
    margin: theme.spacing(1),
    textTransform:"none",
    color:'#ffffff',
    backgroundColor:'#db9e07',
    '&:hover' : {
        backgroundColor: '#be5041',
    }
},
}));

const ChallengeListPage = function(props){

  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
      //not Done || done but not rendering
    } else if (actionState === ActionStates.done &&
    action.render && action.render === true) {
      // redirect to create new challenge page
    } else {
      //not Done || done but not rendering
    }
  },[actionState]);

  const onRandomChallengeButton = () => {
    setAction({
      function: challengeCtrl.getRamdomChallenge,
      args: [props.credentials],
      render: false
    })
  }

  const onCreateChallengeButton = () => {
    // redirect para o create new challenge page
  }

  const classes = useStyles();
  return (
    <div className={classes.layout}>
      <Container className={classes.mainContainer}>
        <Grid 
        container 
        direction="row"
        justify="space-evenly"
        alignItems="center"
        spacing={3}
        >
          </Grid>
          <Grid item xs={12} sm={10}>
            <Toolbar className={classes.toolbar} variant="dense">
              <Button className={classes.randomChallengeButton}
              id="randomChallengeButton"
              variant="contained"
              onClick={onRandomChallengeButton}
              >
                Create challenge
              </Button>
              <Button className={classes.randomChallengeButton}
              endIcon={<RepeatOne />}
              id="randomChallengeButton"
              variant="contained"
              onClick={onRandomChallengeButton}
              >
                Pick one
              </Button>
            </Toolbar>
            <ChallengeListTable />
          <Grid item xs={12} sm={2}>
            <p>SPACE FOR</p>
            <p>RANDOM INFORMATION</p>
          </Grid>
        </Grid>
    </Container>
  </div>
  )

}

export default ChallengeListPage