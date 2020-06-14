// react
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
// material-ui components
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    layout: { },
}));

export default function QuestionnairePage(props) {
    
    const classes = useStyles();
    return(
        <div className={classes.layout}>
            <h1><a href='https://material-ui.com/getting-started/templates/checkout/'>
                Exemplo de como fazer mover entre challeges no questionnaire
            </a></h1>
        </div>
    )
};