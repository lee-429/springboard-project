function EmptyState({
  title = "데이터가 없습니다.",
  description = "",
  icon = "📭",
}) {
  return (
    <div className="empty-state">
      <div className="empty-icon">{icon}</div>

      <h2>{title}</h2>

      {description && (
        <p>{description}</p>
      )}
    </div>
  );
}

export default EmptyState;