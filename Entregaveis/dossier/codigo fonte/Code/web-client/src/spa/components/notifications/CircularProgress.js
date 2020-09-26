// react
import React from 'react'
// material-ui components
import Backdrop from '@material-ui/core/Backdrop'
import CircularProgress from '@material-ui/core/CircularProgress'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles((theme) => ({
  layout: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
}));

export default function CircularProgressPage() {
  const classes = useStyles()
  return (
    <div className={classes.layout}>
      <Backdrop open={true}>
        <CircularProgress color="inherit" />
      </Backdrop>
    </div>
  );
}
