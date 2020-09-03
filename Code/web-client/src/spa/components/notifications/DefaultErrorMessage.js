// react
import React from 'react'
// material-ui components
import Grid from '@material-ui/core/Grid'
import Typography from '@material-ui/core/Typography'

export default function GenericPageMessage({ title, message }) {
    return(
        <Grid>
            <Grid 
            container
            direction="row"
            justify="center"
            alignItems="center"
            style={{paddingTop:250}}
            >
                <Typography variant="h3" color="textSecondary">
                    {title}
                </Typography>
            </Grid>
            <Grid 
            container
            direction="row"
            justify="center"
            alignItems="center"
            >
                <Typography variant="h5" color="textSecondary">
                    {message}
                </Typography>
            </Grid>
        </Grid>
    )
}