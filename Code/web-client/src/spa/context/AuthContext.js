// react
import React, { createContext } from 'react'
// context hooks
import useAuth from './hooks/useAuth'

export { AuthContext, AuthProvider }

const AuthContext = createContext()

function AuthProvider({ children }) {
    const { isAuthed, user, handleSignIn, handleLogin, handleLogout } = useAuth()
    return(
        <AuthContext.Provider
            value = {{
                isAuthed,
                user,
                handleSignIn,
                handleLogin,
                handleLogout
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}