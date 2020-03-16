import React from 'react';
import logo from './logo.svg';
import UserProfile from './components/UserProfile'
import Challenges from './components/Challenges'
import Home from './components/Home'
import {
  Route,
  NavLink,
  HashRouter
} from "react-router-dom";
import './App.css';

class App extends React.Component {

  state = {
    data: {}
  }

  render(){
    return (
      <HashRouter>
        <ul className="header">
            <li><NavLink to="/">Home</NavLink></li>
            <li><NavLink to="/profile">Profile</NavLink></li>
            <li><NavLink to="/challenges">Challenges</NavLink></li>
          </ul>
        <div className="content">
            <Route exact path="/" component={Home}/>
            <Route path="/profile" component={UserProfile}/>
            <Route path="/challenges" component={Challenges}/>
        </div>
      </HashRouter>
      
    );
  }
  
}

export default App;
