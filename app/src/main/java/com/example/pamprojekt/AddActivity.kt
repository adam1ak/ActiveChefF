package com.example.pamprojekt

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class AddActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val next : TextView = findViewById(R.id.nextTextView)
        val choose_btn : Button = findViewById(R.id.choose_btn)
        val back_btn : ImageView = findViewById(R.id.back_add)
        imageView = findViewById(R.id.image_preview)

        back_btn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        choose_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }

        next.setOnClickListener{
            val intent = Intent(baseContext, SecAddActivity::class.java)
            intent.putExtra("imageUri", imageUri.toString())
            startActivity(intent)
        }
    }
}