// react
import React from 'react'
import { useHistory } from 'react-router-dom'
// formik components
import { Formik, Form, Field } from 'formik'
import * as Yup from 'yup'
// material-ui
import AccountCircle from '@material-ui/icons/AccountCircle'
import Avatar from '@material-ui/core/Avatar'
import { Button, LinearProgress } from '@material-ui/core'
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import IconButton from '@material-ui/core/IconButton'
import InputAdornment from '@material-ui/core/InputAdornment'
import { makeStyles } from '@material-ui/core/styles'
import { TextField } from 'formik-material-ui'
import Typography from '@material-ui/core/Typography'
import Visibility from '@material-ui/icons/Visibility'
import VisibilityOff from '@material-ui/icons/VisibilityOff'
// notifications
import CustomizedSnackbars from '../components/notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../components/notifications/DefaultErrorMessage'
//controllers
import UseAction, { ActionStates } from '../controllers/UseAction'
import { UserController } from '../controllers/UserController'
// authentication context
import { AuthContext } from '../context/AuthContext'
// utils
import { fetchHeaders } from '../utils/fetchUtils'

const useStyles = makeStyles((theme) => ({
  avatar: {
    margin: theme.spacing(1),
  },
  centerItems: {
    textAlign: 'center'
  },
  fullWidth: {
    width: '100%'
  },
  paper: {
    maxWidth: '35%',
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  deleteAccount: {
    color: "#ffffff",
    backgroundColor:'#9b1003',
    '&:hover' : {
      backgroundColor: '#e3242b'
    }
  }
}))

export default function UserProfile() {

  const classes = useStyles()
  const { setAuth, user, setUser } = React.useContext(AuthContext)
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [showNewPassword, setShowNewPassword] = React.useState(false)
  const [showRepeatNewPassword, setShowRepeatNewPassword] = React.useState(false)
  const [deleteAccount, setDeleteAccount] = React.useState(false)

  let history = useHistory()

  React.useEffect(() => {
    if (response && actionState === ActionStates.done && action.name && action.name === 'updateMe') {
      setUser(response.json)
    } else if(response && actionState === ActionStates.done && action.name && action.name === 'deleteMe') {
      setAuth(false)
      setUser(undefined)
      localStorage.removeItem('ISELearningLoggedUser')
      fetchHeaders.clear()
      history.push("/")
    }
  },[actionState]);

  const handleClickShowNewPassword = () => {
    setShowNewPassword(!showNewPassword)
  }

  const handleClickShowRepeatNewPassword = () => {
    setShowRepeatNewPassword(!showRepeatNewPassword)
  }

  const handleMouseDownPassword = (event) => {
      event.preventDefault();
  }

  const handleDeleteAccount = () => {
    if(deleteAccount) {
      setAction({
        function: UserController.deleteMe,
        args: [],
        render: false,
        name: "deleteMe"
      })
    }
    setDeleteAccount(!deleteAccount)
}

  if(user) {
    return (
      <>
        {actionState === ActionStates.done && response.render && <CustomizedSnackbars message={response.message} severity={response.severity} />}
        <Container component = "main" maxWidth = "md" className = {classes.paper} >
          <Avatar className = {classes.avatar} >
            <AccountCircle />
          </Avatar >
          <Typography component = "h1" variant = "h3" >
            User Profile
          </Typography >
          <Typography component = "h1" variant = "h5" >
            User Data
          </Typography >
          {/*User Data Form*/}
          <Formik
            initialValues={{
              username: user.username,
              name: user.name,
              email: user.email,
            }}
            validationSchema={Yup.object({
              username: Yup.string()
                .required('Required'),
              name: Yup.string()
                .required('Required'),
              email: Yup.string()
                .email('Invalid email address')
                .required('Required')
            })}
            onSubmit={(data, { setSubmitting }) => {
              setSubmitting(false);
              setAction({
                function: UserController.updateMe,
                args: [{ name:data.name, email:data.email }],
                name: 'updateMe'
              })
            }}
          >
            {({ submitForm, isSubmitting }) => (
              <Grid container spacing={2}>
                <Form className={classes.fullWidth}>
                <Grid item xs={12} >
                    <Field
                      className={classes.fullWidth}
                      component={TextField}
                      type="username"
                      label="Username"
                      name="username"
                      disabled={true}
                    />
                  </Grid>
                  <br />
                  <Grid item xs={12}>
                    <Field
                      className={classes.fullWidth}
                      component={TextField}
                      type="name"
                      label="Name"
                      name="name"
                    />
                  </Grid>
                  <br />
                  <Grid item xs={12}>
                    <Field
                      className={classes.fullWidth}
                      component={TextField}
                      name="email"
                      type="email"
                      label="email"
                    />
                  </Grid>
                  {isSubmitting && <LinearProgress />}
                  <br />
                  <Grid item xs={12} className={classes.centerItems}>
                    <Button
                      variant="contained"
                      color="primary"
                      disabled={isSubmitting}
                      onClick={submitForm}
                    >
                      Save
                    </Button>
                  </Grid>
                </Form>
              </Grid>
            )}
          </Formik>
          <br />
          <br />
          <Typography component = "h1" variant = "h5" >
            Change password
          </Typography >
          {/*Credentials Form*/}
        <Formik
          initialValues={{
            newPassword: '',
            repeatNewPassword: ''
          }}
          validationSchema={Yup.object({
            newPassword: Yup.string()
              .min(8, 'Must be at least 8 characters')
              .required('Required')
              .test({
                name: "match-new-password-to-repeat-new-password",
                test: function(pw) {
                  return pw === this.parent.repeatNewPassword
                },
                message: "The passwords don't match.",
                params: {},
                exclusive: false
              }),
            repeatNewPassword: Yup.string()
              .min(6, 'Must be at least 6 characters')
              .required('Required')
          })}
          onSubmit={(data, { setSubmitting }) => {
            setSubmitting(false);
            setAction({
              function: UserController.changeMyCredentials,
              args: [{ password: data.newPassword }],
              name: 'changeMyCredentials'
            })
          }}
        >
          {({ submitForm, isSubmitting }) => (
            <Grid container spacing={2}>
              <Form className={classes.fullWidth}>
                <Grid item xs={12}>
                  <Field
                    className={classes.fullWidth}
                    component={TextField}
                    name="newPassword"
                    type={showNewPassword ? 'text' : 'password'}
                    label="New Password"
                    InputProps={{
                      endAdornment:(
                        <InputAdornment position="end">
                          <IconButton
                              aria-label="toggle password visibility"
                              onClick={handleClickShowNewPassword}
                              onMouseDown={handleMouseDownPassword}
                              edge="end"
                          >
                            {showNewPassword ? <Visibility /> : <VisibilityOff/>}
                          </IconButton>
                        </InputAdornment>
                      )
                    }}
                  />
                </Grid>
                <br />
                <Grid item xs={12}>
                  <Field
                    className={classes.fullWidth}
                    component={TextField}
                    type={showRepeatNewPassword ? 'text' : 'password'}
                    label="New Password"
                    name="repeatNewPassword"
                    InputProps={{
                      endAdornment:(
                        <InputAdornment position="end">
                          <IconButton
                              aria-label="toggle password visibility"
                              onClick={handleClickShowRepeatNewPassword}
                              onMouseDown={handleMouseDownPassword}
                              edge="end"
                          >
                            {showNewPassword ? <Visibility /> : <VisibilityOff />}
                          </IconButton>
                        </InputAdornment>
                      )
                    }}
                  />
                </Grid>
                {isSubmitting && <LinearProgress />}
                <br />
                <Grid item xs={12} className={classes.centerItems}>
                  <Button
                    variant="contained"
                    color="primary"
                    disabled={isSubmitting}
                    onClick={submitForm}
                  >
                    Change password
                  </Button>
                </Grid>
              </Form>
            </Grid>
          )}
        </Formik>
        <br />
        <br />
        <br />
        <Grid item xs={12} className={classes.centerItems}>
          {!deleteAccount ?
            <Button className={classes.deleteAccount}
              variant="contained"
              onClick={handleDeleteAccount}
            >
              Delete account
            </Button>
            :
            <Grid>
              <Typography component = "h1" variant = "h6" style={{color:"#9b1003"}}>
                This action is irreversible, are you sure?
              </Typography >
              <Grid           
                container
                direction="row"
                justify="center"
                alignItems="center"
              >
                <Button onClick={handleDeleteAccount}>Yes</Button>
                <Button onClick={() => setDeleteAccount(false)}>No</Button>
              </Grid>
            </Grid>
        }
        </Grid>
        <br />
        <br />
      </Container>
    </>
    )
  } else {
    return <DefaultErrorMessage message={"404 | Not Found"} />
  }
}