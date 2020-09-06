// react
import React from 'react'
// material-ui
import { ThemeProvider as MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles'

export { ThemeContext, ThemeProvider }

const ThemeContext = React.createContext()

export const themes = {
    light: 
        createMuiTheme({
            palette: {
                type: "light",
                primary: {
                    // light: will be calculated from palette.primary.main,
                    main: "#db9e07",
                    dark: "#be5041",
                    contrastText: '#fff',
                },
                secondary: {
                    main: "#be5041"
                }
            },
   
        }),
    dark:
        createMuiTheme({
            palette: {
                type: "dark",
                primary: {
                    light: "#a6d4fa",
                    main: "#90caf9",
                    dark: "#648dae",
                    contrastText: '#fff',
                },
                secondary: {
                    main: "#64b5f6"
                }
        }
    }),
}

export default function ThemeProvider({ children }) {
    const [theme, setTheme] = React.useState(themes.light)
    return(
        <ThemeContext.Provider value={{ theme, setTheme}}>
            <MuiThemeProvider theme={theme}>
                {children}
            </MuiThemeProvider>
        </ThemeContext.Provider>
    )
}