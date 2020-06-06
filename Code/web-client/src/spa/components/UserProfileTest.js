// react
import * as React from 'react';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';

// material-ui/Formik components
import { Button, LinearProgress } from '@material-ui/core';
import Container from '@material-ui/core/Container';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import { TextField } from 'formik-material-ui';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';

//controllers
import UseAction, { ActionStates } from '../controllers/UseAction'
import {UserController} from '../controllers/UserController'

export default function App() {
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)
  const [user, setUser] = React.useState()

  const [showNewPassword, setShowNewPassword] = React.useState(false)
  const [showRepeatNewPassword, setShowRepeatNewPassword] = React.useState(false)

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

  const handleClickShowNewPassword = () => {
    setShowNewPassword(!showNewPassword)
  };

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
        {/*User Data Form*/}
        <Formik
          initialValues={{
            email: user ? user.email : '',
            password: '',
          }}
          validationSchema={Yup.object({
            password: Yup.string()
              .max(15, 'Must be 15 characters or less')
              .required('Required'),
            email: Yup.string()
              .email('Invalid email address')
              .required('Required')
          })}
          onSubmit={(values, { setSubmitting }) => {
            setTimeout(() => {
              setSubmitting(false);
              alert(JSON.stringify(values, null, 2));
            }, 500);
          }}
        >
          {({ submitForm, isSubmitting }) => (
            <Form>
              <Field
                component={TextField}
                name="email"
                type="email"
                label="Email"
              />
              <br />
              <Field
                component={TextField}
                type="password"
                label="Password"
                name="password"
              />
              {isSubmitting && <LinearProgress />}
              <br />
              <Button
                variant="contained"
                color="primary"
                disabled={isSubmitting}
                onClick={submitForm}
              >
                Submit
              </Button>
            </Form>
          )}
        </Formik>
        {/*Credentials Form*/}
        <Formik
        initialValues={{
          newPassword: '',
          repeatNewPassword: '',
        }}
        validationSchema={Yup.object({
          newPassword: Yup.string()
            .min(15, 'Must be at least 8 characters')
            .required('Required'),
          repeatNewPassword: Yup.string()
            .min(15, 'Must be at least 8 characters')
            .required('Required')
        })}
        onSubmit={(values, { setSubmitting }) => {
          setTimeout(() => {
            setSubmitting(false);
            alert(JSON.stringify(values, null, 2));
          }, 500);
        }}
      >
        {({ submitForm, isSubmitting }) => (
          <Form>
            <Field
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
            <br />
            <Field
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
            {isSubmitting && <LinearProgress />}
            <br />
            <Button
              variant="contained"
              color="primary"
              disabled={isSubmitting}
              onClick={submitForm}
            >
              Submit
            </Button>
          </Form>
        )}
      </Formik>
    </Container>
    );
  } else {
    return <p>error...</p>
  }
}