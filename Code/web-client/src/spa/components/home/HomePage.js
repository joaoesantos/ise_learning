// react
import React from 'react';
// material-ui components
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
// IS E-Learning images
import mainImage from '../../images/ISELearning.png';
// cards components
import RunEnvironmentsCard from './RunEnvironmentsCard.js'

const useStyles = makeStyles((theme) => ({
  layout: {
    flexGrow: 1,
    backgroundColor: '#963727'
  },
  container: {
    padding: theme.spacing(2),
  },
  mainPaper : {
    fontFamily: 'Lucida Console, Courier, monospace',
    fontSize: '2.5em',
    textAlign: 'center'
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
}));

export default function HomePage() {
  const classes = useStyles();

  return (
    <div className={classes.layout}>
        <Container className={classes.container}>
            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Paper className={classes.paper}>
                        <img src={mainImage}/>
                    </Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper className={classes.mainPaper}>
                        <p>Yes, that's right. The sexyest and the nicest, </p>
                        <p>E-Learning platform ever created!</p>
                    </Paper>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <RunEnvironmentsCard />
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Paper className={classes.paper}>xs=12 sm=6</Paper>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Paper className={classes.paper}>xs=12 sm=6</Paper>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Paper className={classes.paper}>xs=12 sm=6</Paper>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Paper className={classes.paper}>xs=12 sm=6</Paper>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Paper className={classes.paper}>xs=12 sm=6</Paper>
                </Grid>
            </Grid>
      </Container>
    </div>
  );
}
