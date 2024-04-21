package com.example.pamprojekt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DiscoverFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val db = Firebase.firestore
        val homeRecyclerView: RecyclerView = view.findViewById(R.id.home_recyclerview)

        val posts = mutableListOf<Post>()
        val adapter = PostAdapter(posts)
        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = LinearLayoutManager(this.context)


        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["uid"].toString() != auth.currentUser!!.uid)
                        db.collection("posts").document(document.id)
                            .collection("likes")
                            .get()
                            .addOnSuccessListener { result ->
                                posts.add(
                                    Post(
                                        document.data["uid"].toString(),
                                        document.data["postId"].toString(),
                                        document.data["username"].toString(),
                                        document.data["description"].toString(),
                                        document.data["image"].toString(),
                                        result.size(),
                                        document.data["difficulty"].toString()
                                    )
                                )
                                adapter.notifyDataSetChanged()
                            }
                }
            }
    }

}