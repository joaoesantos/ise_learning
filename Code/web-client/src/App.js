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
import { ThemeProvider } from './spa/context/ThemeContext'
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
  const classes = useStyles();
  return (
    <ThemeProvider>
      <div className={classes.layout}>
        <CssBaseline />
        <AuthProvider>
          <Router history={history}>
            <Routes />
          </Router>
        </AuthProvider>
      </div>
    </ThemeProvider>
  )
}
