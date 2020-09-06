// react
import React from 'react'

export default function useAuth() {

    const [isAuthed, setAuth] = React.useState(false)
    const [user, setUser] = React.useState()

    React.useEffect(() => {
        const storedUser = localStorage.getItem('ISELearningLoggedUser')
        if(storedUser) {
            setAuth(true)
            setUser(JSON.parse(storedUser))
        }
    }, [])

    return { isAuthed, setAuth, user, setUser }

}