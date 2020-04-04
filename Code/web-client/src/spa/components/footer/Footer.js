// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import Container from '@material-ui/core/Container';
import Link from '@material-ui/core/Link';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary">
        {'Copyright © '}
            <Link color="inherit" href="https://material-ui.com/">
                ISE-Learning
            </Link>{' '}
        {new Date().getFullYear()}
        {'.'}
        </Typography>
    );
}

const useStyles = makeStyles((theme) => ({
    footer: {
        height: '10vh',
        marginTop: 'auto',
        backgroundColor: 'pink',
        position:'absolute'
    },
}));

export default function Footer() {
    const classes = useStyles();
    return (
        <div>
        <footer className={classes.footer}>
            <Container>
                <Typography>
                    <Link color="inherit" component={RouterLink} to="/contactUs">Contact Us</Link>
                </Typography>
                <Typography>
                    <Link color="inherit" component={RouterLink} to="/faq">FAQ</Link>
                </Typography>
                <Typography> 
                    <Link color="inherit" component={RouterLink} to="/privacyPolicy">Privacy Policy</Link>
                </Typography>
            <Copyright />
            </Container>
        </footer>
        </div>
    );
}