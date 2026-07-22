import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getBoards } from "../api/boardApi";
import { handleError } from "../api/errorHandler";
import Pagination from "../components/common/Pagination"
import Loading from "../components/common/Loading";
import EmptyState from "../components/common/EmptyState";
import "../styles/BoardList.css";
import Button from "../components/common/Button";

function BoardList() {

  // 게시글 목록 저장
  const [page, setPage] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);

  const [type, setType] = useState("");
  const [keyword, setKeyword] = useState("");

  const navigate = useNavigate();

  const loadBoards = (pageNumber) => {
    getBoards(pageNumber, type, keyword)
      .then((response) => {

        console.log(response.data);
        console.log("content 개수:", response.data.content.length);

        setPage(response.data);
        setCurrentPage(pageNumber);
      })
      .catch((error) => {
        handleError(error, navigate);
      })
  }

  const handleSearch = () => {
    loadBoards(0);
  }

  // 페이지 로딩 시 게시글 조회
  useEffect(() => {
    loadBoards(0);
  }, []);

  if (page === null) {
    return <Loading message="게시글을 불러오는 중...⏳"/>;
  }

  if (page.content.length === 0) {
    return (
      <div className="empty-board">
        <EmptyState
          icon={keyword ? "🔍" : "📋"}
          title={keyword ? "검색 결과가 없습니다." : "게시글이 없습니다."}
          description={
            keyword
              ? "다른 검색어로 다시 시도해주세요."
              : "첫 게시글을 작성해보세요."
          }
        />

        {!keyword && (
          <Button onClick={() => navigate("/boards/create")}>
            첫 게시글 작성하기
          </Button>
        )}
      </div>
    );
  }


  return (
    <div className="board-container">

      <h1>게시글 목록</h1>

      <div className="search-box">

        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
        >
          <option value="">제목+내용</option>
          <option value="title">제목</option>
          <option value="writer">작성자</option>
        </select>

        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          placeholder="검색어 입력" 
        />

        <Button onClick={handleSearch}>검색</Button>
      </div>

      <table className="board-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
          </tr>
        </thead>

        <tbody>
          {page.content.map((board) => (
            <tr key={board.id}>
              <td>{board.id}</td>

              <td>
                <Link to={`/boards/${board.id}`}>
                  {board.title}
                </Link>
              </td>

              <td>{board.writer}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <Pagination
        page={page}
        currentPage={currentPage}
        onPageChange={loadBoards}
      />

    </div>
  );
}

export default BoardList;