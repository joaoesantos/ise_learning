// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Container from '@material-ui/core/Container'
import Grid from '@material-ui/core/Grid'
import Link from '@material-ui/core/Link'
import { makeStyles } from '@material-ui/core/styles'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import MaterialTable from 'material-table'
// components
import { QuestionnaireInstanceController } from '../../controllers/questionnaire/QuestionnaireInstanceController'
// notifications
import CircularProgress from '../notifications/CircularProgress'
import CustomizedSnackbars from '../notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
// utils
import { API_VERSION } from '../../clientSideConfig'

const useStyles = makeStyles((theme) => ({
  mainContainer: {
    maxWidth: '90%',
    padding: theme.spacing(2),
  },
  toolbar: {
    borderBottom: `1px solid ${theme.palette.divider}`,
    justifyContent: "space-between"
  },
  link: {
    paddingLeft: 15,
    color: '#db9e07',
    textDecoration: 'none',
    '&:hover, &:focus' : {
      color: '#be5041',
      textDecoration: 'none',
    }
  },
}));

export default function QuestionnaireinstanceListPage(props) {

    const classes = useStyles()
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)

    const questionnaireId = props.match.params.questionnaireId

    // table data
    const [table, setTable] = React.useState({
        columns: [
            { title: 'Description', field: 'description' },
            { title: "Timer (min)", field: 'timer' },
            { title: "Status", field: 'status', editable: 'never' },
            { title: 'Link', field: 'link', editable: 'never' },
            { title: 'Questionnaire answers', field: 'answers', editable: 'never',
            render: questionnaire => 
                <Link component={RouterLink} to={`/questionnaireAnswer/${questionnaire.id}`} key={`${questionnaire.id}`} >
                {`Show`}
                </Link>
            },
        ],
        data: [],
    })

    console.log(response)

    React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
        setAction({
            function: QuestionnaireInstanceController.getAllQuestionnaireInstancesByQuestionnaireId,
                args: [questionnaireId],
                render: true
        })
    } else if (response && response.json && actionState === ActionStates.done && action.render) {
        let data = []
        response.json.forEach(it => data.push(
            {
                questionnaireInstanceId: it.questionnaireInstanceId, 
                description: it.description,
                timer: it.timer ? parseInt(it.timer)/(1000*60) : "N/A",
                status: it.isFinish ? "Closed" : "Open",
                link: `${window.location.hostname}/${API_VERSION}/questionnaireInstances/solve/${it.questionnaireInstanceUuid}`
            }))
        setTable({...table, data:data})
    } 
    },[actionState]);

    if(actionState === ActionStates.clear) {
        return <CircularProgress />
    } else {
        return (
            <>
                {actionState === ActionStates.done && response.message && 
                    <CustomizedSnackbars message={response.message} severity={response.severity} />}
                <Container className={classes.mainContainer}>
                <Grid 
                    container 
                    direction="row"
                    justify="space-evenly"
                    alignItems="center"
                    spacing={3}
                >
                    <Grid item xs={12} sm={12}>
                    <Toolbar className={classes.toolbar} variant="regular" >
                        <Typography variant="h5">
                            {`Instances of Questionnaire # ${questionnaireId}`}
                        </Typography>
                    </Toolbar>
                    <MaterialTable
                        columns={table.columns}
                        data={table.data}
                        title=""
                        editable={{
                            onRowAdd: (newData) =>
                            new Promise((resolve) => {
                                setAction({
                                    function: QuestionnaireInstanceController.createQuestionnaireInstance,
                                        args: [{
                                            questionnaireId: questionnaireId,
                                            description: newData.description,
                                            timer: parseInt(newData.timer)*(1000*60)
                                        }],
                                        render: true
                                })
                                resolve()
                            }),
                            onRowUpdate: (newData, oldData) =>
                            new Promise((resolve) => {
                                console.log(newData)
                                setAction({
                                    function: QuestionnaireInstanceController.updateQuestionnaireInstance,
                                        args: [{
                                            questionnaireId: questionnaireId,
                                            questionnaireInstanceId: newData.questionnaireInstanceId,
                                            description: newData.description,
                                            timer: parseInt(newData.timer)*(1000*60)
                                        }],
                                        render: true
                                })
                              resolve()
                            }),
                            onRowDelete: (oldData) =>
                                new Promise((resolve) => {
                                    setAction({
                                        function: QuestionnaireInstanceController.deleteQuestionnaireInstance,
                                            args: [{
                                                questionnaireId: questionnaireId,
                                                questionnaireInstanceId: oldData.questionnaireInstanceId,
                                            }],
                                            render: true
                                    })
                                resolve()
                            }),
                        }} 
                    />
                    </Grid>
                </Grid>
                </Container>
            </>
        )
    }

}