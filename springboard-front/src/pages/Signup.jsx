import { useState } from "react";
import { signup } from "../api/memberApi";
import { useNavigate } from "react-router-dom";
import { handleError } from "../api/errorHandler";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import toast from "react-hot-toast";
import "../styles/Signup.css";


function Signup() {

  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");

  const navigate = useNavigate();


  const handleSubmit = () => {

    const data = {
      loginId,
      password,
      username
    };


    signup(data)
      .then(() => {

        toast.success("회원가입 완료");

        navigate("/login");

      })
      .catch((error) => {

         handleError(error, navigate);

      });

  };


  return (

    <div className="signup-container">

      <div className="signup-box">

        <h1>회원가입</h1>

        <div>
          <Input
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

        <div>
          <Input
            placeholder="닉네임"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <Button onClick={handleSubmit}>
          회원가입
        </Button>

      </div>

    </div>

  );

}

export default Signup;