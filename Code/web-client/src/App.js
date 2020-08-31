// react
import React from 'react'
import { Router } from 'react-router-dom'
import history from './spa/components/navigation/history'
// material-ui components
import { makeStyles } from '@material-ui/core/styles'
// components
import Routes from './spa/components/navigation/Routes'
// context
import { AuthProvider } from './spa/context/AuthContext'
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline'

const useStyles = makeStyles((theme) => ({
  layout: {
    minWidth: 570,
    minHeight: '100vh',
    position: 'relative'
  },
}));

export default function App() {
<<<<<<< HEAD
=======

>>>>>>> 634c76143c0a208c95a791282811e90d51d19867
  const classes = useStyles();
  return (
    <div className={classes.layout}>
      <CssBaseline />
      <AuthProvider>
        <Router history={history}>
          <Routes />
        </Router>
      </AuthProvider>
    </div>
  )
}
