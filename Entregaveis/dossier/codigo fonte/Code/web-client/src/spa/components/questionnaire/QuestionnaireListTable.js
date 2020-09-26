// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Link from '@material-ui/core/Link'
import Paper from '@material-ui/core/Paper'
import MaterialTable from 'material-table'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { QuestionnaireController } from '../../controllers/questionnaire/QuestionnaireController'

export default function QuestionnaireListTable() {

  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
      setAction({
        function: QuestionnaireController.getAllUserQuestionnaires,
        args: [],
        render: true
      })
    } else if (response && response.json && 
        actionState === ActionStates.done && action.render && action.render === true) {
      let data = []
      response.json.forEach(it => data.push(
          { 
            id: it.questionnaireId, 
            title: it.description,
            timer: it.timer ? parseInt(it.timer)/(1000*60) : "N/A",
            creationDate: it.creationDate
          }))
      setTable({...table, data:data})
    } 
  },[actionState]);

  // table data
  const [table, setTable] = React.useState({
      columns: [
          { title: '#', field: 'id' , width: '5%', cellStyle: {color: '#777777'} },
          { title: 'Title', field: 'title',
          render: questionnaire => 
            <Link component={RouterLink} to={`/editQuestionnaire/${questionnaire.id}`} key={`${questionnaire.id}`} >
              {`${questionnaire.title}`}
            </Link>
          },
          { title: "Timer (min)", field: 'timer' },
          { title: "Creation date", field: 'creationDate' },
          { title: 'Questionnaire instances', field: 'instances',
          render: questionnaire => 
            <Link component={RouterLink} to={`/questionnaireInstances/${questionnaire.id}`} key={`${questionnaire.id}`} >
              {`Show`}
            </Link>
          },
      ],
      data: [],
  });

  return (
      <MaterialTable
        title = ""
        options =
        {{
            search:true, 
            sorting:true,
        }}
        columns={table.columns}
        data={table.data}
        components={{
          Container: props => <Paper {...props} elevation={0}/>
        }}
      />
  )

}