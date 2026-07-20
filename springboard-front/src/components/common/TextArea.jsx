function TextArea({
  value,
  onChange,
  placeholder = '',
  className = '',
  disabled = false,
  rows = 5,
  ...props
}) {
  return (
    <textarea
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`textarea ${className}`}
      disabled={disabled}
      rows={rows}
      {...props}
    />
  );
}

export default TextArea;