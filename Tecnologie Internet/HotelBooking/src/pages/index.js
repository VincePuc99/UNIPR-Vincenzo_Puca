import React from "react"
import { Link } from "gatsby"
import { getUser, isLoggedIn } from "/services/auth.js"

import Layout from "/components/layout.js"

export default function Home() {
  return (
    <Layout>
      <body class=".body">

        <p class="p">
          <div class="login-formlogo-container">
            <h1 class="h1">Benvenuto nel nostro sistema di prenotazione {isLoggedIn() ? getUser().name : ""}!</h1>
            <img class="img2" src={require('/images/hotel2.png').default} alt="Logo" ></img><br></br>
            <label class="label">Per eseguire qualsiasi operazione devi eseguire l'accesso</label>
            <button class="formbtn"><Link to="/app/login">Accedi</Link></button><br></br>
            <label class="label">Se hai gia fatto il login controlla qui il tuo profilo{" "}</label>
            <button class="form__btn"><Link to="/app/profile">Profilo</Link></button>
          </div>
          {isLoggedIn() ? (
           <>
           </>
         ) : (
           <>

           </>
         )}
        </p>
      </body>
    </Layout>
  )
}