import type { InputHTMLAttributes } from "react";

type InputProps = {
  label?: string;
  error?: string;
} & InputHTMLAttributes<HTMLInputElement>;

export function Input({
  label,
  error,
  className = "",
  id,
  ...props
}: InputProps) {
  const inputId = id ?? props.name;

  return (
    <div className="space-y-1">
      {label && (
        <label htmlFor={inputId} className="text-sm font-medium text-text-muted">
          {label}
        </label>
      )}

      <input
        id={inputId}
        className={`w-full rounded-control border border-border bg-background px-4 py-2 text-sm text-text-main outline-none placeholder:text-text-muted focus:border-primary ${className}`}
        {...props}
      />

      {error && <p className="text-sm text-danger">{error}</p>}
    </div>
  );
}