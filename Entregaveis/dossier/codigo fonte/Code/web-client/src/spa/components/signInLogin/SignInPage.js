// react
import React from 'react'
import { useHistory } from 'react-router-dom'
// material-ui components
import Avatar from '@material-ui/core/Avatar'
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import FormControl from '@material-ui/core/FormControl'
import Grid from '@material-ui/core/Grid'
import IconButton from '@material-ui/core/IconButton'
import InputAdornment from '@material-ui/core/InputAdornment'
import InputLabel from '@material-ui/core/InputLabel'
import LockOutlinedIcon from '@material-ui/icons/LockOutlined'
import OutlinedInput from '@material-ui/core/OutlinedInput'
import { makeStyles } from '@material-ui/core/styles'
import TextField from '@material-ui/core/TextField'
import Typography from '@material-ui/core/Typography'
import Visibility from '@material-ui/icons/Visibility'
import VisibilityOff from '@material-ui/icons/VisibilityOff'
// notifications
import CustomizedSnackbars from '../notifications/CustomizedSnackbars'
// controllers
import { UserController } from '../../controllers/UserController'
import UseAction, { ActionStates } from '../../controllers/UseAction'
// authentication context
import { AuthContext } from '../../context/AuthContext'

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: "#757575",
  },
  form: {
    width: '100%',
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function SignUp() {

  const classes = useStyles()
  const { setAuth, setUser } = React.useContext(AuthContext)
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [state, setState] = React.useState({
    name: '',
    username: '',
    email: '',
    password: '',
  })

  let history = useHistory()

  React.useEffect(() => {
    if (actionState === ActionStates.done) {
      if(response.severity === 'success') {
        setAuth(true)
        setUser(response.json)
        localStorage.setItem('ISELearningLoggedUser', JSON.stringify(response.json))
        history.push("/")
      }
    }
  },[actionState]);

  const onChangeHandler = event => {
    const {name, value} = event.target
    setState({...state, [name]: value })
  }

  const onToggleShowPasswordHandler = () => {
    setState({ ...state, showPassword: !state.showPassword })
  }

  const onSubmitHandler = async (event) => {
    event.preventDefault()
    let userModel = { name: state.name, username: state.username, email: state.email, password: state.password }
    setAction({
        function: UserController.createMe,
        args: [userModel],
        credentials: btoa(`${state.username}:${state.password}`)
      })
  }

  return (
    <>
      {actionState === ActionStates.done && <CustomizedSnackbars message={response.message} severity={response.severity} />}
      <Container component="main" maxWidth="xs">
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <form className={classes.form} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  id="SignInName"
                  label="Name"
                  name="name"
                  autoComplete="name"
                  autoFocus
                  value={state.name}
                  onChange={onChangeHandler}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  id="SignInUsername"
                  label="Username"
                  name="username"
                  value={state.username}
                  onChange={onChangeHandler}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  variant="outlined"
                  required
                  fullWidth
                  id="signInEmail"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  value={state.email}
                  onChange={onChangeHandler}
                />
              </Grid>
              <Grid item xs={12}>
                  <FormControl variant="outlined" style={{ width:'100%' }}>
                    <InputLabel htmlFor="outlined-adornment-password">Password *</InputLabel>
                    <OutlinedInput
                      id="signInPassword"
                      name="password"
                      label="Password"
                      type={state.showPassword ? 'text' : 'password'}
                      onChange={onChangeHandler}
                      endAdornment={
                        <InputAdornment position="end">
                          <IconButton
                            aria-label="toggle password visibility"
                            onClick={onToggleShowPasswordHandler}
                            edge="end"
                          >
                            {state.showPassword ? <Visibility /> : <VisibilityOff />}
                          </IconButton>
                        </InputAdornment>
                      }
                      labelWidth={70}
                    />
                  </FormControl>
                </Grid>
            </Grid>
            <Button className={classes.submit}
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              onClick={onSubmitHandler}
            >
              Sign Up
            </Button>
          </form>
        </div>
      </Container>
    </>
  )
}
