// react
import React from 'react'
// material-ui components
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'
import Toolbar from '@material-ui/core/Toolbar'
// components
import QuestionnaireListTable from './QuestionnaireListTable'
// authentication context
import { AuthContext } from '../../context/AuthContext'
// utils
import history from '../navigation/history'

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

export default function QuestionnaireListPage() {

  const classes = useStyles()
  const { isAuthed } = React.useContext(AuthContext)

  const onCreateQuestionnaireButton = () => {
    history.push(`/createEditQuestionnaire/${undefined}`)
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
          <Grid item xs={12} sm={12}>
            <Toolbar className={classes.toolbar} variant="regular" >
              {isAuthed  && 
                <Button className={classes.randomChallengeButton}
                  color="primary"
                  id="createQuestionnaireButton"
                  variant="contained"
                  onClick={onCreateQuestionnaireButton}
                >
                  Create new questionnaire
                </Button>
              }
            </Toolbar>
            <QuestionnaireListTable />
          </Grid>
        </Grid>
      </Container>
    </>
  )

}