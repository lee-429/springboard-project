import { useState } from "react";
import { createBoard } from "../api/boardApi";
import { useNavigate } from "react-router-dom";
import { handleError } from "../api/errorHandler";
import Button from "../components/common/Button";
import Input from "../components/common/Input";
import TextArea from "../components/common/TextArea";
import toast from "react-hot-toast";
import "../styles/BoardCreate.css";

function BoardCreate() {

  // 입력값 저장
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  // 파일 저장
  const [file, setFile] = useState(null);

  // 페이지 이동
  const navigate = useNavigate();


  // 작성 버튼
  const handleSubmit = () => {

    const formData = new FormData();

    formData.append("title", title);
    formData.append("content", content);

    // 파일이 있을 때만 추가
    if (file) {
      formData.append("file", file);
    }


    createBoard(formData)
      .then(() => {
        
        toast.success("게시글이 작성되었습니다.");

        // 작성 완료 후 목록 이동
        navigate("/boards");

      })
      .catch((error) => {

        handleError(error, navigate);

      });

  };


    return (
    <div className="create-container">

      <div className="create-box">

        <h1>게시글 작성</h1>


        <div className="input-group">

          <Input
            type="text"
            placeholder="제목"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

        </div>


        <div className="input-group">

          <TextArea
            placeholder="내용"
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />

        </div>


        <div className="file-group">

          <input
            type="file"
            onChange={(e) => setFile(e.target.files[0])}
          />


          {
            file && (
              <p className="selected-file">
                선택 파일 : {file.name}
              </p>
            )
          }

        </div>


        <Button onClick={handleSubmit}>
          작성
        </Button>


      </div>

    </div>
  );
}

export default BoardCreate;