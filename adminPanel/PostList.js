import './App.css';
import PostItem from './PostItem.js';

function PostList({posts}) {
  return (
    <div className='posts' >
      {
        posts.map((post, key) => (
          <PostItem key={key} post={post}/>
        ))
      }
    </div>
  );
}

export default PostList;
