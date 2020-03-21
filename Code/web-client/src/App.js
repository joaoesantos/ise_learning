import React from 'react';
import logo from './logo.svg';
import UserProfile from './components/UserProfile'
import Challenges from './components/Challenges'
import Home from './components/Home'
import NoMatch from './components/NoMatch'
import {
  Route,
  NavLink,
  HashRouter,
  Switch
} from "react-router-dom";
import './App.css';

class App extends React.Component {

  state = {
    data: {},
    userId: 1
  }

  render(){
    return (
      <HashRouter>
        <ul className="header">
            <li><NavLink to="/">Home</NavLink></li>
            <li><NavLink to={`/profile/${this.state.userId}`}>Profile</NavLink></li>
            <li><NavLink to="/challenges">Challenges</NavLink></li>
          </ul>
        <div className="content">
          <Switch>
            <Route exact path="/" component={Home}/>
            <Route path="/profile" component={UserProfile}/>
            <Route exact path="/challenges" component={Challenges}/>
            <Route component={NoMatch}/>
          </Switch>
        </div>
      </HashRouter>
      
    );
  }
  
}

export default App;
