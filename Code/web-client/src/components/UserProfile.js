import React from "react"

class UserProfile extends React.Component{
    state = {
        username:"",
        name:"",
        email:"",
        id: 0
    }

    componentDidMount(){
        fetch('/users/1')
        .then(response => response.json())
        .then(json => this.setState(
            {
                username: json.username,
                name: json.name,
                email: json.email,
                id: json.id
            }))
    }

    render(){
        return(
            <div>
                <h1>
                    Profile of {this.state.name}
                </h1>
                <ul>
                    <li>username: {this.state.username}</li>
                    <li>email:{this.state.email}</li>
                    <li>id:{this.state.id}</li>
                </ul>
            </div>
        )
    }
}

export default UserProfile