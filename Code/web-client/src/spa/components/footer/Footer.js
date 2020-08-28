// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import Divider from '@material-ui/core/Divider';
import Link from '@material-ui/core/Link';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import { Grid } from '@material-ui/core';

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary">
        {'Copyright © '}
            <Link color="inherit" href="https://material-ui.com/">
                IS E-Learning
            </Link>{' '}
        {new Date().getFullYear()}
        {'.'}
        </Typography>
    );
}

const useStyles = makeStyles((theme) => ({
    footer: {
        padding: theme.spacing(2),
        marginTop: 'auto',
        alignItems: 'center'
    },
    link: {
        paddingLeft: 15,
        textDecoration: 'none',
        color: '#f0bc5e'
      }
}));

export default function Footer() {
    const classes = useStyles();
    return (
        <footer className={classes.footer}>
            <Divider variant="middle" />
            <Grid 
            container
            direction="row"
            justify="space-around"
            alignItems="center"
            style={{paddingTop:10}}
            >
                <Copyright />
                <Typography variant="body2">
                    <Link className={classes.link} component={RouterLink} to="/contactUs">Contact Us</Link>
                    <Link className={classes.link} component={RouterLink} to="/faq">FAQ</Link>
                    <Link className={classes.link}component={RouterLink} to="/privacyPolicy">Privacy Policy</Link>
                </Typography>
            </Grid>
        </footer>
    );
}