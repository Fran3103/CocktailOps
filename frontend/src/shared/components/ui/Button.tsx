import type { ButtonHTMLAttributes, ReactNode } from "react";

type ButtonVariant = "primary" | "secondary" | "ghost";

type ButtonProps = {
  children: ReactNode;
  variant?: ButtonVariant;
  fullWidth?: boolean;
} & ButtonHTMLAttributes<HTMLButtonElement>;

const variantClasses: Record<ButtonVariant, string> = {
  primary: "bg-primary text-background hover:bg-primary-soft font-semibold",
  secondary:
    "border border-border-soft bg-transparent text-text-main hover:bg-surface-bright",
  ghost:
    "bg-transparent text-text-muted hover:bg-surface-bright hover:text-text-main",
};

export function Button({
  children,
  variant = "primary",
  fullWidth = false,
  className = "",
  ...props
}: ButtonProps) {
  return (
    <button
      className={`rounded-control px-4 py-2 text-sm transition disabled:cursor-not-allowed disabled:opacity-60 ${
        variantClasses[variant]
      } ${fullWidth ? "w-full" : ""} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}