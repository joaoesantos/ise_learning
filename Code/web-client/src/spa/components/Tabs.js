import React from 'react';
import PropTypes from 'prop-types';
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';

const theme = createMuiTheme({
  palette: {
    primary:{
      main: "#51344d"
    } 
  },
});

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`full-width-tabpanel-${index}`}
      aria-labelledby={`full-width-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          {children}
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    'aria-controls': `full-width-tabpanel-${index}`,
  };
}
  
/**
 * @param {*} props is expecting :
 *  - 'childComponents' an array that represent the components to be rendered in each tab;
 *  - 'tabLabels' an array that represents the label for each tab,
 *  - 'useStyles' a function to be used for styling this component
 */
export default function FullWidthTabs(props) {
  const [value, setValue] = React.useState(0);
  const [state, SetState] = React.useState({
    childComponents: props.childComponents,
    tabLabels: props.tabLabels,
    useStyles: props.useStyles
  });

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleChangeIndex = (index) => {
    setValue(index);
  };

  React.useEffect(() => {
    SetState({
      childComponents: props.childComponents,
      tabLabels: props.tabLabels,
      useStyles: props.useStyles
    });
  },[props]);

  const tabs = state.childComponents.map((comp, idx) => <TabPanel key={idx} value={value} index={idx}>{comp}</TabPanel>)
  const labels = state.tabLabels.map((label, idx) => <Tab key={idx} label={`${label}`} {...a11yProps(idx)} />)
  return (
    <div>
      <ThemeProvider theme={theme}>
        <AppBar position="static" color="default">
          <Tabs
            value={value}
            onChange={handleChange}
            variant="fullWidth"
            indicatorColor='primary'
            textColor = 'primary'
          >
            {labels}
          </Tabs>
        </AppBar>     
        {tabs}
      </ThemeProvider>
    </div>
  );
}
