// react
import React from 'react';
import clsx from 'clsx';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import AccountCircle from '@material-ui/icons/AccountCircle';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import Container from '@material-ui/core/Container';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import InputLabel from '@material-ui/core/InputLabel';
import Link from '@material-ui/core/Link';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import TextField from '@material-ui/core/TextField';
import Typography from '@material-ui/core/Typography';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import { makeStyles } from '@material-ui/core/styles';
// pallete color
import {apiUrlTemplates} from '../clientSideConfig'

//controllers
import UseAction, { ActionStates } from '../controllers/UseAction'
import {UserController} from '../controllers/UserController'

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
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
  save25: {
    width: '25%',
    margin: theme.spacing(1, 0, 2),
    /*backgroundColor: palleteColor.color4,
    '&:hover': {
      backgroundColor: palleteColor.color3,
    },*/
  },
  save: {
    margin: theme.spacing(1, 0, 2),
    /*backgroundColor: palleteColor.color4,
    '&:hover': {
      backgroundColor: palleteColor.color3,
    },*/
  },
}));

export default function UserProfile(props) {
  const classes = useStyles()
  const [newPassword, setNewPassword] = React.useState("")
  const [showNewPassword, setShowNewPassword] = React.useState(false)
  const [repeatNewPassword, setRepeatNewPassword] = React.useState("")
  const [showRepeatNewPassword, setShowRepeatNewPassword] = React.useState(false)

  // fetch props & data
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [user, setUser] = React.useState()

  React.useEffect(() => {
    console.log("----------------------------------")
    console.log(response)
    console.log(actionState)
    console.log("||||||||||||||||||||||||||||||||||")
    if (response && actionState === ActionStates.done &&
      action.render && action.render === true) {
      setUser(response)
    } else if (!response) {
      setAction({
        function: UserController.getUserMe,
        args: [],
        render: true
      })
    } else {
        //TODO
    }
  },[actionState]);

  const handleSaveName = async (e) => {
    e.preventDefault()
  }

  const handleChangePassword = async (e) => {
    e.preventDefault()
    if(newPassword !== repeatNewPassword){
      alert("The passwords must match!")
    } else {

    }
  }

  const handleNameChange = (name) => {
    let userTemp = Object.assign({}, user)
    userTemp.name = name
    setUser(userTemp)
  }

  const handleEmailChange = (email) => {
    let userTemp = Object.assign({}, user)
    userTemp.email = email
    setUser(userTemp)
  }

  const handleNewPasswordChange = (password) => {
    setNewPassword(password)
  }

  const handleClickShowNewPassword = () => {
    setShowNewPassword(!showNewPassword)
  };

  const handleRepeatNewPasswordChange = (password) => {
    setRepeatNewPassword(password)
  }

  const handleClickShowRepeatNewPassword = () => {
    setShowRepeatNewPassword(!showRepeatNewPassword)
  };

  const handleMouseDownPassword = (event) => {
      event.preventDefault();
  };

  if (actionState === ActionStates.clear) {
    return <p>insert URL</p>
  } else if (actionState === ActionStates.fetching) {
    return <p>fetching...</p>
  } else if (actionState === ActionStates.done) {
    return (
    < Container component = "main" maxWidth = "md" >
      {user &&
        < div className = {classes.paper} >
          < Avatar className = {classes.avatar} >
            < AccountCircle />
          </ Avatar >
          < Typography component = "h1" variant = "h3" >
            User Profile
          </ Typography >
          < Typography component = "h1" variant = "h5" >
            User Data
          </ Typography >
          < Grid container spacing = {2} >
            < Grid item xs = {12} >
              < TextField
              variant = "outlined"
              required
              fullWidth
              disabled = {true}
              id = "username"
              label = "Username"
              name = "username"
              value = {user.username}
              />
            </ Grid >
            < Grid item xs = {12} >
              <FormControl className={clsx(classes.margin, classes.textField, classes.fullWidth)} variant="outlined">
                < Grid item xs = {12} >
                  {/*<InputLabel htmlFor="outlined-adornment-password">Name</InputLabel>
                  <OutlinedInput
                    id="name"
                    name = "name"
                    label = "Name"
                    required
                    fullWidth
                    type="text"
                    autoComplete = "name"
                    defaultValue={user.name}
                    value={user.name}
                    onChange={e => handleNameChange(e.target.value)}
                    labelWidth={110}
                  />*/}
                  < TextField
                  variant = "outlined"
                  required
                  fullWidth
                  id = "name"
                  label = "Name"
                  name = "name"
                  autoComplete = "name"
                  type = "text"
                  value = {user.name}
                  onChange={e => handleNameChange(e.target.value)}
                  />
                </ Grid >
                < Grid item xs = {12} >
                  < TextField
                  variant = "outlined"
                  required
                  fullWidth
                  id = "email"
                  label = "Email"
                  name = "email"
                  autoComplete = "email"
                  type = "email"
                  value = {user.email}
                  onChange={e => handleEmailChange(e.target.value)}
                  />
                </ Grid >
                < Grid item xs = {3} className = {classes.centerItems} >
                  < Input
                  type = "submit"
                  fullWidth
                  variant = "contained"
                  color = "primary"
                  className = {classes.save}
                  onClick = {e => handleSaveName(e)}
                  >
                    Save Name
                  </ Input >
                </ Grid >
              </FormControl>
            </ Grid >
          </ Grid >
          < Typography component = "h1" variant = "h5" >
            Credentials
          </ Typography >
          < Grid container spacing = {2} >
            < Grid item xs = {12} >
              <FormControl className={clsx(classes.margin, classes.textField, classes.fullWidth)} variant="outlined">
                <InputLabel htmlFor="outlined-adornment-password">New Password</InputLabel>
                <OutlinedInput
                  id="newPassword1"
                  name = "newPassword1"
                  type={showNewPassword ? 'text' : 'password'}
                  value={newPassword}
                  onChange={e => handleNewPasswordChange(e.target.value)}
                  endAdornment={
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
                  }
                  labelWidth={110}
                />
              </FormControl>
            </ Grid >
            < Grid item xs = {12} >
              <FormControl className={clsx(classes.margin, classes.textField, classes.fullWidth)} variant="outlined">
                <InputLabel htmlFor="outlined-adornment-password">Repeat New Password</InputLabel>
                <OutlinedInput
                  id="newPassword2"
                  name = "newPassword2"
                  type={showRepeatNewPassword ? 'text' : 'password'}
                  value={repeatNewPassword}
                  onChange={e => handleRepeatNewPasswordChange(e.target.value)}
                  endAdornment={
                      <InputAdornment position="end">
                        <IconButton
                            aria-label="toggle password visibility"
                            onClick={handleClickShowRepeatNewPassword}
                            onMouseDown={handleMouseDownPassword}
                            edge="end"
                        >
                          {showRepeatNewPassword ? <Visibility /> : <VisibilityOff/>}
                        </IconButton>
                      </InputAdornment>
                  }
                  labelWidth={165}
                />
              </FormControl>
            </ Grid >
            < Grid item xs = {12} className = {classes.centerItems} >
              < Button
              type = "submit"
              fullWidth variant = "contained"
              color = "primary"
              className = {classes.save25}
              onClick = {e => handleChangePassword(e)}
              >
                Change Password
              </ Button >
            </ Grid >
          </ Grid >
          <Link className={classes.link} component={RouterLink} to={`/users/${user.id}/projects`}>
            My projects
          </Link>
        </ div >
      }
    </ Container >
    );
  } else {
      return <p>error...</p>
  }
}