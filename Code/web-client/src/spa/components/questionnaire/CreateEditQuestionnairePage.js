// react
import React from 'react'
import { useParams } from 'react-router-dom'
// material-ui components
import Button from '@material-ui/core/Button'
import Chip from '@material-ui/core/Chip'
import FormControl from '@material-ui/core/FormControl'
import Grid from '@material-ui/core/Grid'
import Input from '@material-ui/core/Input'
import MenuItem from '@material-ui/core/MenuItem'
import Select from '@material-ui/core/Select'
import Toolbar from '@material-ui/core/Toolbar'
import { makeStyles } from '@material-ui/core/styles'
import MaterialTable from 'material-table'
// other components
import { Formik, Form, Field, ErrorMessage } from 'formik'
import { TextField } from 'formik-material-ui'
// notifications
import CircularProgress from '../notifications/CircularProgress'
import CustomizedSnackbars from '../notifications/CustomizedSnackbars'
import DefaultErrorMessage from '../notifications/DefaultErrorMessage'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnaireController } from '../../controllers/questionnaire/QuestionnaireController'
// utils
import history from '../../components/navigation/history'

const useStyles = makeStyles(theme => ({
    layout: {},
    container: {
        padding: theme.spacing(1),
    },
    runCodetoolbar: {
        paddingLeft: theme.spacing(5),
    },
    chips: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    formControl: {
        margin: theme.spacing(1),
        background: 'fff',
        minWidth: 120,
        maxWidth: 300,
    },
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    button: {
        marginTop: theme.spacing(3),
        marginLeft: theme.spacing(1),
        margin: theme.spacing(3, 0, 2),
    },
    deleteButton: {
        marginTop: theme.spacing(3),
        marginLeft: theme.spacing(1),
        margin: theme.spacing(3, 0, 2),
        color: "#ffffff",
        backgroundColor:'#9b1003',
        '&:hover' : {
          backgroundColor: '#e3242b'
        }
    },
}));

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
      style: {
        maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
        width: 250,
      },
    },
  };

export default function CreateEditQuestionnairePage(props) {
    const challengesColumns = [
        { title: 'Description', field: 'challengeText' },
        // { title: 'Tags', field: 'tags' }
    ]

    const selectedColumns = [
        { title: 'Description', field: 'challengeText' },
        // { title: 'Tags', field: 'tags' },
        { title: 'Language', field: 'selectedLanguage', render: (rowData) => 
            <FormControl className={classes.formControl}>
                <Select
                    id="select-language-multiple"
                    multiple
                    value={rowData.selectedLanguages}
                    onChange={(event) => changeSelectedLanguages(event, rowData)}
                    input={<Input id="elect-language-multiple-chip" />}
                    renderValue={(selected) => (
                        <div className={classes.chips}>
                        {selected.map((value) => (
                            <Chip key={value} label={value} className={classes.chip} />
                        ))}
                        </div>
                    )}
                    MenuProps={MenuProps}
                >
                    {rowData.languages.map((language) => (
                        <MenuItem key={language} value={language}>
                            {language}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        }
    ]

    const classes = useStyles()
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)

    const { questionnaireId } = useParams()

    const [editable, setEditable] = React.useState(questionnaireId === 'undefined')
    const [questionnaire, setQuestionnaire] = React.useState()
    const [savedQuestionnaire, setSavedQuestionnaire] = React.useState(questionnaire)
    const [challengesData, setChallengesData] = React.useState()

    React.useEffect(() => {
        if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnaireController.getQuestionnaire,
                args: [questionnaireId],
                render: true
            })
        } else if (actionState === ActionStates.done && action.render && action.render === true) {
            setQuestionnaire(response.questionnaire)
            setSavedQuestionnaire(response.questionnaire)
            setChallengesData(response.challenges)
        } else if(response && actionState === ActionStates.done && action.name && action.name === 'deleteQuestionnaire') {
            history.push("/questionnaires")
        }
    }, [actionState]);

    const isChallengeSelected = (id) => (typeof questionnaire.selectedChallenges.find(element => element.id === id) !== 'undefined');

    const toggleEdit = async function () {
        setEditable((prev) => {
            return !prev
        })
    }

    const changeSelectedLanguages = function(event, changedChallenge){
        const sc = [...questionnaire.selectedChallenges]
        const idx = questionnaire.selectedChallenges.findIndex(e => e.id === changedChallenge.id)
        sc[idx].selectedLanguages = event.target.value
        setQuestionnaire({ ...questionnaire, selectedChallenges: sc })
    }

    const onTitleChangeHandler = (event) => {
        const { value } = event.target
        setQuestionnaire({ ...questionnaire, title: value })
    }

    const onTimerChangeHandler = (event) => {
        const { value } = event.target
        if (!isNaN(value)) {
            setQuestionnaire({ ...questionnaire, timer: value })
        }
    }

    const handleDelete = () => {
        setAction({
            function: QuestionnaireController.deleteQuestionnaire,
            args: [questionnaireId],
            name: "deleteQuestionnaire",
        })
    }

    const handleCancel = () => {
        setQuestionnaire(savedQuestionnaire)
        if(questionnaire.id !== undefined) {
            toggleEdit()
        }
    }

    const renderChallengesTable = () => {
        return (
            <MaterialTable
                style={{ height: '100%' }}
                columns={challengesColumns}
                data={challengesData}
                title="Available Challenges"
                actions={[
                    rowData => ({
                        icon: 'add',
                        tooltip: 'Add Challenge',
                        onClick: (event, rowData) => {
                            const newSelected = [...questionnaire.selectedChallenges]
                            rowData["selectedLanguages"] = []
                            newSelected.push(rowData)
                            newSelected.sort((a, b) => a.id - b.id)
                            setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                        },
                        disabled: !editable || isChallengeSelected(rowData.id),
                    })
                ]}
            />
        )
    }

    const renderSelectedChallengesTable = () => {
        return (
            <MaterialTable
                style={{ height: '100%' }}
                columns={selectedColumns}
                data={questionnaire.selectedChallenges}
                title="Selected Challenges"
                actions={[
                    {
                        icon: 'remove',
                        tooltip: 'Remove Challenge',
                        onClick: (event, rowData) => {
                            // let newSelected = [...questionnaire.selectedChallenges]
                            let newSelected = questionnaire.selectedChallenges.filter(c => c.id !== rowData.id)
                            setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                        },
                        disabled: !editable
                }]}
            />
        )
    }

    const renderCreateEditQuestionnairePage = () => {
        return (
            <Formik
                initialValues={{
                    title: questionnaire.title,
                    timer: questionnaire.timer
                }}
                onSubmit={async (values, {setSubmitting}) => {
                    console.log("rrrr", questionnaire)
                    setSubmitting(false)
                    setAction({
                        function: QuestionnaireController.saveQuestionnaire,
                        args: [questionnaire],
                        render: true
                    })
                    toggleEdit()
                }}
            >
                {({ isSubmitting, errors }) => (
                    <div >
                        <Form>
                            <Toolbar variant="dense">
                                <Grid container spacing={5} direction="row" className={classes.container}>
                                    <Grid item >
                                        <Field
                                            component={TextField}
                                            name="title"
                                            label="Title"
                                            disabled={!editable}
                                            InputProps={{onChange:onTitleChangeHandler, value:questionnaire.title || ''}}
                                            style={{width:'50ch'}}
                                        />
                                    </Grid>
                                    <Grid item >
                                        <Field
                                            component={TextField}
                                            name="timer"
                                            label="Timer (minutes)"
                                            value={questionnaire.timer}
                                            disabled={!editable}
                                            InputProps={{onChange:onTimerChangeHandler, value:questionnaire.timer || ''}}
                                        />
                                    </Grid>
                                </Grid>
                                {/* <Grid container spacing={0} direction="row">
                                    <Grid item xs={2}>
                                        <Grid container spacing={2} direction="column">
                                            <Grid item >
                                                <Field
                                                    component={TextField}
                                                    name="title"
                                                    label="Title"
                                                    disabled={!editable}
                                                    InputProps={{onChange:onTitleChangeHandler, value:questionnaire.title || ''}}
                                                    style={{width:'50ch'}}
                                                />
                                            </Grid>
                                            <Grid item>
                                                <ErrorMessage name="title" />
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                    <Grid item xs={2}>
                                        <Grid container spacing={2} direction="column">
                                            <Grid item >
                                                <Field
                                                    component={TextField}
                                                    name="timer"
                                                    label="Timer (minutes)"
                                                    value={questionnaire.timer}
                                                    disabled={!editable}
                                                    InputProps={{onChange:onTimerChangeHandler, value:questionnaire.timer || ''}}
                                                />
                                            </Grid>
                                            <Grid item>
                                                <ErrorMessage name="title" />
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                </Grid> */}
                            </Toolbar>

                            <Grid container spacing={2}>
                                <Grid item xs={7}>
                                    {renderChallengesTable()}
                                </Grid>
                                <Grid item xs={5}>
                                    {renderSelectedChallengesTable()}
                                </Grid>
                            </Grid>
                            <div className={classes.buttons}>
                                {!editable && (

                                    <React.Fragment>
                                        <Button
                                            color="primary"
                                            variant="contained"
                                            className={classes.button}
                                            onClick={toggleEdit}
                                        >
                                            Edit
                                        </Button>
                                        <Button className={classes.deleteButton}
                                            variant="contained"
                                            onClick={handleDelete}
                                        >
                                            Delete
                                        </Button>
                                    </React.Fragment>

                                )}
                                {editable && (
                                        <React.Fragment>
                                            <Button
                                                color="primary"
                                                variant="contained"
                                                type="submit"
                                                className={classes.button}
                                                disabled={isSubmitting}
                                            >
                                                Save
                                            </Button>
                                            <Button
                                                color="primary"
                                                variant="contained"
                                                onClick={handleCancel}
                                                className={classes.button}
                                            >
                                                Cancel
                                            </Button>
                                        </React.Fragment>
                                    )
                                }
                            </div>
                        </Form>
                    </div>
                )}
            </Formik>
        )
    }

    if (actionState === ActionStates.clear || actionState === ActionStates.inProgress) {
        return <CircularProgress />
    } else if (actionState === ActionStates.done && questionnaire) {
        return (
            <>
                {actionState === ActionStates.done && response && response.message && 
                    <CustomizedSnackbars message={response.message} severity={response.severity} />}
                {renderCreateEditQuestionnairePage()}
            </>
        )
    } else {
        return <DefaultErrorMessage message={"404 | Not Found"} />
    }

}