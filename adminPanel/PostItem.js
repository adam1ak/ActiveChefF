import './App.css';
import React from 'react';
import { doc, deleteDoc  } from "firebase/firestore";
import { db } from './firebase-cfg';

function PostItem({post}) {

  const removeReported = async () =>{
    await deleteDoc(doc(db, "reportedPosts", `${post.uid}:${post.postId}`));
    window.location.reload();
  };
  const removePost = async () =>{
    await deleteDoc(doc(db, "posts", `${post.postId}`));
    await deleteDoc(doc(db, "reportedPosts", `${post.uid}:${post.postId}`));
    await deleteDoc(doc(db, "savedPosts", `${post.uid}:${post.postId}`));
    window.location.reload();
  }


  return (
    <div className='post'>
        <p>post id <text>{post.postId}</text></p>
        <p>user id <text>{post.uid}</text></p>
        <p>username <text>{post.username}</text></p>
        <p>post description <text>{post.description}</text></p>
        <section>
            <p>post image</p>
            <img src={post.image} alt='post'/>
        </section>
        <section id="buttons">
            <button id="deletePost" onClick={removePost}>Delete Post</button>
            <button id="deleteReport" onClick={removeReported}>Delete Report</button>
        </section>
    </div>
  );
}

export default PostItem;
