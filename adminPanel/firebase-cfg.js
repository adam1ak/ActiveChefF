// Import the functions you need from the SDKs you need

import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCBraOslmCIINU3IoydnvNP9oQ5f4IawcQ",
  authDomain: "fir-connection-pamfa4tap.firebaseapp.com",
  databaseURL: "https://fir-connection-pamfa4tap-default-rtdb.europe-west1.firebasedatabase.app",
  projectId: "fir-connection-pamfa4tap",
  storageBucket: "fir-connection-pamfa4tap.appspot.com",
  messagingSenderId: "39363287656",
  appId: "1:39363287656:web:d764da234b885fd793f9dd"
};

const app = initializeApp(firebaseConfig);
const db = getFirestore(app);
const auth = getAuth(app);

export { db, auth };
