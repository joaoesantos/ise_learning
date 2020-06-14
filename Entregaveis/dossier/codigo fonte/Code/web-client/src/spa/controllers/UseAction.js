// react 
import { useEffect, useReducer } from 'react'

export const ActionStates = {
  clear: 'clear',
  inProgress: 'inProgress',
  done: 'done',
  error: 'error'
}

const ActionsTypes = {
  clear: 'clear',
  start: 'start',
  response: 'response',
  error: 'error',
}

function reducer(state, action) {
  switch (action.type) {
    case ActionsTypes.clear:
      return { actionState: ActionStates.clear }
    case ActionsTypes.start:
      return { actionState: ActionStates.inProgress }
    case ActionsTypes.response:
      return { actionState: ActionStates.done, response: action.response }
    case ActionsTypes.error:
    default:
      return { actionState: ActionStates.error, response: action.response }
  }
}

export default function UseFetch(actionPayload) {
  const [{actionState, response}, disp] = useReducer(reducer, { actionState: ActionStates.clear })

  useEffect(() => {
    async function performAction () {
      try {
        if (!actionPayload || !actionPayload.function) {
          disp({ type: ActionsTypes.clear })
          return
        }
        if(!actionPayload.args) {
          actionPayload.args = []
        }
        disp({ type: ActionsTypes.start })
        let res = actionPayload.function.apply(undefined, actionPayload.args)
        if(res instanceof Promise) {
          res = await res;
        }
        disp({ type: ActionsTypes.response, response: res })
      } catch (e) {
        disp({ type: ActionsTypes.error, response: e })
      }
    }
    performAction()
  }, [actionPayload,disp])

  return [actionState, response]
}
