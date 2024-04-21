package com.example.pamprojekt

data class FollowingPost(
    val uid : String,
    val postId : String,
    val username : String,
    val desc : String,
    val image : String,
    val likes : Int,
    val difficulty : String)
