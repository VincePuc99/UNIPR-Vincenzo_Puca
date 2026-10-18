///////////////////////////////////////////////////////import section

import React from "react"
import {getdeleted,getrefreshdb,setremoved} from "/components/hystory.js"
var CryptoJS= require("crypto-js");

///////////////////////////////////////////////////////var section

let arrA=[];
var id=1;
var din;
var dout;
var adn;
var chn;
var cont;
var dc;

///////////////////////////////////////////////////////class section

class Booking extends React.Component {

    state = {
        checkin: ``,
        checkout: ``,
        name:``,
        surname:``,
        contacts:``,
        adultnumber:``,
        childnumber:``,
        servicetype:``,
        notes:``
      }

      handleSubmit = event => {
        event.preventDefault()
        var deletedfromhistory=getdeleted();
        if(deletedfromhistory!==0){
          setremoved();
          arrA=getrefreshdb();
          id--;
        }
        dc=new Date().toISOString().slice(0, 10);
        din=new Date();
        dout = new Date();
        din=this.state.checkin;
        dout=this.state.checkout;
        adn=parseInt(this.state.adultnumber);
        chn=parseInt(this.state.childnumber);
 
       if(id===4){
         alert("Limite max prenotazioni raggiunto");
       }else{
       if (din>=dout){
         alert("Data check-in/check-out non valida");
       }
       else if(din<dc){
        alert("Data check-in non valida");
       }
       else if(this.state.name==="" || this.state.surname==="" ){
           alert("Campo vuoto non valido");
       }
       else if(!isNaN(this.state.name) || !isNaN(this.state.surname) ){
        alert("Non puoi chiamarti come un numero");
    }
       else if( isNaN(adn) || isNaN(chn))
       {
           alert("Campo numerico non valido");
       }
       else if( adn < 1 || adn >= 5)
       {
           alert("Valore adulti non disponibile Min 1 Max 5");
       }
       else if(chn <0 || chn >= 5)
       {
           alert("Valore bambini non disponibile Min 0 Max 5");
       }
       else if(this.state.servicetype!=="Mezza Pensione" && this.state.servicetype!=="Pensione Completa"&&
       this.state.servicetype!=="All inclusive"){
          alert("Campo servizio non valido");
       }
       else{ 
        if(deletedfromhistory===0)
        {
          arrA.push(id);
          arrA.push(this.state.checkin);
          arrA.push(this.state.checkout);
          arrA.push(this.state.name);
          arrA.push(this.state.surname);
          arrA.push(this.state.contacts);
          arrA.push(this.state.adultnumber);
          arrA.push(this.state.childnumber);
          arrA.push(this.state.servicetype);
          arrA.push(this.state.notes);
          id++;
          alert("Prenotazione effettuata con successo");
        }else{
         for(var i=0;i<30;i+=10){
         if(arrA[i]==="Vuoto" || arrA[i]===undefined){
              if(i===0 || arrA[i]===undefined){
                arrA[i]=1;}
              if(i===10 || arrA[i]===undefined){
                arrA[i]=2;}
              if(i===20 || arrA[i]===undefined){
                arrA[i]=3;}
              arrA[i+1]=this.state.checkin;
              arrA[i+2]=this.state.checkout;
              arrA[i+3]=this.state.name;
              arrA[i+4]=this.state.surname;
              arrA[i+5]=this.state.contacts;
              arrA[i+6]=this.state.adultnumber;
              arrA[i+7]=this.state.childnumber;
              arrA[i+8]=this.state.servicetype;
              arrA[i+9]=this.state.notes;
              id++;
              alert("Prenotazione effettuata con successo");
              break;
              }//if 
         }//for
        }//else submit checked 
       }//else control
       }//else id
       }
 
      handleUpdate = event => {
        this.setState({
            [event.target.name]: event.target.value,
          })
    }

render(){
    return(
<>

    <form class="form" method="post" onSubmit={event => {this.handleSubmit(event) }}>
    <div class="form__title">Form per la  prenotazione</div>

      <p class="form__desc">Compila i campi per effettuare una prenotazione</p>
        <div class="form__item">
            <label class="form__label">Data check-In
            <input type="date" class="form__input form__input--small" name="checkin" onChange={this.handleUpdate}/></label>
        </div>

        <div class="form__item">
            <label class="form__label">Data check-Out
            <input type="date" class="form__input form__input--small" name="checkout" onChange={this.handleUpdate} /></label>
        </div>

        <div class="form__item">
            <label for="name" class="form__label">Nome
            <input type="text" class="form__input " name="name" placeholder="Inserisci Nome" onChange={this.handleUpdate}/></label>
        </div>
        <div class="form__item">
            <label class="form__label">Cognome
            <input type="text" class="form__input" name="surname" placeholder="Inserisci Cognome"onChange={this.handleUpdate} /></label>
        </div>
        <div class="form__item">
            <label class="form__label" >Contatti
            <input type="text" class="form__input" name="contacts" placeholder="Inserisci un contatto" onChange={this.handleUpdate} /></label>
        </div>
        <div class="form__item">
            <label class="form__label" >Numero adulti
            <input type="number" class="form__input form__input--small" name="adultnumber" min="1" max="5"placeholder="Numero Adulti" onChange={this.handleUpdate} /></label>
        </div>
        <div class="form__item">
           <label class="form__label">Numero bambini
           <input type="number" class="form__input form__input--small" name="childnumber" min="0" max="5" placeholder="Numero Bambini"onChange={this.handleUpdate} /></label>
        </div>
        <div class="form__item"> 
          <label class="form__label">Tipo di servizio
          <input type="text" class="form__input" name="servicetype" placeholder="Tipologia del servizio"onChange={this.handleUpdate} /></label>
          <span class="form__span">Mezza Pensione / Pensione Completa / All inclusive</span>
        </div>
        <div class="form__item"> 
        <label class="form__label">Note
        <input type="text" class="form__input" name="notes" placeholder="Note"onChange={this.handleUpdate} /></label>
         </div>
        <div class="form__item">
          <button class="form__btn" type="submit" onClick={this.handleSubmit}>Prenota</button> 
        </div>
    </form>
  </>
    )}//render return
}//class

///////////////////////////////////////////////////////export section

export const GetData = () => {
  var cryptoarr;
  cryptoarr=CryptoJS.AES.encrypt(JSON.stringify(arrA), 'q@ZPm+9g5oYVRqwF5R$5[/YT~W^7ya').toString();
  return cryptoarr;
  }

export default Booking