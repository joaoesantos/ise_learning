// react
import React from 'react'
// material-ui components
import { Grid } from '@material-ui/core'
import Divider from '@material-ui/core/Divider'
import Link from '@material-ui/core/Link'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary">
            {'Copyright © '}
                <Link color="inherit" href="https://github.com/joaoesantos/ise_learning">
                    IS E-Learning
                </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    )
}

const useStyles = makeStyles((theme) => ({
    footer: {
        padding: theme.spacing(2),
        width: '100%',
        bottom: 0,
        //position: 'absolute',
    },
    link: {
        paddingLeft: 15,
        textDecoration: 'none'
      }
}));

export default function Footer() {
    const classes = useStyles()
    return (
        <footer className={classes.footer}>
            <Divider variant="middle" />
            <Grid 
                container
                direction="row"
                justify="space-around"
                alignItems="center"
                style={{ paddingTop:10 }}
            >
                <Copyright />
                <Typography variant="body2">
                    <Link className={classes.link} target="_blank" href="https://github.com/joaoesantos/ise_learning">Contact Us</Link>
                    <Link className={classes.link} target="_blank" href="https://github.com/joaoesantos/ise_learning/wiki">Wiki</Link>
                    <Link className={classes.link} target="_blank" href="https://github.com/joaoesantos/ise_learning/blob/master/LICENSE">Privacy Policy</Link>
                </Typography>
            </Grid>
        </footer>
    )
}