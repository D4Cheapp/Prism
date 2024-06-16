const apiUrl = "https://95aa-85-140-112-133.ngrok-free.app/prism/v1";

/*function registration(event) {
  event.preventDefault()
  fetch(apiUrl+"/registration", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    body: JSON.stringify({
    login: 'login',
    password: 'password',
    confirmPassword: 'password',
    isAdmin: false
  })
  })
 
}*/
function verify(e){
  e.preventDefault()
  fetch(apiUrl+"/registration"),{
    method: 'POST',
    headers: {
      'Content-Type' : 'application/json;charset=UTF-8'
    },
    body: JSON.stringify({
      
    })
  }
}
const pass = document.getElementById('pass');
const confirm_pass = document.getElementById('confirmPass');

function showPassword(){
   password = document.getElementById("pass");
  console.log(password);
  if (password.type === "password"){
    password.type = "text";
  } else{
    password.type = "password";
  }
}

function validate_password() {
 

  if (pass.value != confirm_pass.value) {
      document.getElementById('SIB').disabled = true;
      document.getElementById('SIB').style.opacity = (0.4);
  } else {
      document.getElementById('SIB').disabled = false;
      document.getElementById('SIB').style.opacity = (1);
  }
}


