// react
import React from 'react';
// material-ui components
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
// codemirror
import RunCodeTextEditor from '../codemirror/RunCodeTextEditor'
import OutputTextEditor from '../codemirror/OutputTextEditor'

const useStyles = makeStyles((theme) => ({
  layout: {

  },
  container: {
    padding: theme.spacing(0),
  },
}));

export default function RunCode() {
  const classes = useStyles();
  return(
    <div className={classes.layout}>
        <Container className={classes.container} maxWidth={false}>
          <Grid container>
            <Grid item xs={7}>
              <RunCodeTextEditor />
            </Grid>
            <Grid item xs={5}>
              <OutputTextEditor />
            </Grid>
          </Grid>
      </Container>
    </div>
  )
};