export const handleError = (error, navigate, loginError = false) => {

  if (!error.response) {
    alert("서버와 연결할 수 없습니다.");
    return;
  }


  const status = error.response.status;


  if (status === 401) {

    if (loginError) {

      alert(
        error.response.data.message ||
        "아이디 또는 비밀번호가 올바르지 않습니다."
      );

      return;
    }


    alert("로그인이 필요합니다.");

    localStorage.removeItem("accessToken");

    navigate("/login");

  }


  else if (status === 403) {

    alert(
      error.response.data.message ||
      "권한이 없습니다."
    );

  }


  else if (status === 404) {

    alert(
      error.response.data.message ||
      "요청한 데이터를 찾을 수 없습니다."
    );

    navigate("/boards");

  }


  else {

    alert(
      error.response.data.message ||
      "오류가 발생했습니다."
    );

  }

};