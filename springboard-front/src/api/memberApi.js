import api from "./axios";

// 회원가입
export const signup = (data) => {
  return api.post("/api/members", data);
};

// 로그인
export const login = (data) => {
  return api.post("/api/members/login", data);
};

// 로그아웃
export const logout = () => {

  const refreshToken = localStorage.getItem("refreshToken");

  return api.post("/api/members/logout", {
    refreshToken,
  });
};

// 현재 로그인한 회원 정보 조회
export const getMyInfo = () => {
  return api.get("/api/members/me");
}