// react
import React, { useState, createContext } from 'react'

export { ThemeContext, ThemeProvider }

const ThemeContext = createContext()

export const themes = {
    light: {
        colors: {
            background: "white",
            text: "black"
        }
    },
    dark: {
        colors: {
            background: "black",
            text: "white"
        }
    },
}

export default function ThemeProvider({ children }) {
    const [theme, setTheme] = React.useState(themes.light)
    return(
        <ThemeContext.Provider
            value = {{
                theme,
                setTheme
            }}
        >
            {children}
        </ThemeContext.Provider>
    )
}