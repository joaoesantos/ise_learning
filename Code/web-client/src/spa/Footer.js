// react
import React from 'react';

// material-ui components
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';

// styles
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
    layout: {
        height:'5vh',
        paddingTop: theme.spacing(3),
        paddingBottom: theme.spacing(3),
    },
}));

export default function Copyright() {
    const classes = useStyles();
    return (
        <div className={classes.layout}>
            <Typography variant="body2" color="textSecondary" align="center">
                {'Copyright © '}
                <Link color="inherit" href="https://material-ui.com/">
                    ISE-Learning
                </Link>{' '}
                {new Date().getFullYear()}
                {'.'}
            </Typography>
        </div>
    );
  }