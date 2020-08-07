/*
search by user, challenger name, tags
table with questionnaire answers that goes to challenge answers list

create page with challenge answer list
*/

import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import UseAction, { ActionStates } from '../../controllers/UseAction'
import MaterialTable from "material-table"

export default function QuestionnaireAnswerListPage(props) {
    <MaterialTable
    options =
    {{
      search:true, 
      sorting:true,
    }}
    title="Project Labels"
    columns={table.columns}
    data={table.data}
    editable={{
      onRowAdd: (newData) =>
        new Promise((resolve) => {
          let action = siren.actions.find(a => a.name.includes('create-label'))
          setUrl(action.href)
          setOptions({ ...options, method: action.method, body: newData })
          pushGetSelfToNextFetch()
          resolve()
      }),
      onRowUpdate: (newData, oldData) =>
        new Promise((resolve) => {
          let labelId = siren.entities[table.data.indexOf(oldData)].properties.id
          let action = siren.actions.find(a => a.name.includes('edit-label'))
          let href = action.href.replace("{labelId}", labelId)
          setUrl(href)
          setOptions({ ...options, method: action.method, body: newData })
          pushGetSelfToNextFetch()
          resolve()
      }),
      onRowDelete: (oldData) =>
        new Promise((resolve) => {
          let labelId = siren.entities[table.data.indexOf(oldData)].properties.id
          let action = siren.actions.find(a => a.name.includes('delete-label'))
          let href = action.href.replace("{labelId}", labelId)
          setUrl(href)
          setOptions({ ...options, method: action.method })
          pushGetSelfToNextFetch()
          resolve()
        }),
      }}
      localization={{
        header: {actions:undefined},
      }}
      components={{
        Pagination: () => (
          <td>
            <TablePagination
              component="div"
              page={page}
              count={-1}
              rowsPerPage={rowsPerPage.labels}
              rowsPerPageOptions={[]}
              onChangePage={handleChangePage}
            />
          </td>
        ),
      }}
    />
}