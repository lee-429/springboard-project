function Loading({ message = "불러오는 중...⏳" }) {
  return (
    <div className="loading">
      <div className="loading-spinner"></div>
      <p>{message}</p>
    </div>
  );
}

export default Loading;