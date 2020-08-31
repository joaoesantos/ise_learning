// react
import React from 'react'

export default function useAuth() {

    const [isAuthed, setAuth] = React.useState(false)
    const [user, setUser] = React.useState()

    React.useEffect(() => {
        const storedlUser = localStorage.getItem('user')
        //localStorage.setItem('credentials', credentials)
        if(storedlUser) {
            setAuth(true)
            setUser(storedlUser)
        }
    }, [])

    return { isAuthed, setAuth, user, setUser }

}