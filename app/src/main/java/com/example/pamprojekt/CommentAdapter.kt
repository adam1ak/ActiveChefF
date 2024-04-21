package com.example.pamprojekt

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CommentAdapter(var comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val commentContent : TextView = holder.itemView.findViewById(R.id.comment_content)
        val commentUsername : TextView = holder.itemView.findViewById(R.id.comment_username)

        commentContent.text = comments[position].comment
        commentUsername.text = comments[position].username


    }

    override fun getItemCount(): Int {
        return comments.size
    }
}