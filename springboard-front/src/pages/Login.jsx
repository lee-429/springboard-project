import { useState } from "react";
import { getMyInfo, login } from "../api/memberApi";
import { useNavigate } from "react-router-dom";
import { handleError } from "../api/errorHandler";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import toast from "react-hot-toast";
import "../styles/Login.css";

function Login({ setIsLogin, setMember }) {

  // 입력값 저장
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");

  // 페이지 이동
  const navigate = useNavigate();


  // 로그인 버튼
  const handleSubmit = async () => {

    try {
      const response = await login({
        loginId,
        password,
      });

      const { accessToken, refreshToken } = response.data;

      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      const memberResponse = await getMyInfo();

      setMember(memberResponse.data);
      setIsLogin(true);

      toast.success("로그인 완료");

      navigate("/boards");

    } catch (error) {
      console.log(error);
      alert("로그인 실패");
    }
  };


  return (
    <div className="login-container">

      <div className="login-box">

        <h1>로그인</h1>

        <div>
          <Input
            type="text"
            placeholder="아이디"
            value={loginId}
            onChange={(e) => setLoginId(e.target.value)}
          />
        </div>

        <div>
          <Input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <Button onClick={handleSubmit}>
          로그인
        </Button>

      </div>

    </div>
  );
}

export default Login;