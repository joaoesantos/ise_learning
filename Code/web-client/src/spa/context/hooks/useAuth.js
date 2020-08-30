// react
import React, { useState, useEffect } from 'react'

import history from '../../components/navigation/history'

import { apiUrlTemplates } from '../../clientSideConfig'
import { HttpMethods, fetchHeaders } from '../../utils/fetchUtils'

export default function useAuth() {

    const [isAuthed, setAuth] = useState(false)
    const [user, setUser] = useState()

    useEffect(() => {
        const credentials = localStorage.getItem('credentials')
        if(credentials) {
            fetchHeaders.append({ key: "Authorization", value: `Basic ${credentials}` })
            setAuth(true)
        }
    }, [])

    async function handleSignIn({ data }) {

        let url = apiUrlTemplates.createUser()
        let options = {
            method: HttpMethods.get,
            headers: fetchHeaders.get(),
            body: data
        }

        console.log(data)

        // let response = await fetch(url, options)
        // if(response.ok) {
        //     setAuth(true)
        //     setUser(response)
        //     let credentials = btoa(`${data.username}:${data.password}`)
        //     localStorage.setItem('credentials', credentials)
        //     fetchHeaders.append({ key: "Authorization", value: `Basic ${credentials}` })
        //     history.push("/")
        // } else {
        //     // todo handle not ok
        // }

    }

    async function handleLogin({ credentials }) {
        
        let url = apiUrlTemplates.login()
        let options = {
            method: HttpMethods.get,
            headers: fetchHeaders.get()
        }

        // let response = await fetch(url, options)
        // if(response.ok) {
            setAuth(true)
            //setUser(response)
            localStorage.setItem('credentials', credentials)
            fetchHeaders.append({ key: "Authorization", value: `Basic ${credentials}` })
            history.push("/")
        // } else {
        //     // todo handle not ok
        // }

    }

    function handleLogout() {
        setAuth(false)
        setUser(undefined)
        localStorage.removeItem('credentials')
        fetchHeaders.headers = undefined
        history.push("/")
    }

    return { isAuthed, user, handleSignIn, handleLogin, handleLogout }

}