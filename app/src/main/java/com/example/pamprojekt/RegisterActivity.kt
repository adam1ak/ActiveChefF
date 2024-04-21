package com.example.pamprojekt

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private fun register(){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "createUserWithEmail:success")
                    val user = auth.currentUser

                    showToast("Welcome! ${user!!.email}")

                    val newUser = hashMapOf(
                        "username" to username!!,
                        "email" to user!!.email
                    )

                    db.collection("users").document(user!!.uid)
                        .set(newUser)
                        .addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot successfully written!")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "createUserWithEmail:failure", task.exception)
                    showToast("Authentication failed.")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            baseContext,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("RegisterActivity", "User is already signed in")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = Firebase.firestore

        val backArrow : ImageView = findViewById(R.id.backImageView)
        val usernameEditText : EditText = findViewById(R.id.username_edit_text_reg)
        val passwordEditText : EditText = findViewById(R.id.password_edit_text_reg)
        val emailEditText : EditText = findViewById(R.id.email_edit_text_reg)
        val getStarted : Button = findViewById(R.id.getStarted_btn)
        val showPass : Button = findViewById(R.id.show_password_btn)

        getStarted.setOnClickListener{
            when{
                usernameEditText.text.isEmpty() -> showToast("Please enter a username")
                emailEditText.text.isEmpty() -> showToast("Please enter an email")
                passwordEditText.text.isEmpty() -> showToast("Please enter a password")
                passwordEditText.text.length !in 8..16 -> showToast("Password must be between 8 and 16 characters")
                else -> {
                    username = usernameEditText.text.toString()
                    password = passwordEditText.text.toString()
                    email = emailEditText.text.toString()
                    Log.d("RegisterActivity", "Username: $username, Email: $email, Password: $password")
                    register()
                }
            }
        }
        backArrow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        showPass.setOnClickListener {
            if (passwordEditText.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                // Show Password
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Hide Password
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Ensure the cursor position is maintained at the end of the text
            passwordEditText.setSelection(passwordEditText.text.length)
        }

    }
}