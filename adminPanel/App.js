import './Reset.css';
import './App.css';
import Login from './Login';
import Main from './Main';
import { BrowserRouter, Routes, Route } from 'react-router-dom';



function App() {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path='/' exact element={<Login/>}/>
          <Route path='/logged' element={<Main/>}/>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
