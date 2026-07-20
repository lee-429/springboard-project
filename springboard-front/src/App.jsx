import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useState } from "react";
import { Toaster } from "react-hot-toast";
import BoardList from "./pages/BoardList";
import BoardDetail from "./pages/BoardDetail";
import BoardCreate from "./pages/BoardCreate";
import BoardUpdate from "./pages/BoardUpdate";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import "./styles/common.css";


function App() {

  const [member, setMember] = useState(null);

  const [isLogin, setIsLogin] = useState(
    !!localStorage.getItem("accessToken")
  );

  return (
    <BrowserRouter>

      <Navbar 
        isLogin={isLogin}
        setIsLogin={setIsLogin}
      />

      <Routes>

        <Route path="/boards" element={<BoardList />} />

        <Route path="/boards/:id" element={<BoardDetail isLogin={isLogin}/>} />

        <Route path="/boards/create" element={
          <ProtectedRoute isLogin={isLogin}>
            <BoardCreate />
          </ProtectedRoute>
        }
        />

        <Route path="/boards/:id/update" element={
          <ProtectedRoute isLogin={isLogin}>
            <BoardUpdate />
          </ProtectedRoute>
        } />

        <Route path="/login" element={
          <Login  
            setIsLogin={setIsLogin}
            setMember={setMember}
            />
          } 
        />

        <Route path="/signup" element={<Signup />} />

      </Routes>

      <Toaster
        position="top-center"
        toastOptions={{
          duration: 2000,
        }}
      />

    </BrowserRouter>
  );

}

export default App;