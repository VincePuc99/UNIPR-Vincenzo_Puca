import React from "react"
import { Router } from "@reach/router"
import Layout from "/components/layout"
import PrivateRoute from "/components/privateroute.js"
import Profile from "/components/profile.js"
import Login from "/components/login.js"
import Booking from "/components/booking.js"
import Historypren from "/components/hystory.js"

const App = () => (
  <Layout>
    <Router>
      <PrivateRoute path="/app/profile" component={Profile} />
      <PrivateRoute path="/app/booking" component={Booking} />
      <PrivateRoute path="/app/hystory" component={Historypren}/>
      <Login path="/app/login" />
    </Router>
  </Layout>
)

export default App