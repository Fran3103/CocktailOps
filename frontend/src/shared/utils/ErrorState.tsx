import { AlertTriangle } from "lucide-react";
import type { ReactNode } from "react";

import { Card } from "../../shared/components/ui/Card";

type ErrorStateProps = {
  status?: string;
  title: string;
  description: string;
  children?: ReactNode;
  className?: string;
};

export function ErrorState({
  status,
  title,
  description,
  children,
  className = "",
}: ErrorStateProps) {
  return (
    <Card
      className={`border-danger/30 bg-[linear-gradient(135deg,rgba(255,180,171,0.10),rgba(26,46,38,0.92))] ${className}`}
    >
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start">
        <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-control border border-danger/30 bg-background/80 text-danger">
          <AlertTriangle size={22} />
        </div>

        <div className="min-w-0 flex-1">
          {status && (
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-danger">
              Error {status}
            </p>
          )}

          <h2 className="mt-1 font-heading text-2xl font-semibold text-text-main">
            {title}
          </h2>

          <p className="mt-2 max-w-2xl text-sm leading-6 text-text-muted">
            {description}
          </p>

          {children && <div className="mt-5 flex flex-col gap-3 sm:flex-row">{children}</div>}
        </div>
      </div>
    </Card>
  );
}