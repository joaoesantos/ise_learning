import React from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { makeStyles } from '@material-ui/core/styles';
import { TextField } from 'formik-material-ui';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Select from '@material-ui/core/Select';
import Toolbar from '@material-ui/core/Toolbar';
import FormControl from '@material-ui/core/FormControl';
import MaterialTable from 'material-table';
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { defaultLanguage } from '../../clientSideConfig';
import { QuestionnaireController } from '../../controllers/QuestionnaireController'

const useStyles = makeStyles(theme => ({
    layout: {},
    buttons: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    button: {
        marginTop: theme.spacing(3),
        marginLeft: theme.spacing(1),
        margin: theme.spacing(3, 0, 2),
        backgroundColor: "#be5041",
        '&:hover': {
            backgroundColor: '#cf6744',
        }
    },
    container: {
        padding: theme.spacing(1),
    },
    runCodetoolbar: {
        paddingLeft: theme.spacing(5),
    }
}));

export default function CreateEditQuestionnaire(props) {
    /*
        Title ---> x
        Challenge search by tag/name ---> x
        table with search results or with selected challenges -> x
        link button, with box to show the created link --> x
        save and cancel button -> x
    
        The questionnaire can have a language associated with it, in that case the chalenge solutions can only be of that language type
    */
    const columns = [
        { title: 'Title', field: 'title' },
        { title: 'Tags', field: 'tags' }
    ]

    const selectedColumns = [
        { title: 'Title', field: 'title' },
        { title: 'Tags', field: 'tags' },
        { title: 'Language', field: 'selectedLanguage', render: (rowData) =>
        <Select
        id="languageSelect"
        native
        label="Language"
        disabled={!editable}
        value={rowData.selectedLanguages}
    >
        <option value={''}>Select</option>
        <option value={'java'}>Java</option>
        <option value={'kotlin'}>Kotlin</option>
        <option value={'javascript'}>JavaScript</option>
        <option value={'csharp'}>C#</option>
        <option value={'python'}>Python</option>
    </Select>
    }
    ]
    const classes = useStyles();
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [editable, setEditable] = React.useState(typeof props.id == 'undefined')
    const [questionnaire, setQuestionnaire] = React.useState({
        id: null,
        title: '',
        language: '',
        selectedChallenges: []
    })

    const [savedQuestionnaire, setSavedQuestionnaire] = React.useState(questionnaire)
    const [challengesData, setChallengesData] = React.useState([])

    React.useEffect(() => {
        if (response === undefined && actionState === ActionStates.clear) {
            setAction({
                function: QuestionnaireController.getQuestionnaire,
                args: [props.id],
                render: true
            })
        } else if (actionState === ActionStates.done &&
            action.render && action.render === true) {
            setQuestionnaire(response.questionnaire)
            setSavedQuestionnaire(response.questionnaire)
            setChallengesData(response.challenges)
        } else {
            //not Done || done but not rendering
        }
    }, [actionState]);

    const isChallengeSelected = (id) => questionnaire.selectedChallenges.find(element => element.id == id);

    const toggleEdit = async function () {
        setEditable((prev) => {
            return !prev
        })
    }

    const onTitleChangeHandler = function (event) {
        const { value } = event.target
        setQuestionnaire({ ...questionnaire, title: value })
    }

    const handleCancel = function (event) {
        setQuestionnaire(savedQuestionnaire)
        toggleEdit()
    }

    const createLink = function (event) {
        alert('this should be a link')
    }

    const renderQuestionnaire = function () {
        console.log(challengesData)
        return (
            <Formik
                initialValues={{
                    title: ''
                }}
                // validationSchema={Yup.object({
                //     title: Yup.string()
                //         .required('Required')
                // })}
                onSubmit={async (values, {setSubmitting}) => {
                    setSubmitting(false)
                    const newQuestionnaire = await QuestionnaireController.saveQuestionnaire(questionnaire)
                    setSavedQuestionnaire(newQuestionnaire)
                    toggleEdit()
                }}
            >
                {({ isSubmitting, errors }) => (
                    <div className={classes.container}>
                        <Form>
                            <Toolbar variant="dense">
                                <Grid container spacing={0}>
                                    <Grid item xs={2}>
                                        <Grid container spacing={2} direction="column">
                                            <Grid item >
                                                <Field
                                                    component={TextField}
                                                    name="title"
                                                    label="Title"
                                                    disabled={!editable}
                                                    onChange={onTitleChangeHandler}
                                                />
                                            </Grid>
                                            <Grid item>
                                                <ErrorMessage name="title" />
                                            </Grid>
                                        </Grid>


                                    </Grid>
                                </Grid>
                            </Toolbar>


                            <Grid container spacing={2}>
                                <Grid item xs={7}>
                                    <MaterialTable
                                        style={{ height: '100%' }}
                                        columns={columns}
                                        data={challengesData}
                                        title="Available Challenges"
                                        actions={[
                                            rowData => ({
                                                icon: 'add',
                                                tooltip: 'Add Challenge',
                                                onClick: (event, rowData) => {
                                                    const newSelected = [...questionnaire.selectedChallenges]
                                                    newSelected.push(rowData)
                                                    newSelected.sort((a, b) => a.id - b.id)
                                                    setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                                                },
                                                disabled: !editable || isChallengeSelected(rowData.id),
                                            })
                                        ]}
                                    />
                                </Grid>
                                <Grid item xs={5}>
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
                                                    let newSelected = [...questionnaire.selectedChallenges]
                                                    newSelected = questionnaire.selectedChallenges.filter(c => c.id != rowData.id)
                                                    setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                                                },
                                                disabled: !editable
                                            }]}
                                    />
                                </Grid>
                            </Grid>
                            <div className={classes.buttons}>
                                {!editable && (

                                    <React.Fragment>
                                        <Button
                                            color="primary"
                                            variant="contained"
                                            className={classes.button}
                                            onClick={() => toggleEdit()}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={createLink}
                                            className={classes.button}
                                        >
                                            Create Link
                                        </Button>
                                    </React.Fragment>

                                )}
                                {
                                    editable && (
                                        <React.Fragment>
                                            <Button
                                                variant="contained"
                                                color="primary"
                                                type="submit"
                                                className={classes.button}
                                                disabled={isSubmitting}
                                            >
                                                Save
                                            </Button>
                                            <Button
                                                variant="contained"
                                                color="primary"
                                                onClick={handleCancel}
                                                className={classes.button}>
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

    if (actionState === ActionStates.clear) {
        return <p>insert URL</p>
    } else if (actionState === ActionStates.inProgress) {
        return <p>fetching...</p>
    } else if (actionState === ActionStates.done && questionnaire) {
        return (
            renderQuestionnaire()
        )
    } else {
        return <p>error...</p>
    }
};