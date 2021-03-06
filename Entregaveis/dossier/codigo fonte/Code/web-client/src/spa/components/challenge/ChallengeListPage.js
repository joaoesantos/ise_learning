// react
import React from 'react'
import { useHistory } from 'react-router-dom'
// material-ui components
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'
import Toolbar from '@material-ui/core/Toolbar'
import RepeatOne from '@material-ui/icons/RepeatOne'
// components
import ChallengeListTable from './ChallengeListTable'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { ChallengeController } from '../../controllers/challenge/ChallengeController'
// authentication context
import { AuthContext } from '../../context/AuthContext'

const useStyles = makeStyles((theme) => ({
  mainContainer: {
    maxWidth: '90%',
    padding: theme.spacing(2),
  },
  toolbar: {
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
},
}));

export default function ChallengeListPage() {

  const classes = useStyles()
  const { isAuthed } = React.useContext(AuthContext)
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  let history = useHistory()

  React.useEffect(() => {
    if (actionState === ActionStates.done && action.name && action.name === 'getRandomChallenge') {
      history.push(`/challenges/${response.json.challengeId}`)
    }
  },[actionState]);

  const onRandomChallengeButton = () => {
    setAction({
      function: ChallengeController.getRandomChallenge,
      args: [],
      name: 'getRandomChallenge'
    })
  }

  const onCreateChallengeButton = () => {
    history.push('/newChallenge')
  }


  return (
    <>
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
                color="primary"
                endIcon={<RepeatOne />}
                id="randomChallengeButton"
                variant="contained"
                onClick={onRandomChallengeButton}
              >
                Pick one
              </Button>
              {isAuthed  && 
                <Button className={classes.randomChallengeButton}
                  color="primary"
                  id="createChallengeButton"
                  variant="contained"
                  onClick={onCreateChallengeButton}
                >
                  Create new challenge
                </Button>
              }
            </Toolbar>
            <ChallengeListTable />
          </Grid>
          {/* <Grid item xs={12} sm={2}>
            <p>Possible user information</p>
            <p>Pie-chart statistics of number of challenges completed?</p>
          </Grid> */}
        </Grid>
      </Container>
    </>
  )

}