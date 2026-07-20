import Button from "./Button";

function Pagination({ page, currentPage, onPageChange }) {
  const startPage = Math.floor(currentPage / 10) * 10;
  const endPage = Math.min(startPage + 10, page.totalPages);

  return (
    <div className="pagination">
      <Button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={page.first}
      >
        이전
      </Button>

      {[...Array(endPage - startPage)].map((_, index) => {
        const pageNumber = startPage + index;

        return (
          <Button
            key={pageNumber}
            onClick={() => onPageChange(pageNumber)}
            className={currentPage === pageNumber ? "active-page" : ""}
          >
            {pageNumber + 1}
          </Button>
        );
      })}

      <Button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={page.last}
      >
        다음
      </Button>
    </div>
  );
}

export default Pagination;