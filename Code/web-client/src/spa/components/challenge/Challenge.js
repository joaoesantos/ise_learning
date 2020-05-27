import React, {useState} from 'react'
import Paper from '@material-ui/core/Paper';

const Challenge = function(props){

    const [state, setstate] = useState({
        challengeId: props.challenge.id,
        challengeText: props.challenge.challengeText,
        unitTests: "",
        code: ""
    })

    return(
        <React.Fragment>
            <Paper>
                <p>{state.challengeText}</p>
                <p>{state.unitTests}</p>
                <p>{state.code}</p>
            </Paper>

        </React.Fragment>
    )
}

export default Challenge
