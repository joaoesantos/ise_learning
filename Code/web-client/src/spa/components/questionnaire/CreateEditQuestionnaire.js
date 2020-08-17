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
import Chip from '@material-ui/core/Chip';
import MenuItem from '@material-ui/core/MenuItem';
import Input from '@material-ui/core/Input';
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
    }
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

export default function CreateEditQuestionnaire(props) {
    const columns = [
        { title: 'Description', field: 'challengeText' },
        { title: 'Tags', field: 'tags' }
    ]

    const selectedColumns = [
        { title: 'Description', field: 'challengeText' },
        { title: 'Tags', field: 'tags' },
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
    const classes = useStyles();
    const [action, setAction] = React.useState()
    const [actionState, response] = UseAction(action)
    const [editable, setEditable] = React.useState(typeof props.id == 'undefined')
    const [questionnaire, setQuestionnaire] = React.useState({
        id: null,
        title: '',
        language: '',
        timer: '123',
        selectedChallenges: []
    })



    const [savedQuestionnaire, setSavedQuestionnaire] = React.useState(questionnaire)
    const [challengesData, setChallengesData] = React.useState([])
    const [testTimer, setTestTimer] = React.useState('')

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

    const isChallengeSelected = (id) => (typeof questionnaire.selectedChallenges.find(element => element.id == id) != 'undefined');

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

    const onTitleChangeHandler = function (event) {
        const { value } = event.target
        setQuestionnaire({ ...questionnaire, title: value })
    }

    const onTimerChangeHandler = function (event) {
        const { value } = event.target
        if (!isNaN(value)) {
            setQuestionnaire({ ...questionnaire, timer: value })
          }
    }

    const handleCancel = function (event) {
        setQuestionnaire(savedQuestionnaire)
        toggleEdit()
    }

    const createLink = function (event) {
        alert('this should be a link')
    }

    const renderQuestionnaire = function () {
        return (
            <Formik
                initialValues={{
                    title: questionnaire.title,
                    timer:questionnaire.timer
                }}
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
                                                    InputProps={{onChange:onTitleChangeHandler, value:questionnaire.title}}
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
                                                    InputProps={{onChange:onTimerChangeHandler, value:questionnaire.timer}}
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
                                                    rowData["selectedLanguages"] = []
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