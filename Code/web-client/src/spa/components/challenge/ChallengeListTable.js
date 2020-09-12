// react
import React from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Link from '@material-ui/core/Link'
import Paper from '@material-ui/core/Paper'
import MaterialTable from 'material-table'
// controllers
import UseAction, { ActionStates } from '../../controllers/UseAction'
import { ChallengeController } from '../../controllers/challenge/ChallengeController'
// utils
import { importAllImagesFromFolder } from '../../utils/utils'
const codeLanguageIcons = importAllImagesFromFolder(require.context('../../images/icons/codeLanguages', false, /\.(png|jpe?g|svg)$/))

export default function ChallengeListTable(){

  const [action, setAction] = React.useState()
  const [actionState, response] = UseAction(action)

  React.useEffect(() => {
    if (response === undefined && actionState === ActionStates.clear) {
      setAction({
        function: ChallengeController.getAllChallenges,
        args: [],
        render: true
      })
    } else if (actionState === ActionStates.done &&
    action.render && action.render === true) {
      let data = []
      response.json.forEach(it => data.push(
          { 
            id: it.challengeId, 
            title: it,
            solution: it.solutions
          }))
      setTable({...table, data:data})
    } 
  },[actionState]);

  // table data
  const [table, setTable] = React.useState({
      columns: [
          { title: '#', field: 'id' , width: '5%', cellStyle: {color: '#777777'} },
          { title: 'Title', field: 'title', width: '45%',
          render: challenge => 
            <Link component={RouterLink} to={`challenges/${challenge.title.challengeId}`} key={`${challenge.title.challengeId}`}>
              {`${challenge.title.challengeTitle}`}
            </Link>
          },
          { title: 'Solution', field: 'solution', 
          render: challengeSolutions => challengeSolutions.solution.map(it =>
              //<Link component={RouterLink} to={`/`} key={`${it.solutionId}`}> 
                  <img src={codeLanguageIcons[it.codeLanguage]} height={24} />
              //</Link>
            )
          }
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