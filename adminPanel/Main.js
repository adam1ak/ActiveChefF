import './Reset.css';
import './Main.css';
import PostList from './PostList';
import React, { useEffect, useState } from 'react';
import { collection, getDocs } from "firebase/firestore";
import { getAuth } from "firebase/auth";
import { useNavigate } from "react-router-dom";
import { db } from './firebase-cfg';
import './reported.css';


function Main() {
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);

  const auth = getAuth();
  const user = auth.currentUser;
  
  useEffect(() => {
    fetchData();
  }, [navigate, user]);

  const fetchData = async () => {
    let newList = []
    const querySnapshot = await getDocs(collection(db, "reportedPosts"));
        querySnapshot.forEach((doc) => {
        const post = {
           postId: doc.data().postId,
            uid: doc.data().uid,
            username: doc.data().username,
            description: doc.data().description,
            image: doc.data().image
        }
        newList.push(post)
        console.log(post)
    });
    console.log("hiii")
    setPosts(newList);
    }

  return (
    <div className='main'>
      <PostList posts={posts}/>
    </div>
  );
}

export default Main;
