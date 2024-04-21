package com.example.pamprojekt

import java.text.NumberFormat
import java.util.Locale
import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PostAdapter(var posts: List<Post>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>()  {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val db = Firebase.firestore
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post_username: TextView = holder.itemView.findViewById(R.id.username_post)
        val post_image: ImageView = holder.itemView.findViewById(R.id.post_image)
        val post_likes : TextView = holder.itemView.findViewById(R.id.post_likes)
        val post_desc: TextView = holder.itemView.findViewById(R.id.username_desc_post)
        val post_follow_btn : Button = holder.itemView.findViewById(R.id.follow_post_btn)
        val post_like_btn : ImageView = holder.itemView.findViewById(R.id.post_like_btn)
        val opitons_btn : ImageView = holder.itemView.findViewById(R.id.opitons_img)
        val save_post_btn : ImageView = holder.itemView.findViewById(R.id.save_post_btn)
        val post_comment_btn : ImageView = holder.itemView.findViewById(R.id.post_comment_btn)
        val format = NumberFormat.getNumberInstance(Locale.FRANCE)

        post_username.text = posts[position].username
        post_desc.text = posts[position].desc
        post_likes.text = format.format(posts[position].likes) + " likes"
        Picasso.get().load(posts[position].image).into(post_image)

        val view : View = View.inflate(holder.itemView.context, R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(holder.itemView.context)

        val comments = mutableListOf<Comment>()
        view.findViewById<RecyclerView>(R.id.comment_recycler).adapter = CommentAdapter(comments)
        view.findViewById<RecyclerView>(R.id.comment_recycler).layoutManager = LinearLayoutManager(holder.itemView.context)

        post_comment_btn.setOnClickListener {
            dialog.setContentView(view)
            dialog.show()
            comments.clear()
            db.collection("posts").document(posts[position].postId).collection("comments")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        comments.add(
                            Comment(
                                document.data["comment"].toString(),
                                document.data["username"].toString(),
                                document.data["uid"].toString()
                            )
                        )
                    }
                    Log.d("TAG", "Comments: $comments")
                    view.findViewById<RecyclerView>(R.id.comment_recycler).adapter?.notifyDataSetChanged()
                }
        }

        val send : ImageView = view.findViewById(R.id.sendComment_btn)
        val commentContent : EditText = view.findViewById(R.id.comment_editText)



        val commentsRef = db.collection("posts").document(posts[position].postId)
            .collection("comments")

        send.setOnClickListener {
            if(commentContent.text.toString().isNotEmpty()) {
                db.collection("posts").document(posts[position].postId)
                    .collection("comments")
                commentsRef.get()
                    .addOnSuccessListener {
                        val newComment = hashMapOf(
                            "uid" to user!!.uid,
                            "username" to "need to change it",
                            "comment" to commentContent.text.toString()
                        )
                        db.collection("posts").document(posts[position].postId)
                            .collection("comments")
                            .add(newComment)
                            .addOnSuccessListener {
                                commentContent.text.clear()
                                dialog.hide()
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "get failed with ", exception)
                    }
            }else{
                commentContent.error = "Comment can't be empty"
            }
        }

        val reportDocRef = db.collection("reportedPosts")
            .document(user!!.uid + ":" + posts[position].postId)
        reportDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("TAG", "Document Exists")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        opitons_btn.setOnClickListener {
            val popUpMenu = PopupMenu(holder.itemView.context, opitons_btn)
            popUpMenu.inflate(R.menu.popup_items)

            popUpMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.reportOption -> {
                        db.collection("reportedPosts").document(user!!.uid + ":" + posts[position].postId)
                        reportDocRef.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    Log.d("TAG", "Document Exists")
                                } else {
                                    val newReportedPost = hashMapOf(
                                        "uid" to posts[position].uid,
                                        "postId" to posts[position].postId,
                                        "username" to posts[position].username,
                                        "description" to posts[position].desc,
                                        "image" to posts[position].image
                                    )
                                    db.collection("reportedPosts").document(user!!.uid + ":" + posts[position].postId)
                                        .set(newReportedPost)
                                }
                            }
                        true
                    }else ->{false}
                }
            }
            popUpMenu.show()
        }


        val likesRef = db.collection("posts").document(posts[position].postId)
            .collection("likes").document(user!!.uid)
        likesRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    post_like_btn.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        post_like_btn.setOnClickListener {
            db.collection("posts").document(posts[position].postId)
                .collection("likes").document(user!!.uid)
            likesRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("posts").document(posts[position].postId)
                            .collection("likes").document(user!!.uid)
                            .delete()
                            .addOnSuccessListener {
                                post_like_btn.setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                        db.collection("users").document(posts[position].uid)
                            .collection("likes").document(user!!.uid)
                            .delete()
                    } else {
                        val newLike = hashMapOf(
                            user!!.uid to true
                        )
                        db.collection("posts").document(posts[position].postId)
                            .collection("likes").document(user!!.uid)
                            .set(newLike)
                            .addOnSuccessListener {
                                post_follow_btn.text = "Following"
                            }
                            .addOnFailureListener { e -> Log.w("Saved post", "Error writing document", e) }
                        db.collection("users").document(posts[position].uid)
                            .collection("likes").document(user!!.uid)
                            .set(newLike)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }


        }

        val followersRef = db.collection("users").document(posts[position].uid)
            .collection("followers").document(user!!.uid)
        followersRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    post_follow_btn.text = "Following"
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        post_follow_btn.setOnClickListener {
            db.collection("users").document(posts[position].uid)
                .collection("followers").document(user!!.uid)
            followersRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("users").document(posts[position].uid)
                            .collection("followers").document(user!!.uid)
                            .delete()
                            .addOnSuccessListener {
                                post_follow_btn.text = "Follow"
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                        db.collection("users").document(user!!.uid)
                            .collection("following").document(posts[position].uid)
                            .delete()
                    } else {
                        val newSavedPost = hashMapOf(
                            user!!.uid to true
                        )
                        db.collection("users").document(posts[position].uid)
                            .collection("followers").document(user!!.uid)
                            .set(newSavedPost)
                            .addOnSuccessListener {
                                post_follow_btn.text = "Following"
                            }
                            .addOnFailureListener { e -> Log.w("Saved post", "Error writing document", e) }
                        db.collection("users").document(user!!.uid)
                            .collection("following").document(posts[position].uid)
                            .set(newSavedPost)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }


        }

        val docRef = db.collection("savedPosts")
            .document(user!!.uid + ":" + posts[position].postId)
            docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    save_post_btn.setImageResource(R.drawable.baseline_bookmark_24_post)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        save_post_btn.setOnClickListener {
            db.collection("savedPosts").document(user!!.uid + ":" + posts[position].postId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("savedPosts").document(user!!.uid + ":" + posts[position].postId)
                            .delete()
                            .addOnSuccessListener {
                                save_post_btn.setImageResource(R.drawable.baseline_bookmark_border_24)
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                    } else {
                        val newSavedPost = hashMapOf(
                            "uid" to posts[position].uid,
                            "postId" to posts[position].postId,
                            "username" to posts[position].username,
                            "description" to posts[position].desc,
                            "difficulty" to posts[position].difficulty,
                            "likes" to posts[position].likes,
                            "image" to posts[position].image
                        )
                        db.collection("savedPosts").document(user!!.uid + ":" + posts[position].postId)
                            .set(newSavedPost)
                            .addOnSuccessListener {
                                save_post_btn.setImageResource(R.drawable.baseline_bookmark_24_post)
                            }
                            .addOnFailureListener { e -> Log.w("Saved post", "Error writing document", e) }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }


        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}