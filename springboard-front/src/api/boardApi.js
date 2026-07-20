import api from "./axios";

// 게시글 목록 조회
export const getBoards = (page = 0) => {
  return api.get(`/api/boards?page=${page}`);
};

// 게시글 상세 조회
export const getBoard = (id) => {
  return api.get(`/api/boards/${id}`);
};

// 게시글 작성
export const createBoard = (formData) => {
  return api.post("/api/boards", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

// 게시글 수정
export const updateBoard = (id, formData) => {
  return api.put(
    `/api/boards/${id}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
};

// 게시글 삭제
export const deleteBoard = (id) => {
  return api.delete(`/api/boards/${id}`);
};