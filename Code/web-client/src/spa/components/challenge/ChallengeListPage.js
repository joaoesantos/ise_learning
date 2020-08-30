// react
import React, { useContext, useState } from 'react'
// material-ui components
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import Toolbar from '@material-ui/core/Toolbar'
import { makeStyles } from '@material-ui/core/styles'
import RepeatOne from '@material-ui/icons/RepeatOne'
// components
import ChallengeListTable from './ChallengeListTable'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import challengeCtrl from '../../controllers/_challengeCtrl'
// authentication context
import { AuthContext } from '../../context/AuthContext'

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

const ChallengeListPage = function(props) {

  const { isAuthed } = useContext(AuthContext)
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
      //not Done || done but not rendering
    } else if (actionState === ActionStates.done &&
    action.render && action.render === true) {
      // redirect do Rodrigo to create new challenge page
    } else {
      //not Done || done but not rendering
    }
  },[actionState]);

  const onRandomChallengeButton = () => {
    setAction({
      function: challengeCtrl.getRandomChallenge,
      args: [],
      render: false
    })
  }

  const onCreateChallengeButton = () => {
    // redirect do Rodrigo to create new challenge page
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
          <Grid item xs={12} sm={10}>
            <Toolbar className={classes.toolbar} variant="regular" >
              <Button className={classes.randomChallengeButton}
                endIcon={<RepeatOne />}
                id="randomChallengeButton"
                variant="contained"
                onClick={onRandomChallengeButton}
              >
                Pick one
              </Button>
              {isAuthed  && 
                <Button className={classes.randomChallengeButton}
                  id="randomChallengeButton"
                  variant="contained"
                  onClick={onRandomChallengeButton}
                >
                  Create new challenge
                </Button>
              }
            </Toolbar>
            <ChallengeListTable />
          </Grid>
          <Grid item xs={12} sm={2} style={{backgroundColor:'#db9e07'}}>
            <p>Possible user information</p>
            <p>Pie-chart statistics of number of challenges compelted?</p>
          </Grid>
        </Grid>
      </Container>
    </div>
  )

}

export default ChallengeListPage