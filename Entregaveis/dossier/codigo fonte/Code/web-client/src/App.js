// react
import React from 'react'
import { HashRouter as Router } from 'react-router-dom'
// material-ui components
import { makeStyles } from '@material-ui/core/styles'
// components
import Routes from './spa/components/navigation/Routes'
// context
import { AuthProvider } from './spa/context/AuthContext'
import { ThemeProvider } from './spa/context/ThemeContext'
// css normalization
import CssBaseline from '@material-ui/core/CssBaseline'
import 'react-reflex/styles.css'
import './spa/css/iselearning.css'
import './spa/css/wickedcss.min.css'

const useStyles = makeStyles((theme) => ({
  layout: {
    minWidth: 570,
    height: "100%",
    position: "relative"
  },
}));

export default function App() {
  const classes = useStyles();
  return (
    <ThemeProvider>
      <div className={classes.layout}>
        <CssBaseline />
        <AuthProvider>
          <Router>
            <Routes />
          </Router>
        </AuthProvider>
      </div>
    </ThemeProvider>
  )
}
