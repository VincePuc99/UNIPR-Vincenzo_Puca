///////////////////////////////////////////////////////var section

let arraypplusrnname=["john","admin"];
let arraypplusrnemail=["john@gmail.com","admin@local.com"];
let arraypplusrpass=["pass","1234"];
let arraypplusrirlname=["Johnny","admin"];

///////////////////////////////////////////////////////export section

export const isBrowser = () => typeof window !== "undefined"

var CryptoJS = require("crypto-js");

export const getUser = () => isBrowser() && window.localStorage.getItem("gatsbyUser") 
? JSON.parse(window.localStorage.getItem("gatsbyUser"))
    : {}

const setUser = user => window.localStorage.setItem("gatsbyUser", JSON.stringify(user))//token

export const handleLogin = ({ username, password }) => {

console.log("usn: ",username);//only for presentation
console.log("pass: ",password);

  var bytes = CryptoJS.AES.decrypt(password, 'q@ZPm+9g5oYVRqwF5R$5[/YT~W^7ya');
  var decryptedpass = JSON.parse(bytes.toString(CryptoJS.enc.Utf8));

  var bytes1 = CryptoJS.AES.decrypt(username, 'h3(2g|"p3:Q/L"=`TkOL1XzX9Cp{7I');
  var decripteduser = JSON.parse(bytes1.toString(CryptoJS.enc.Utf8));

  console.log("usndec:",decripteduser);
console.log("passdec:",decryptedpass);

  for (let i = 0; i < arraypplusrnname.length; i++) {
    if (decripteduser === arraypplusrnname[i] && decryptedpass === arraypplusrpass[i]) {
      return setUser({
        username: arraypplusrnname[i],
        name: arraypplusrirlname[i],
        email: arraypplusrnemail[i],
      })
    }

  }
  return false
}

export const isLoggedIn = () => {
  const user = getUser()

  return !!user.username
}

export const isadmin = () => {
  const user = getUser()

  return user.username
}

export const logout = callback => {
  setUser({})
  callback()
}