import './Reset.css';
import './App.css';
import {signInWithEmailAndPassword } from "firebase/auth";
import { auth } from './firebase-cfg.js';
import { useNavigate } from "react-router-dom";

function Login() {
const navigate = useNavigate();
const logIn = () => {
    const email = document.getElementById('emailEnter').value;
    const password = document.getElementById('passEnter').value;
    
    if(email === '' || password === ''){
        alert('Please fill in all fields');
        return;
    }
    signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
        const user = userCredential.user;
        console.log(user);
        navigate('/logged');
    })
    .catch((error) => {
        console.log(error);
    });
};


  return (
    <div className='login'>
        <section>
            <input type="text" placeholder="Enter email" id="emailEnter"/>
            <input type="password" placeholder="Enter passowrd" id="passEnter"/>
            <button id="loginBtn" onClick={logIn}>Log in</button>
        </section>
    </div>
  );
}

export default Login;
