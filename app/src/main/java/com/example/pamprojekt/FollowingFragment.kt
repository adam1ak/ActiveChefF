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


class FollowingFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val db = Firebase.firestore
        val homeRecyclerView: RecyclerView = view.findViewById(R.id.following_recyclerview)

        val posts = mutableListOf<Post>()
        val adapter = PostAdapter(posts)
        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = LinearLayoutManager(this.context)


        val followersRef = db.collection("users").document(auth.currentUser!!.uid)
            .collection("following")
        followersRef.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    db.collection("posts").whereEqualTo("uid", doc.id)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                posts.add(
                                    Post(
                                        document.data["uid"].toString(),
                                        document.data["postId"].toString(),
                                        document.data["username"].toString(),
                                        document.data["description"].toString(),
                                        document.data["image"].toString(),
                                        document.data["likes"].toString().toInt(),
                                        document.data["difficulty"].toString()
                                    )
                                )
                            }
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            Log.d("TAG", "get failed with ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

}