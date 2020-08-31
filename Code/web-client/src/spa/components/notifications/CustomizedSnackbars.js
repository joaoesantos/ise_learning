// react 
import React from 'react'
// material-ui components
import Snackbar from '@material-ui/core/Snackbar'
import { Alert } from '@material-ui/lab'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles((theme) => ({
  alert: {
    width: "100%",
    marginTop: theme.spacing(3.5),
  }
}))

export default function CustomizedSnackbars({ message, severity } ) {

  const classes = useStyles()
  const [open, setOpen] = React.useState(true)

  const handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return
    }
    setOpen(false)
  }

  return (
      <Snackbar 
        anchorOrigin={{ vertical:'top', horizontal:'center' }}
        autoHideDuration={3000} 
        open={open} 
        onClose={ handleClose }
      >
        <Alert 
          className={classes.alert}
          elevation={5} 
          variant="filled"
          severity={ severity }
          onClose={ handleClose } 
        >
          { message }
        </Alert>
      </Snackbar>
  )

}
