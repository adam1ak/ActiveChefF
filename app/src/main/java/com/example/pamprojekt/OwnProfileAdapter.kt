package com.example.pamprojekt

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OwnProfileAdapter(var images: List<ProfileImages>) : RecyclerView.Adapter<OwnProfileAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_images, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val postImage : ImageView = holder.itemView.findViewById(R.id.profile_imageView)

        Picasso.get().load(images[position].image).into(postImage)

        postImage.setOnClickListener {
            Log.d("TAG", images[position].postId)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}