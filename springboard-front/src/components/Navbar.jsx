import { useNavigate } from "react-router-dom";
import { logout, getMyInfo } from "../api/memberApi";
import { handleError } from "../api/errorHandler";
import "../styles/Navbar.css";
import { useEffect, useState } from "react";
import Button from "./common/Button";
import toast from "react-hot-toast";


function Navbar({ isLogin, setIsLogin }) {

  const [loginMember, setLoginMember] = useState(null);

  const navigate = useNavigate();

  const handleLogout = () => {

    logout()
      .then(() => {

        console.log("logout click");

        toast.success("로그아웃 완료");

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        setIsLogin(false);

        navigate("/login");

      })
      .catch((error) => {

        handleError(error, navigate);

      })
      .finally(() => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        setIsLogin(false);
        navigate("/login");
      });

  };

  useEffect(() => {

    if (isLogin) {
      getMyInfo()
        .then((response) => {
          setLoginMember(response.data);
        })
        .catch((error) => {
          handleError(error, navigate);
        })
    }

  }, [isLogin]);

    return (
      <nav className="navbar">

        <div className="navbar-left">

          <div
            className="logo"
            onClick={() => navigate("/boards")}
          >
            📝 Board
          </div>

        </div>

        <div className="navbar-right">

          {isLogin ? (

            <>
              <span className="welcome">
                👋 {loginMember?.username}님
              </span>

              <Button
                className="primary" 
                onClick={() => navigate("/boards/create")}
              >
                글쓰기
              </Button>

              <Button
                className="danger" 
                onClick={handleLogout}
              >
                로그아웃
              </Button>
            </>

          ) : (

            <>
              <Button onClick={() => navigate("/login")}>
                로그인
              </Button>

              <Button onClick={() => navigate("/signup")}>
                회원가입
              </Button>
            </>

          )}

        </div>

      </nav>
  );
}


export default Navbar;