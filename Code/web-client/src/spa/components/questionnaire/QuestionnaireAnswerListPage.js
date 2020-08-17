import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import UseAction, { ActionStates } from '../../controllers/UseAction'
import Link from '@material-ui/core/Link';
import { Link as RouterLink } from 'react-router-dom';
import MaterialTable from "material-table"

import questionnaireCtrl from '../../controllers/questionnaireCtrl'

export default function QuestionnaireAnswerListPage(props) {

  const useStyles = makeStyles((theme) => ({}))
  const classes = useStyles()
  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
      setAction({
        function: questionnaireCtrl.getQuestionnaireInstances,
        args: [props.credentials],
        render: true
      })
    } else if (actionState === ActionStates.done &&
      action.render && action.render === true) {
      setTable({...table, data:response})
    } else {
      //not Done || done but not rendering
    }
  },[actionState]);

  const [table, setTable] = React.useState({
    columns:[
      { title: 'Questionnaire', field: 'questionnaireDescription' },
      { title: 'Instance', field: 'questionnaireInstanceDescription' },
      { 
        field: 'url', 
        render: rowData => <Link className={classes.link} component={RouterLink} to={`/questionnaireAnswer/${rowData.questionnaireInstanceId}`}>Show</Link>
      }
    ],
    data: [
    ]
  })

  return (
    <MaterialTable
      options=
      {{
        search: true,
        sorting: true,
      }}
      title="Questionnaires Created"
      columns={table.columns}
      data={table.data}
    />
  )
}