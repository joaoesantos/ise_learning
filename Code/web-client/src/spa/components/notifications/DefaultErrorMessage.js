// react
import React from 'react'
// material-ui components
import Grid from '@material-ui/core/Grid'
import Typography from '@material-ui/core/Typography'

export default function DefaultErrorMessage({ message }) {
    return(
        <div>
            <Grid 
                container
                direction="column"
                alignItems="center"
                justify="center"
                style={{ minHeight: '80vh' }}
            >
                <Typography variant="h5" color="textSecondary">
                    {message}
                </Typography>
            </Grid>
        </div>
    )
}