// react
import * as React from 'react'
import { Formik, Form, Field } from 'formik'
import * as Yup from 'yup'
// material-ui/Formik components
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
//controllers
import UseAction, { ActionStates } from '../controllers/UseAction'
import { UserController } from '../controllers/UserController'

const useStyles = makeStyles((theme) => ({
  avatar: {
    margin: theme.spacing(1),
    //backgroundColor: palleteColor.color4,
  },
  centerItems: {
    textAlign: 'center'
  },
  fullWidth: {
    width: '100%'
  },
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  }
}))

export default function App() {

  const classes = useStyles()
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [user, setUser] = React.useState()
  const [showNewPassword, setShowNewPassword] = React.useState(false)
  const [showRepeatNewPassword, setShowRepeatNewPassword] = React.useState(false)

  React.useEffect(() => {
    if (response && actionState === ActionStates.done &&
      action.render && action.render === true) {
      setUser(response)
    } else if (!response) {
      setAction({
        function: UserController.getUserMe,
        args: [],
        render: true
      })
    } else if (actionState === ActionStates.done &&
      action.render && action.render === true) {
      setUser(response)
    } else {
      //not Done || done but not rendering
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

  if (actionState === ActionStates.clear) {
    return <p>insert URL</p>
  } else if (actionState === ActionStates.inProgress) {
    return <p>fetching...</p>
  } else if (actionState === ActionStates.done && user) {
    return (
      < Container component = "main" maxWidth = "md" className = {classes.paper} >
        < Avatar className = {classes.avatar} >
          < AccountCircle />
        </ Avatar >
        < Typography component = "h1" variant = "h3" >
          User Profile
        </ Typography >
        < Typography component = "h1" variant = "h5" >
          User Data
        </ Typography >
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
          onSubmit={(values, { setSubmitting }) => {
            setSubmitting(false);
            setAction({
              function: UserController.updateUserData,
              args: [values],
              render: true
            })
          }}
        >
          {({ submitForm, isSubmitting }) => (
            < Grid container spacing = {2} >
              <Form className={classes.fullWidth}>
              < Grid item xs = {12} >
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
                < Grid item xs = {12} >
                  <Field
                    className={classes.fullWidth}
                    component={TextField}
                    type="name"
                    label="Name"
                    name="name"
                  />
                </Grid>
                <br />
                < Grid item xs = {12} >
                  <Field
                    className={classes.fullWidth}
                    component={TextField}
                    name="email"
                    type="email"
                    label="Email"
                  />
                </Grid>
                {isSubmitting && <LinearProgress />}
                <br />
                < Grid item xs = {12} className = {classes.centerItems}>
                  <Button
                    variant="contained"
                    color="primary"
                    disabled={isSubmitting}
                    onClick={submitForm}
                  >
                    Save User Data
                  </Button>
                </Grid>
              </Form>
            </Grid>
          )}
        </Formik>
        <br />
        <br />
        < Typography component = "h1" variant = "h5" >
          Credentials
        </ Typography >
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
            .min(8, 'Must be at least 8 characters')
            .required('Required')
        })}
        onSubmit={(values, { setSubmitting }) => {
          setSubmitting(false);
          setAction({
            function: UserController.changeCredentials,
            args: [values],
            render: true
          })
        }}
      >
        {({ submitForm, isSubmitting }) => (
          < Grid container spacing = {2} >
            <Form className={classes.fullWidth} >
              < Grid item xs = {12} >
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
              < Grid item xs = {12} >
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
                          {showNewPassword ? <Visibility /> : <VisibilityOff/>}
                        </IconButton>
                      </InputAdornment>
                    )
                  }}
                />
              </Grid>
              {isSubmitting && <LinearProgress />}
              <br />
              < Grid item xs = {12} className = {classes.centerItems}>
                <Button
                  variant="contained"
                  color="primary"
                  disabled={isSubmitting}
                  onClick={submitForm}
                >
                  Change Password
                </Button>
              </Grid>
            </Form>
          </Grid>
        )}
      </Formik>
    </Container>
    )
  } else {
    return <p>error...</p>
  }
}