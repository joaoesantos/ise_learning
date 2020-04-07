// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    layout: { },
}));

export default function ChallengePage() {
    const classes = useStyles();
    return(
        <div className={classes.layout}>
            <h1>Challenges</h1>
        </div>
    )
};