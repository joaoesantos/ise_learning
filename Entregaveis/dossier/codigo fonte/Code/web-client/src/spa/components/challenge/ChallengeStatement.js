// react
import React from 'react'
// material-ui components
import Paper from '@material-ui/core/Paper'
import { makeStyles } from '@material-ui/core/styles'
import TextField from '@material-ui/core/TextField'

const useStyles = makeStyles(theme => ({
    challengeStatementText: {
        width: '100%',
    },
    challengeStatementPage: {
        height: '100%',
        padding: theme.spacing(2),
    }
}));

export default function ChallengePage(props) {
    const classes = useStyles()
    return (
        <Paper variant="outlined" square={true} className={classes.challengeStatementPage} >
            <TextField
                id="challengeStatement"
                label="Challenge Statement"
                multiline
                variant="outlined"
                rowsMax={38}
                value={props.challengeStatement}
                disabled={props.readOnly}
                onChange={props.setChallengeStatement}
                className={classes.challengeStatementText}
            />
        </Paper>
    )
}