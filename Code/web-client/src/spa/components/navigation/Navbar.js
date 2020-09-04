// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import AppBar from '@material-ui/core/AppBar'
import Brightness4Icon from '@material-ui/icons/Brightness4'
import Brightness7Icon from '@material-ui/icons/Brightness7'
import IconButton from '@material-ui/core/IconButton'
import Link from '@material-ui/core/Link'
import Toolbar from '@material-ui/core/Toolbar'
import Tooltip from '@material-ui/core/Tooltip'
import Menu from '@material-ui/core/Menu'
import MenuItem from '@material-ui/core/MenuItem'
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew'
import { makeStyles } from '@material-ui/core/styles'
import Typography from '@material-ui/core/Typography'
// logo
import ISELearningLogo from '../../images/ISELearning_logo_wht.png'
// controllers
import { FetchHeaders } from '../../utils/fetchUtils'
// authentication context
import { themes, ThemeContext } from '../../context/ThemeContext'
import { AuthContext } from '../../context/AuthContext'
// utils
import history from '../../components/navigation/history'

const useStyles = makeStyles(theme => ({
  layout: {
      marginLeft: theme.spacing(0),
    },
    appBar: {
      position : 'sticky',
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

  const classes = useStyles()
  const { setTheme } = React.useContext(ThemeContext)
  const { isAuthed, setAuth, setUser } = React.useContext(AuthContext)
  const [checked, setChecked] = React.useState(true)
  const [anchorEl, setAnchorEl] = React.useState(null)
  const open = Boolean(anchorEl)

  const handleOnMenu = event => {
    setAnchorEl(event.currentTarget)
  }

  const handleOnClose = () => {
    setAnchorEl(null)
  }

  const toggleChecked = () => {
    setChecked((prev) => !prev)
    checked ? setTheme(themes.dark) : setTheme(themes.light)
  }

  const handleOnLogout = () => {
    setAnchorEl(null)
    setAuth(false)
    setUser(undefined)
    localStorage.removeItem('ISELearningLoggedUser')
    FetchHeaders.clear()
    history.push("/")
  }

  return (
    <div className={classes.layout}>
      <AppBar className={classes.appBar}>
        <Toolbar variant="dense">
            <Link className={classes.link} component={RouterLink} to="/">
              <img src={ISELearningLogo} height={40}/>
            </Link>
            <Typography className={classes.title}>
              <Link className={classes.link} component={RouterLink} to="/runCode">
                Run Code
              </Link>
              <Link className={classes.link} component={RouterLink} to="/listChallenges">
                Challenges
              </Link>
              {isAuthed &&
                <Link className={classes.link} component={RouterLink} to="/questionnaires">
                  Questionnaires
                </Link>
              }
            </Typography>
            <Tooltip title={checked ? "Switch to darkmode" : "Switch to lightmode"} >
              <IconButton
                aria-controls="theme-button"
                aria-haspopup="true"
                onClick={toggleChecked}
                color="inherit"
              >
                {checked ? <Brightness7Icon /> : <Brightness4Icon />}
              </IconButton>
            </Tooltip>
            {isAuthed ? 
            <>
              <Tooltip title="User options">
                <IconButton
                  aria-controls="menu-user"
                  aria-haspopup="true"
                  onClick={handleOnMenu}
                  color="inherit"
                >
                  <PowerSettingsNewIcon />
                </IconButton>
              </Tooltip>
              <Menu className={classes.menu}
                id="menu-user"
                getContentAnchorEl={null}
                anchorEl={anchorEl}
                anchorOrigin={{vertical:'bottom', horizontal:'right'}}
                keepMounted
                transformOrigin={{vertical:'top', horizontal:'center'}}
                open={open}
                onClose={handleOnClose}
              >
                <MenuItem component={RouterLink} to="/profile" onClick={handleOnClose}>Profile</MenuItem>
                <MenuItem onClick={handleOnLogout}>Log out</MenuItem>
              </Menu>
            </>
            :
            <>
              <Tooltip title="Login">
                <IconButton color="inherit" component={RouterLink} to="/login">
                  <PowerSettingsNewIcon />
                </IconButton>
              </Tooltip>
            </>
          }
        </Toolbar>
      </AppBar>
    </div>
  )
}

