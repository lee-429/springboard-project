import api from "./axios";

// 댓글 조회
export const getComments = (boardId) => {
  return api.get(`/api/boards/${boardId}/comments`);
};

// 댓글 작성
export const createComment = (boardId, data) => {
  return api.post(`/api/boards/${boardId}/comments`, data);
};

// 댓글 수정
export const updateComment = (boardId, commentId, data) => {
  return api.put(`/api/boards/${boardId}/comments/${commentId}`, data);
};

// 댓글 삭제
export const deleteComment = (boardId, commentId) => {
  return api.delete(`/api/boards/${boardId}/comments/${commentId}`);
};

