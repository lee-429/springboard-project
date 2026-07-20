import axios from "axios";

// Axios 인스턴스 생성
// 모든 API 요청에서 공통으로 사용할 설정을 정의함
const api = axios.create({

  // Spring Boot 서버의 기본 주소 설정
  baseURL: "http://localhost:8080",
});

const refreshApi = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use(
  (config) => {

    // 1. localStorage에서 accessToken 가져오기
    const token = localStorage.getItem("accessToken");

    // 2. 토큰이 있으면 Authorization 헤더 추가
    if (token) {
      config.headers.Authorization = "Bearer " + token;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(

  // 정상 응답
  (response) => {
    return response;
  },

  // 에러 응답
  async (error) => {
    
    const originalRequest = error.config;

    // Access Token 만료(401)이고 재시도 요청이 아닌 경우
    if (
      error.response?.status === 401 &&
      !originalRequest._retry
    ) {

      originalRequest._retry = true;

      try {

        const refreshToken = localStorage.getItem("refreshToken");

        // Refresh Token으로 Access Token 재발급
        const response = await refreshApi.post(
          "/api/members/reissue",
          {
            refreshToken,
          }
        );

        const newAccessToken =
          response.data.accessToken;


        // 새로운 Access Token 저장
        localStorage.setItem(
          "accessToken",
          newAccessToken
        );


        // 기존 요청에 새 토큰 적용
        originalRequest.headers.Authorization =
          "Bearer " + newAccessToken;


        // 실패했던 요청 다시 실행
        return api(originalRequest);


      } catch (refreshError) {

        // Refresh Token도 만료된 경우
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        window.location.href = "/login";

        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
)

// 생성한 Axios 인스턴스를 다른 파일에서 사용할 수 있도록 내보냄
export default api;