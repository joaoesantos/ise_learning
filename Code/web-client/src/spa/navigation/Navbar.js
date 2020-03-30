// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// icons
import NotificationsIcon from '@material-ui/icons/Notifications';
import Badge from '@material-ui/core/Badge';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
// material-ui components
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
// login-logout
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormGroup from '@material-ui/core/FormGroup';
// styles
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
  layout: {
      //flexGrow: 1,
      marginLeft: theme.spacing(0),
    },
    appBar: {
      position : 'static',
      background : '#202020',
      borderBottom: `1px solid ${theme.palette.divider}`,
    },
    menuButton: {
      marginRight: theme.spacing(3),
    },
    menu: {
      '&:selected': {
        background: '#202020',
      },
    },
    link: {
      paddingLeft: 15,
      color: '#777777',
      textDecoration: 'none',
      '&:hover, &:focus' : {
        color: '#ffffff',
        textDecoration: 'none',
      }
    },
    title: {
      flexGrow: 1, 
    },
}));
  
export default function Navbar() {
  const classes = useStyles();
  const [auth, setAuth] = React.useState(true);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);

  const handleChange = event => {
    setAuth(event.target.checked);
  };

  const handleMenu = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <div className={classes.layout}>
      {/* <FormGroup>
        <FormControlLabel
          control={<Switch checked={auth} onChange={handleChange} aria-label="login switch" />}
          label={auth ? 'Logout' : 'Login'}
        />
      </FormGroup> */}
      <AppBar className={classes.appBar}>
        <Toolbar variant="dense">
          <Typography className={classes.title}>
            <Link className={classes.link} component={RouterLink} to="/">
              ISE-Learning
            </Link>
            <Link className={classes.link} component={RouterLink} to="/challenges">
              Challenges
            </Link>
            <Link className={classes.link} component={RouterLink} to="/questionnaires">
              Questionnaires
            </Link>
            <Link className={classes.link} component={RouterLink} to="/runCode">
              Run Code
            </Link>
          </Typography>
          {auth && (
            <div>
              <Tooltip title="xora no meu pau">
                <IconButton color="inherit">
                  <Badge badgeContent={4} color="secondary">
                    <NotificationsIcon />
                  </Badge>
                </IconButton>
              </Tooltip>
              <IconButton
                aria-controls="menu-user"
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <PowerSettingsNewIcon />
              </IconButton>
              <Menu className={classes.menu}
                id="menu-user"
                getContentAnchorEl={null}
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'center',
                }}
                open={open}
                onClose={handleClose}
              >
                <MenuItem component={RouterLink} to="/profile" onClick={handleClose}>Profile   </MenuItem>
                <MenuItem component={RouterLink} to="/account" onClick={handleClose}>My account</MenuItem>
                <MenuItem component={RouterLink} to="/logout"  onClick={handleClose}>Log out   </MenuItem>
              </Menu>
            </div>
          )}
        </Toolbar>
      </AppBar>
    </div>
  );
}


