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
import {palleteColor, apiUrlTemplates} from '../clientSideConfig'

//controllers
import UseFetch, { FetchStates } from '../controllers/UseFetch'
import {addSirenActionFetchOptions} from '../controllers/sirenParser'
// custom components
import RedirectToAuthentication from './RedirectToAuthentication'

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: palleteColor.color4,
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
    backgroundColor: palleteColor.color4,
    '&:hover': {
      backgroundColor: palleteColor.color3,
    },
  },
  save: {
    margin: theme.spacing(1, 0, 2),
    backgroundColor: palleteColor.color4,
    '&:hover': {
      backgroundColor: palleteColor.color3,
    },
  },
}));

export default function UserProfile(props) {
  const classes = useStyles()
  const [newPassword, setNewPassword] = React.useState("")
  const [showNewPassword, setShowNewPassword] = React.useState(false)
  const [repeatNewPassword, setRepeatNewPassword] = React.useState("")
  const [showRepeatNewPassword, setShowRepeatNewPassword] = React.useState(false)

  // fetch props & data
  const [url, setUrl] = React.useState()
  const [options, setOptions] = React.useState({ credentials: props.credentials })
  const [fetchState, response, json] = UseFetch(url,options)
  const [user, setUser] = React.useState(props.loggedUser.properties)

  React.useEffect(() => {
    if (json) {
      setUser(json.properties)
      props.setLoggedUser(json)
    } else {
      setUrl(apiUrlTemplates.userOperationsById(props.match.params.id))
    }
  },[json]);

  const handleSaveName = async () => {
    let newOptions = addSirenActionFetchOptions(json, 'edit-user-name', options)
    newOptions.body.name = user.name
    setOptions(newOptions)
  }

  const handleChangePassword = async () => {
    if(newPassword !== repeatNewPassword){
      alert("The passwords must match!")
    } else {
      let newOptions = addSirenActionFetchOptions(json, 'edit-user-password', options)
      newOptions.body.password = newPassword
      setOptions(newOptions)
      setNewPassword("")
      setRepeatNewPassword("")
      props.setCredentials(btoa(`${user.username}:${user.password}`))
    }
  }

  const handleNameChange = (name) => {
    let userTemp = Object.assign({}, user)
    userTemp.name = name
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

  if (fetchState === FetchStates.clear) {
    return <p>insert URL</p>
  } else if (fetchState === FetchStates.fetching) {
    return <p>fetching...</p>
  } else if (fetchState === FetchStates.done) {
    return (
    < Container component = "main" maxWidth = "md" >
      <RedirectToAuthentication credentials={props.credentials} loggedUser={props.loggedUser} url={props.match.url}/>
      {props.loggedUser && props.credentials &&
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
            < Grid item xs = {10} >
              < TextField
              variant = "outlined"
              required
              fullWidth
              id = "name"
              label = "Name"
              name = "name"
              autoComplete = "name"
              value = {user.name}
              onChange={e => handleNameChange(e.target.value)}
              />
            </ Grid >
            < Grid item xs = {2} className = {classes.centerItems} >
              < Button
              type = "submit"
              fullWidth
              variant = "contained"
              color = "primary"
              className = {classes.save}
              onClick = {() => handleSaveName()}
              >
                Save Name
              </ Button >
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
              onClick = {() => handleChangePassword()}
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