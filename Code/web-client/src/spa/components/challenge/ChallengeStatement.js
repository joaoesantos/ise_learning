// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    layout: { },
}));

export default function ChallengePage(props) {
    const classes = useStyles();

    return (
        <Paper variant="outlined" square elevation={1} >
            <h1>{props.challengeStatement.title}</h1>
            <div className={classes.layout}>
                {props.challengeStatement.text}
            </div>
        </Paper>
    )
};