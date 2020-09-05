// react
import React from 'react'
// context hooks
import useAuth from './hooks/useAuth'

export { AuthContext, AuthProvider }

const AuthContext = React.createContext()

function AuthProvider({ children }) {
    const { isAuthed, setAuth, user, setUser } = useAuth()
    return(
        <AuthContext.Provider value = {{ isAuthed, setAuth, user, setUser }} >
            {children}
        </AuthContext.Provider>
    )
}