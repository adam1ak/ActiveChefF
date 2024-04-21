package com.example.pamprojekt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SavedFragment : Fragment() {


    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = auth.currentUser
        val homeRecyclerView: RecyclerView = view.findViewById(R.id.saved_fragment_recyclerview)

        val posts = mutableListOf<Post>()
        val adapter = PostAdapter(posts)
        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter.notifyDataSetChanged()



        db.collection("savedPosts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val userId = document.id.split(":")[0]
                    if(userId == user!!.uid)
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
                Log.d("Home", "Error getting documents: ", exception)
            }
    }

}