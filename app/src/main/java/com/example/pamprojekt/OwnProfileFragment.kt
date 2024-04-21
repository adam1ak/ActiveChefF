package com.example.pamprojekt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.Locale


class OwnProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val user = auth.currentUser
        val db = Firebase.firestore
        val ownProfileRecycler: RecyclerView = view.findViewById(R.id.own_profile_recyclerView)
        ownProfileRecycler.layoutManager = GridLayoutManager(this.context, 3)

        val format = NumberFormat.getNumberInstance(Locale.FRANCE)
        val images = mutableListOf<ProfileImages>()
        val adapter = OwnProfileAdapter(images)
        ownProfileRecycler.adapter = adapter

        val username : TextView = view.findViewById(R.id.ownProfile_username)
        val userHash : TextView = view.findViewById(R.id.ownProfile_userHash)
        val userFollowers : TextView = view.findViewById(R.id.ownProfile_followersNum)
        val userPostsNumb : TextView = view.findViewById(R.id.ownProfile_postsNum)
        val userLikesNumb : TextView = view.findViewById(R.id.ownProfile_likesNum)

        val followersRef = db.collection("users").document(user!!.uid)
            .collection("followers")
            followersRef.get()
            .addOnSuccessListener { document ->
                if(document.size() == 0){
                    userFollowers.text = "0"
                }
                val followers = document.size()
                userFollowers.text = format.format(followers).toString()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        val likesRef = db.collection("users").document(user!!.uid)
            .collection("likes")
        likesRef.get()
            .addOnSuccessListener { document ->
                if(document.size() == 0){
                    userLikesNumb.text = "0"
                }
                val likes = document.size()
                userLikesNumb.text = format.format(likes).toString()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        db.collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                username.text = document.data!!["username"].toString()
                userHash.text = "@"+document.data!!["username"].toString()
            }

        db.collection("posts")
            .whereEqualTo("uid", user!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                   images.add(
                       ProfileImages(
                           document.data["uid"].toString(),
                           document.data["postId"].toString(),
                           document.data["image"].toString()
                       )
                   )
                }
                adapter.notifyDataSetChanged()
                if(documents.size() == 0){
                    userPostsNumb.text = "0"
                }
                val followers = documents.size()
                userPostsNumb.text = format.format(followers).toString()

            }
    }

}