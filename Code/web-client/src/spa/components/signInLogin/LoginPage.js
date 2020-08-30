// react
import React, { useContext } from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Avatar from '@material-ui/core/Avatar'
import Button from '@material-ui/core/Button'
import Container from '@material-ui/core/Container'
import FormControl from '@material-ui/core/FormControl'
import Grid from '@material-ui/core/Grid'
import IconButton from '@material-ui/core/IconButton'
import InputAdornment from '@material-ui/core/InputAdornment'
import InputLabel from '@material-ui/core/InputLabel'
import Link from '@material-ui/core/Link'
import LockOutlinedIcon from '@material-ui/icons/LockOutlined'
import OutlinedInput from '@material-ui/core/OutlinedInput'
import Visibility from '@material-ui/icons/Visibility'
import VisibilityOff from '@material-ui/icons/VisibilityOff'
import TextField from '@material-ui/core/TextField'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'
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
    backgroundColor: "#be5041",
  },
  form: {
    width: '100%',
    marginTop: theme.spacing(3),
    '& label.Mui-focused': {
      color: '#be5041',
    },
    '& .MuiOutlinedInput-root': {
      '& fieldset': {
        borderColor: 'gray',
      },
      '&:hover fieldset': {
        borderColor: 'black',
      },
      '&.Mui-focused fieldset': {
        borderColor: '#e68d4c',
      },
    },
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
    backgroundColor: "#be5041",
    '&:hover': {
      backgroundColor: '#cf6744',
    },
  },
}));

export default function SignUp(props) {

  const { handleLogin } = useContext(AuthContext)

  const [state, setState] = React.useState({
    username: '',
    password: '',
  })

  const onChangeHandler = event => {
    const {name, value} = event.target
    setState({...state, [name]: value })
  }

  const onToggleShowPasswordHandler = () => {
    setState({ ...state, showPassword: !state.showPassword })
  }

  const onSubmitHandler = async function(event) {
    event.preventDefault()
    handleLogin({ credentials: btoa(`${state.username}:${state.password}`) })
  }

  const classes = useStyles()
  return (
    <Container component="main" maxWidth="xs">
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <form className={classes.form} noValidate>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                variant="outlined"
                required
                fullWidth
                id="loginUsername"
                label="Username"
                name="username"
                autoComplete="username"
                value={state.username}
                onChange={onChangeHandler}
              />
            </Grid>
            <Grid item xs={12}>
                <FormControl variant="outlined" style={{ width:'100%' }}>
                  <InputLabel htmlFor="outlined-adornment-password">Password *</InputLabel>
                  <OutlinedInput
                    id="loginPassword"
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
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            onClick={onSubmitHandler}
          >
            Login
          </Button>
          <Grid container justify="flex-end">
            <Grid item>
              <Link variant="body2" component={RouterLink} to="/signIn">
                What?! You don't have an account? Sign in
              </Link>
            </Grid>
          </Grid>
        </form>
      </div>
    </Container>
  );
}
