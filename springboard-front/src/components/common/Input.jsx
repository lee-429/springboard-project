function Input({
  type = "text",
  value,
  onChange,
  placeholder = "",
  className = "",
  disabled = false,
  ...props
}) {
  return (
    <input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`input ${className}`}
      disabled={disabled}
      {...props}
    />
  );
}

export default Input;