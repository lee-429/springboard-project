import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { deleteBoard, getBoard } from "../api/boardApi";
import { getMyInfo } from "../api/memberApi";
import { getComments, createComment, deleteComment, updateComment } from "../api/commentApi";
import { handleError } from "../api/errorHandler";
import Button from "../components/common/Button";
import TextArea from "../components/common/TextArea";
import EmptyState from "../components/common/EmptyState";
import toast from "react-hot-toast";
import Modal from "../components/common/Modal";
import "../styles/BoardDetail.css";


function BoardDetail({ isLogin }) {

  // URL의 게시글 id 가져오기
  const { id } = useParams();

  // 게시글 데이터 저장
  const [board, setBoard] = useState(null);

  // 댓글 목록 저장
  const [comments, setComments] = useState([]);

  // 회원 저장
  const [loginMember, setLoginMember] = useState(null);

  // 댓글 입력값 저장
  const [commentContent, setCommentContent] = useState("");

  const [editCommentId, setEditCommentId] = useState(null);
  const [editContent, setEditContent] = useState("");

  const [openDeleteModal, setOpenDeleteModal] = useState(false);


  const [openCommentDeleteModal, setOpenCommentDeleteModal] = useState(false);
  const [selectedCommentId, setSelectedCommentId] = useState(null);

 

  const navigate = useNavigate();

  useEffect(() => {

    getBoard(id)
      .then((response) => {

        setBoard(response.data);
      })
      .catch((error) => {
        handleError(error, navigate);
      });

    getComments(id)
      .then((response) => {
        setComments(response.data);
      })
      .catch((error) => {
        handleError(error, navigate);
      });

    if (isLogin) {
      getMyInfo()
        .then((response) => {
          setLoginMember(response.data);
        })
        .catch((error) => {
          handleError(error, navigate);
        })
    }

  }, [id, isLogin]);

  if (board === null) {
    return <div>게시글 불러오는 중...⌛</div>
  }

  // 삭제 버튼
  const handleDelete = () => {

    deleteBoard(board.id)
      .then(() => {
        toast.success("게시글이 삭제되었습니다.");
        navigate("/boards");
      })
      .catch((error) => {
        handleError(error, navigate);
      });

  };

  // 댓글 작성
  const handleCommentSubmit = () => {

    const data = {
      content: commentContent
    };

    createComment(id, data)
      .then((response) => {

        // 작성한 댓글 목록에 추가
        setComments([
          ...comments,
          response.data
        ]);

        // 입력창 초기화
        setCommentContent("");

        toast.success("댓글이 등록되었습니다.");
      })
      .catch((error) => {
        handleError(error, navigate);
      });
  };

  // 댓글 삭제
  const handleCommentDelete = (commentId) => {

    deleteComment(id, commentId)
      .then(() => {

        setComments(
          comments.filter(
            (comment) => comment.id !== commentId
          )
        );

        toast.success("댓글이 삭제되었습니다.");
      })
      .catch((error) => {
        handleError(error, navigate);
      });
  };

  // 댓글 수정
  const handleCommentUpdate = (commentId) => {

    const data = {
      content: editContent
    };

    updateComment(id, commentId, data)
      .then((response) => {

        // 수정된 댓글로 교체
        setComments(
          comments.map((comment) =>
            comment.id === commentId
              ? response.data
              : comment
          )
        );

        // 수정 모드 종료
        setEditCommentId(null);
        setEditContent("");

        toast.success("댓글이 수정되었습니다.");
      })
      .catch((error) => {
        handleError(error, navigate);
      });
  };

  return (
    <div className="detail-container">

      <div className="board-box">

        <h1>{board.title}</h1>

        <p className="writer">
          작성자 : {board.writer}
        </p>

        <hr />

        <p className="content">
          {board.content}
        </p>

        {board.originalFileName && (
          <div className="file-box">

            <p>첨부파일 : {board.originalFileName}</p>

            <a
              href={`http://localhost:8080/uploads/${board.storedFileName}`}
              target="_blank"
              rel="noreferrer"
            >
              다운로드
            </a>

          </div>
        )}

        <div className="button-group">

          <Button onClick={() => navigate("/boards")}>
            목록으로
          </Button>

          {isLogin && loginMember?.username === board.writer && (
            <>
              <Button onClick={() => navigate(`/boards/${board.id}/update`)}>
                수정
              </Button>

              <Button
                className="danger" 
                onClick={() => setOpenDeleteModal(true)}>
                삭제
              </Button>
            </>
          )}

        </div>

      </div>

      <h2>댓글</h2>

      {isLogin ? (

        <div className="comment-write">

          <TextArea
            value={commentContent}
            onChange={(e) => setCommentContent(e.target.value)}
            placeholder="댓글 입력"
          />

          <Button onClick={handleCommentSubmit}>
            댓글 작성
          </Button>

        </div>

      ) : (

        <p>
          로그인 후 댓글 작성이 가능합니다.
        </p>

      )}

      <div>

        {comments.length === 0 ? (

          <EmptyState
            icon="💬"
            title="댓글이 없습니다."
            description="첫 댓글을 작성해보세요."
          />

        ) : (

          comments.map((comment) => (

            <div
              key={comment.id}
              className="comment-box"
            >

              <p className="comment-writer">
                작성자 : {comment.writer}
              </p>

              {editCommentId === comment.id ? (

                <div>

                  <TextArea
                    value={editContent}
                    onChange={(e) => setEditContent(e.target.value)}
                  />

                  <Button onClick={() => handleCommentUpdate(comment.id)}>
                    저장
                  </Button>

                </div>

              ) : (

                <p className="comment-content">
                  {comment.content}
                </p>

              )}

              {isLogin && loginMember?.username === comment.writer && (

                <div className="comment-buttons">

                  <Button
                    onClick={() => {
                      setEditCommentId(comment.id);
                      setEditContent(comment.content);
                    }}
                  >
                    수정
                  </Button>

                  <Button
                    className="danger"
                    onClick={() => {
                      setSelectedCommentId(comment.id);
                      setOpenCommentDeleteModal(true);
                    }}
                  >
                    삭제
                  </Button>

                </div>

              )}

            </div>

          ))

        )}

      </div>

        <Modal
          open={openDeleteModal}
          title="게시글 삭제"
          message="정말 게시글을 삭제하시겠습니까?"
          onCancel={() => setOpenDeleteModal(false)}
          onConfirm={() => {
            setOpenDeleteModal(false);
            handleDelete();
          }}
        />

    <Modal
        open={openCommentDeleteModal}
        title="댓글 삭제"
        message="정말 댓글을 삭제하시겠습니까?"
        onCancel={() => setOpenCommentDeleteModal(false)}
        onConfirm={() => {
          setOpenCommentDeleteModal(false);

          if (selectedCommentId !== null) {
            handleCommentDelete(selectedCommentId);
          }
        }}
      />

    </div>
  );
}

export default BoardDetail;