import React from "react"
import { Link, navigate } from "gatsby"
import { getUser, isLoggedIn, logout,  } from "/services/auth"

export default function NavBar() {
  let greetingMessage = ""
  if (isLoggedIn()) {
    greetingMessage = `Ciao ${getUser().name}`
  } else {
    greetingMessage = "Non hai fatto l'accesso"
  }

  return (
    <div class="stylenavbar">
      <span>{greetingMessage}</span>
      <nav>
        <Link to="/" >Home</Link> {` `}
        <Link to="/app/profile" >Profilo</Link> {` `}
        {isLoggedIn() ? (
          <a href="/"
            onClick={event => { event.preventDefault() 
            logout(() => navigate(`/app/login`)) }} 
          >
            Logout
          </a>
        ) : null}
      </nav>
    </div>
  )
}