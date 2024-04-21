package com.example.pamprojekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SecAddActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var diff: String
    private var clicked: Boolean = false

    private fun showToast(message: String) {
        Toast.makeText(
            baseContext,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec_add)

        val desc : EditText = findViewById(R.id.descriptionSecAdd)
        val btn_create : Button = findViewById(R.id.create_post_btn)
        val recivedUri = intent.getStringExtra("imageUri")
        val imageView : ImageView = findViewById(R.id.previewImage)
        val back_sec_add : ImageView = findViewById(R.id.back_sec_add)
        imageView.setImageURI(recivedUri!!.toUri())

        val db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser

        back_sec_add.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonIdToDifficulty = mapOf(
            R.id.btn_begg to "Beginner",
            R.id.btn_novice to "Novice",
            R.id.btn_inter to "Intermediate",
            R.id.btn_adv to "Advanced",
            R.id.btn_exp to "Expert"
        )


        // Set a single click listener for all buttons
        buttonIdToDifficulty.keys.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                diff = buttonIdToDifficulty[buttonId] ?: ""
                clicked = true
            }
        }
        btn_create.setOnClickListener {
            when{
                clicked == false -> showToast("Select difficulty")
                desc.text.isEmpty() -> showToast("Type some instructions")
                recivedUri == null -> showToast("Select image")
                else -> {
                    val storageReference = Firebase.storage.reference
                    val imageRef = storageReference.child("postsImages/${user!!.uid}")
                    val uploadTask = imageRef.putFile(recivedUri.toUri())

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            db.collection("users")
                                .document(user!!.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val post = hashMapOf(
                                            "uid" to user!!.uid,
                                            "postId" to "",
                                            "username" to document.data!!["username"].toString(),
                                            "description" to desc.text.toString(),
                                            "difficulty" to diff,
                                            "likes" to 0,
                                            "image" to downloadUrl
                                        )
                                        db.collection("posts")
                                            .add(post)
                                            .addOnSuccessListener { documentReference ->
                                                db.collection("posts")
                                                    .document(documentReference.id)
                                                    .update("postId", documentReference.id)
                                                    .addOnSuccessListener {
                                                        showToast("Post added")
                                                        post["postId"] = documentReference.id
                                                        Log.d("PostId", documentReference.id)
                                                        Log.d("List PostId", post["postId"].toString())
                                                        val intent = Intent(this, MainActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                            }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}