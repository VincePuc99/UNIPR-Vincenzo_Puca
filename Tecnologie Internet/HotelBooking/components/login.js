///////////////////////////////////////////////////////import section

import React from "react"
import { navigate } from "gatsby"
import { handleLogin, isLoggedIn } from "/services/auth"
var CryptoJS= require("crypto-js");

///////////////////////////////////////////////////////class section

class Login extends React.Component {
  state = {
    username: ``,
    password: ``,
  }

  handleUpdate = event => {
    this.setState({
      [event.target.name]: event.target.value,
    })
  }

  handleSubmit = event => {
    event.preventDefault()
    this.state.password = CryptoJS.AES.encrypt(JSON.stringify(this.state.password), 'q@ZPm+9g5oYVRqwF5R$5[/YT~W^7ya').toString();
    this.state.username = CryptoJS.AES.encrypt(JSON.stringify(this.state.username), 'h3(2g|"p3:Q/L"=`TkOL1XzX9Cp{7I').toString();
    handleLogin(this.state)
  }

  render() {
    if (isLoggedIn()) {
      navigate(`/app/profile`)
    }

    return (
      <>
      <link rel="stylesheet" href="styles/global.css"></link>
      <body  class="body">
        <form class="login-form" action="./"
          method="post"
          onSubmit={event => {
            this.handleSubmit(event)
            navigate(`/app/profile`)
          }}
        >
          <div class="login-form__logo-container">
            <img class="login-form__logo" src={require('/images/hotel2.png').default} alt="Logo"></img>
          </div>
          <div class="login-form__content">
            <div class="login-form__header">Login to your account</div>
            <input class="login-form__input" type="text" name="username" placeholder="Username" onChange={this.handleUpdate} />
            <input class="login-form__input" type="password" name="password" placeholder="Password" onChange={this.handleUpdate} />
            <button class ="login-form__button" type="submit">Login</button>
          </div>
          
        </form>
        </body>
      </>
    )
  }
}

///////////////////////////////////////////////////////export section

export default Login