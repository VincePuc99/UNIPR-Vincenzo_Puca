import React from "react"
import { Link } from "gatsby"

const Profile = () => {
  return(
  <>
     <h1>Benvenuto nel tuo Profilo</h1>
     <div class="form__item">
      <img class="img" src={require('/images/profile.png').default} alt="Logo"></img>
      <button class="form__btn" type="submit"><Link to="/app/booking">Prenotazioni</Link></button>
      <button class="form__btn" type="submit"><Link to="/app/hystory">Lista</Link></button>
     </div>
  </>
  )
}
export default Profile
