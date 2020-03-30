// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';

// styles
import { makeStyles } from '@material-ui/core/styles';
import logo from '../navigation/ISELearning_logo.png';

const useStyles = makeStyles(theme => ({
    layout: {
        flexGrow: 1,
        height:'85vh',
      },
}));

export default function HomePage() {
    const classes = useStyles();
    return(
        <div className={classes.layout}>
            <h1><img src={logo} /></h1>
        </div>
    )
};