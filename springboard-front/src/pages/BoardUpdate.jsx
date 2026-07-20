import { useEffect, useState } from "react";
import { updateBoard, getBoard } from "../api/boardApi";
import { useNavigate, useParams } from "react-router-dom";
import { handleError } from "../api/errorHandler";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import TextArea from "../components/common/TextArea";
import toast from "react-hot-toast";
import "../styles/BoardUpdate.css";

function BoardUpdate() {

  const [board, setBoard] = useState(null);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const [file, setFile] = useState(null);

  const [deleteFile, setDeleteFile] = useState(false);

  const navigate = useNavigate();

  const { id } = useParams();

  // 기존 게시글 조회
  useEffect(() => {

    getBoard(id)
      .then((response) => {
        
        setBoard(response.data);

        setTitle(response.data.title);
        setContent(response.data.content);
      })
      .catch((error) => {
        handleError(error, navigate);
      });
  }, [id]);

  // 수정 버튼
  const handleSubmit = () => {

    const formData = new FormData();

    formData.append("title", title);
    formData.append("content", content);

    // 새 파일 선택했을 경우
    if (file) {
      formData.append("file", file);
    }

    formData.append(
      "deleteFile",
      deleteFile
    );

    updateBoard(id, formData)
      .then(() => {
        
        toast.success("게시글이 수정되었습니다.");
        
        // 수정 완료 후 상세 페이지 이동
        navigate(`/boards/${id}`);
      })
      .catch((error) => {
        handleError(error, navigate);
      })
  }

    return (
    <div className="update-container">

      <div className="update-box">

        <h1>게시글 수정</h1>


        <div className="input-group">
          <Input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>


        <div className="input-group">
          <TextArea
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
        </div>


        {/* 새 파일 선택 */}
        <div className="file-group">

          <input
            type="file"
            onChange={(e) => setFile(e.target.files[0])}
          />

        </div>


        {/* 기존 파일 표시 및 삭제 */}
        {
          board &&
          board.storedFileName && (

            <div className="current-file">

              <p>
                현재 파일 : {board.originalFileName}
              </p>


              <label>

                <input
                  type="checkbox"
                  checked={deleteFile}
                  onChange={(e) => setDeleteFile(e.target.checked)}
                />

                기존 파일 삭제

              </label>

            </div>

          )
        }


        <Button onClick={handleSubmit}>
          수정
        </Button>


      </div>

    </div>
  )
}

export default BoardUpdate;