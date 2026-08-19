import type { ReactNode } from "react";

import { Card } from "../../../shared/components/ui/Card";

type DashboardMetricCardProps = {
  title: string;
  value: string | number;
  description?: string;
  icon?: ReactNode;
};

export function DashboardMetricCard({
  title,
  value,
  description,
  icon,
}: DashboardMetricCardProps) {
  return (
    <Card className="flex h-full flex-col justify-between gap-4 border-border-soft bg-surface-soft/80">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm font-medium text-text-muted">{title}</p>
          <p className="mt-2 text-3xl font-bold text-text-main">{value}</p>
        </div>

        {icon && (
          <div className="rounded-control border border-border-soft bg-background/40 p-2 text-primary">
            {icon}
          </div>
        )}
      </div>

      {description && (
        <p className="text-sm leading-6 text-text-muted">{description}</p>
      )}
    </Card>
  );
}