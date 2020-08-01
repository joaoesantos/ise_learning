import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import MaterialTable from 'material-table';
import UseAction, { ActionStates } from '../../controllers/UseAction'

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
    }
}));

export default function CreateEditQuestionnaire(props) {
    /*
        Title
        Challenge search by tag/name
        table with search results or with selected challenges
        link button, with box to show the created link
        save and cancel button
    
        The questionnaire can have a language associated with it, in that case the chalenge solutions can only be of that language type
    */
    const columns = [
        { title: 'Title', field: 'title' },
        { title: 'Tags', field: 'tags' }
    ]

    const classes = useStyles();
    const [editable, setEditable] = React.useState(false)
    const [questionnaire, setQuestionnaire] = React.useState({
        id: 0,
        title: 'This is a title',
        selectedChallenges: []
    })

    const [challengesData, setChallengesData] = React.useState([
        {
            id: 1,
            title: 'cenas1',
            tags: 'a, b, c'
        },
        {
            id: 2,
            title: 'cenas2',
            tags: 'a, b, dad'
        },
        {
            id: 3,
            title: 'cenas3',
            tags: 'a, b, oiopq'
        }
    ])

    React.useEffect(() => { },)

    const toggleEdit = async function () {
        setEditable((prev) => {
            return !prev
        })
    }

    const onTitleChangeHandler = function (event) {
        const { value } = event.target
        setQuestionnaire({ ...questionnaire, title: value })
    }

    const handleSave = function (event) {
        console.log('save')
    }

    const handleCancel = function (event) {
        toggleEdit()
        console.log('cancel')
    }

    const buttonToolbar = function () {
        return (<div className={classes.buttons}>
            {!editable && (
                <Button
                    color="primary"
                    variant="contained"
                    className={classes.button}
                    onClick={() => toggleEdit()}
                >
                    Edit
                </Button>
            )}
            {
                editable && (
                    <React.Fragment>
                        <Button
                            variant="contained" A
                            color="primary"
                            onClick={handleSave}
                            className={classes.button}>
                            Save
                        </Button>
                        <Button
                            variant="contained" A
                            color="primary"
                            onClick={handleCancel}
                            className={classes.button}>
                            Cancel
                        </Button>
                    </React.Fragment>
                )
            }
        </div>
        )
    }

    return (
        <React.Fragment>
            <TextField required disabled={!editable} label="Title" value={questionnaire.title} onChange={onTitleChangeHandler} />
            <MaterialTable
                columns={columns}
                data={challengesData}
                title="Demo Title"
                actions={[
                    {
                        icon: 'add',
                        tooltip: 'Add Challenge',
                        onClick: (event, rowData) => {
                            const newSelected = [...questionnaire.selectedChallenges]
                            newSelected.push(rowData)
                            setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                        },
                        disabled: !editable,
                    }]}
            />

            <MaterialTable
                columns={columns}
                data={questionnaire.selectedChallenges}
                title="Demo Title"
                actions={[
                    {
                        icon: 'remove',
                        tooltip: 'Remove Challenge',
                        onClick: (event, rowData) => {
                            const newSelected = questionnaire.selectedChallenges.filter(c => c.id != rowData.id)
                            setQuestionnaire({ ...questionnaire, selectedChallenges: newSelected })
                        }
                    }]}
            />
            {buttonToolbar()}
        </React.Fragment>
    )
};