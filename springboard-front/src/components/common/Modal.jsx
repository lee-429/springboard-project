function Modal({
  open,
  title,
  message,
  onConfirm,
  onCancel,
}) {

  if (!open) {
    return null;
  }

  return (
    <div className="modal-overlay">

      <div className="modal">

        <h2>{title}</h2>

        <p>{message}</p>

        <div className="modal-buttons">

          <button
            className="cancel-btn"
            onClick={onCancel}
          >
            취소
          </button>

          <button
            className="confirm-btn"
            onClick={onConfirm}
          >
            확인
          </button>

        </div>

      </div>

    </div>
  );
}

export default Modal;