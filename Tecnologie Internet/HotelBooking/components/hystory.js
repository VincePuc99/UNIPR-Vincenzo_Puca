///////////////////////////////////////////////////////import section

import $ from 'jquery'
import * as React from "react"
import { GetData } from "/components/booking.js"
import {isadmin} from "/services/auth"
var CryptoJS= require("crypto-js");

///////////////////////////////////////////////////////var section

var done=false;
var content=[];
var acceptedpreno=[];
var skiparr;
var removed=0;

///////////////////////////////////////////////////////user section

function sendacceptedtodb(acceptedarr) {
  $.ajax("/savesettings.php", ///////ajax post secured
  {
      checkin: acceptedarr[1],
      checkout: acceptedarr[2],
      name: acceptedarr[3],
      surname: acceptedarr[4],
      contatcs: acceptedarr[5],
      adultsnum: acceptedarr[6],
      childnum: acceptedarr[7],
      servicetype: acceptedarr[8],
      notes: acceptedarr[9],
  },
  function(data,status){
      document.getElementById("server").value = data.toString();
      $( "#saveWarningText" ).fadeIn(100);
      setTimeout(function(){ $( "#saveWarningText" ).fadeOut(100); }, 3000);
  });
}

function checkuser(){
  var currentuser=isadmin();
  if(currentuser!="admin"){
    document.getElementById("accept").hidden = true;
    document.getElementById("reject").hidden = true;
    document.getElementById("rejectaccepttext").hidden=true;
    document.getElementById("prenorejacc").hidden=true;}
}
///////////////////////////////////////////////////////display section

function Load(){
  skiparr=0;
  var decryptarr=GetData();
  var bytes1 = CryptoJS.AES.decrypt(decryptarr, 'q@ZPm+9g5oYVRqwF5R$5[/YT~W^7ya');
  content = JSON.parse(bytes1.toString(CryptoJS.enc.Utf8));

  for(var i=1;i<4;i++){
  document.getElementById("num"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("checkin"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("checkout"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("name"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("surname"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("contacts"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("adult"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("child"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("service"+i).innerHTML = content[skiparr];
  skiparr++;
  document.getElementById("note"+i).innerHTML = content[skiparr];
  skiparr++;
  }
}

function shownewcontentonly4code(){
  skiparr=0;
  for(var i=1;i<4;i++){
    document.getElementById("num"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("checkin"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("checkout"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("name"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("surname"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("contacts"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("adult"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("child"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("service"+i).innerHTML = content[skiparr];
    skiparr++;
    document.getElementById("note"+i).innerHTML = content[skiparr];
    skiparr++;
  }
}

///////////////////////////////////////////////////////handle section

function handledelete() {
  done=false;
  var x = document.getElementById("prenodel").value.toString();
  let iddel = parseInt(x);

  if(iddel>=4 || isNaN(iddel) || iddel===0){
    alert("Prenotazione da cancellare non valida");
  }else{
    for(var i=0;i<content.length;i+=10){
      if(content[i]===iddel){
        content[i]="Vuoto";
        content[i+1]="Vuoto";
        content[i+2]="Vuoto";
        content[i+3]="Vuoto";
        content[i+4]="Vuoto";
        content[i+5]="Vuoto";
        content[i+6]="Vuoto";
        content[i+7]="Vuoto";
        content[i+8]="Vuoto";
        content[i+9]="Vuoto";
        done=true;
      }
    }//for

    if(done===true){
      alert("Cancellato con successo");
      shownewcontentonly4code();
      removed++;
    }else{
      alert("Già cancellata o non disonibile");
    } 
  }
 }

 function handleaccept(){
  var accepted=false;
  var x = document.getElementById("prenorejacc").value.toString();
  var idprenoaccept = parseInt(x);
  if(idprenoaccept>=4 || isNaN(idprenoaccept) || idprenoaccept===0){
    alert("Prenotazione da accettare non valida");}else{

      for(var i=0;i<content.length;i+=10){
        if(content[i]===idprenoaccept){
            acceptedpreno.push(content[i]);
            acceptedpreno.push(content[i+1]);
            acceptedpreno.push(content[i+2]);
            acceptedpreno.push(content[i+3]);
            acceptedpreno.push(content[i+4]);
            acceptedpreno.push(content[i+5]); 
            acceptedpreno.push(content[i+6]);
            acceptedpreno.push(content[i+7]);
            acceptedpreno.push(content[i+8]);
            acceptedpreno.push(content[i+9]);
            content[i]="Vuoto";
            content[i+1]="Vuoto";
            content[i+2]="Vuoto";
            content[i+3]="Vuoto";
            content[i+4]="Vuoto";
            content[i+5]="Vuoto";
            content[i+6]="Vuoto";
            content[i+7]="Vuoto";
            content[i+8]="Vuoto";
            content[i+9]="Vuoto";
            accepted=true;}
    }

  }//else

    if(accepted===true){
      shownewcontentonly4code();
      removed++;
      sendacceptedtodb(acceptedpreno);
      acceptedpreno=[];
    }else{
      alert("Già accettata o non disponibile");
    }
 }

 function handlereject(){
   var rejected=false;
  var x = document.getElementById("prenorejacc").value.toString();
  var idprenoreject = parseInt(x);
  if(idprenoreject>=4 || isNaN(idprenoreject) || idprenoreject===0){
    alert("Prenotazione da rifiutare non valida");}else{
        for(var i=0;i<content.length;i+=10){
          if(content[i]===idprenoreject){
              content[i]="Vuoto";
              content[i+1]="Vuoto";
              content[i+2]="Vuoto";
              content[i+3]="Vuoto";
              content[i+4]="Vuoto";
              content[i+5]="Vuoto";
              content[i+6]="Vuoto";
              content[i+7]="Vuoto";
              content[i+8]="Vuoto";
              content[i+9]="Vuoto";
              rejected=true;}//if
      }
    }//else
  
      if(rejected===true){
        alert("Prenotazione rifiutata con successo");
        shownewcontentonly4code();
        removed++;
      }else{
        alert("Già rifiuatata o non disponibile");
      }
    }
 
///////////////////////////////////////////////////////class section

class Hystorypren extends React.Component {

  render(){
    return(
  <>
  <h1 class="h1">Questa è la lista delle tue prenotazioni</h1>

  <table class="content-table">
    <thead>
      <tr>
        <th>N°</th>
        <th>Check-In</th>
        <th>Check-Out</th>
        <th>Nome</th>
        <th>Cognome</th>
        <th>Contatti</th>
        <th>Numero Adulti</th>
        <th>Numero Bambini</th>
        <th>Tipo di servizio</th>
        <th>Note</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td id="num1"> </td>
        <td id="checkin1"> </td>
        <td id="checkout1"> </td>
        <td id="name1"> </td>
        <td id="surname1"> </td>
        <td id="contacts1"> </td>
        <td id="adult1"> </td>
        <td id="child1"> </td>
        <td id="service1"> </td>
        <td id="note1"> </td>
      </tr>
      <tr>
      <td id="num2"> </td>
        <td id="checkin2"> </td>
        <td id="checkout2"> </td>
        <td id="name2"> </td>
        <td id="surname2"> </td>
        <td id="contacts2"> </td>
        <td id="adult2"> </td>
        <td id="child2"> </td>
        <td id="service2"> </td>
        <td id="note2"> </td>
      </tr>
      <tr>
      <td id="num3"> </td>
        <td id="checkin3"> </td>
        <td id="checkout3"> </td>
        <td id="name3"> </td>
        <td id="surname3"> </td>
        <td id="contacts3"> </td>
        <td id="adult3"> </td>
        <td id="child3"> </td>
        <td id="service3"> </td>
        <td id="note3"> </td>
      </tr>
    </tbody>

  </table>

     <button class="form__btn"onClick={Load}>Carica</button> <button class="form__btn"id="checkuser"onClick={checkuser}>User</button> <text id="server"class="text">AutoFunction onLoad</text>  

  <div class ="form__item"> 
    <text class="text">Cancella Prenotazione N°</text> 
       <input claass="form__input"id="prenodel" type="text" class="input" />
       <button class="form__btn"onClick={handledelete}>Cancella Prenotazione</button>
  </div>

  <div class="form__item"> 
      <label class="text"id="rejectaccepttext">Accetta/Rifiuta N°</label> 
       <input class="form__input"id="prenorejacc" type="text" class="input" />
  </div>
  <button class="form__btn"id="accept"onClick={handleaccept}>Accetta Prenotazione</button>  <button class="form__btn"id="reject"onClick={handlereject}>Rifiuta Prenotazione</button>
    <div>
    </div>
</>
    )
}
}
///////////////////////////////////////////////////////export section
export const getdeleted = () => {
return removed;
  }

export const getrefreshdb = () => {
 return content;
  }

export const setremoved = () => {
  removed--;
  }
    
export default Hystorypren