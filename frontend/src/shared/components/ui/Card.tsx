import type { HTMLAttributes, ReactNode } from "react";

type CardProps = {
  children: ReactNode;
} & HTMLAttributes<HTMLDivElement>;

export function Card({ children, className = "", ...props }: CardProps) {
  return (
    <div
      className={`rounded-card border border-border-soft bg-surface-soft p-6 ${className}`}
      {...props}
    >
      {children}
    </div>
  );
}