// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';

// styles
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    layout: {
        flexGrow: 1,
        height:'85vh',
      },
}));

export default function ChallengePage() {
    const classes = useStyles();
    return(
        <div className={classes.layout}>
            <h1>Challenges</h1>
        </div>
    )
};