package com.example.pamprojekt

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var password: String
    private lateinit var email: String
    private lateinit var auth: FirebaseAuth

    private fun showToast(message: String) {
        Toast.makeText(
            baseContext,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithEmail:success")
                    showToast("Welcome! ${auth.currentUser!!.email}")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

                } else {
                    Log.w("LoginErr", "signInWithEmail:failure", task.exception)
                    showToast("Authentication failed.")
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("Not logged")
        }
        else {
            showToast("Logged")
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Initialize Firebase Auth
        auth = Firebase.auth

        val register_btn : Button = findViewById(R.id.create_acc_btn)
        val login_btn : Button = findViewById(R.id.login_btn)
        val show_password : Button = findViewById(R.id.show_password_btn)
        val password_editText : EditText = findViewById(R.id.password_edit_text)
        val email_editText : EditText = findViewById(R.id.email_edit_text)


        show_password.setOnClickListener {
            if (password_editText.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
                // Show Password
                password_editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Hide Password
                password_editText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Ensure the cursor position is maintained at the end of the text
            password_editText.setSelection(password_editText.text.length)
        }
        register_btn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        login_btn.setOnClickListener {
            when{
                password_editText.text.isEmpty() -> showToast("Please enter a password")
                email_editText.text.isEmpty() -> showToast("Please enter an email")
                else -> {
                    password = password_editText.text.toString()
                    email = email_editText.text.toString()
                    loginUser()
                }
            }
        }
    }
}