// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import AppBar from '@material-ui/core/AppBar'
import Badge from '@material-ui/core/Badge'
import IconButton from '@material-ui/core/IconButton'
import Link from '@material-ui/core/Link'
import Toolbar from '@material-ui/core/Toolbar'
import Tooltip from '@material-ui/core/Tooltip'
import { makeStyles } from '@material-ui/core/styles'
import Menu from '@material-ui/core/Menu'
import MenuItem from '@material-ui/core/MenuItem'
import NotificationsIcon from '@material-ui/icons/Notifications'
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew'
import Typography from '@material-ui/core/Typography'
// logo
import logo from '../../images/ISELearning_logo_wht.png'

const useStyles = makeStyles(theme => ({
  layout: {
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
  
export default function Navbar(props) {

  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);

  const handleMenu = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    setAnchorEl(null);
    props.setAuth(false);
  };

  const classes = useStyles();
  return (
    <div className={classes.layout}>
      <AppBar className={classes.appBar}>
        <Toolbar variant="dense">
            <Link className={classes.link} component={RouterLink} to="/">
              <img src={logo} height={40}/>
            </Link>
          {props.isAuthed ? 
            <Typography className={classes.title}>
              <Link className={classes.link} component={RouterLink} to="/listChallenges">
                Challenges
              </Link>
              <Link className={classes.link} component={RouterLink} to="/questionnaires">
                Questionnaires
              </Link>
              <Link className={classes.link} component={RouterLink} to="/runCode">
                Run Code
              </Link>
            </Typography>
            :
            <Typography className={classes.title}>
              <Link className={classes.link} component={RouterLink} to="/runCode">
                Run Code
              </Link>
            </Typography>}
            {props.isAuthed ? 
              <div>
                <IconButton color="inherit">
                  <Badge badgeContent={4} color="secondary">
                    <NotificationsIcon />
                  </Badge>
                </IconButton>
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
                anchorOrigin={{vertical:'bottom', horizontal:'right'}}
                keepMounted
                transformOrigin={{vertical:'top', horizontal:'center'}}
                open={open}
                onClose={handleClose}
              >
                <MenuItem component={RouterLink} to="/profile" onClick={handleClose}>Profile</MenuItem>
                <MenuItem component={RouterLink} to="/" onClick={handleLogout}>Log out</MenuItem>
              </Menu>
            </div>
            :
            <div>
              <Tooltip title="Login">
                <IconButton color="inherit" component={RouterLink} to="/login">
                  <PowerSettingsNewIcon />
                </IconButton>
              </Tooltip>
            </div>
          }
        </Toolbar>
      </AppBar>
    </div>
  );
}


