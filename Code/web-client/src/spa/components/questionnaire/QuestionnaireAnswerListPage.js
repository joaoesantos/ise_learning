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
    return(
        <MaterialTable
        options =
        {{
          search:true, 
          sorting:true,
        }}
        title="Test table"
        columns={table.columns}
        data={table.data}
        />
    )
}