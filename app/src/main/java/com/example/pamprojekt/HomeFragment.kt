package com.example.pamprojekt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.main_fragment, DiscoverFragment()).commit()

        // Initialize Firebase Auth
        auth = Firebase.auth
        val db = Firebase.firestore
        val lg : Button = view.findViewById(R.id.logout_btn)

        val homeTextView : TextView = view.findViewById(R.id.home_textView)
        val followingTextView : TextView = view.findViewById(R.id.following_textView)

        val posts = mutableListOf<Post>()
        val adapter = PostAdapter(posts)

        lg.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
            this.activity?.finish()
        }

        homeTextView.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.main_fragment, DiscoverFragment()).commit()
        }
        followingTextView.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.main_fragment, FollowingFragment()).commit()
        }
    }
}