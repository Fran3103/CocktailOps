import { CalendarDays, ClipboardList, Eye } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import type { OrderResponse } from "../order.types";

type OrderHistoryTableProps = {
  orders: OrderResponse[];
  onViewDetail: (order: OrderResponse) => void;
  showUserColumn?: boolean;
};

function formatDate(value: string | null | undefined) {
  if (!value) {
    return "Sin fecha";
  }

  return new Intl.DateTimeFormat("es-AR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(value));
}

function getTotalDrinks(order: OrderResponse) {
  return (
    order.cocktail?.reduce(
      (total, cocktail) => total + (cocktail.quantity ?? 0),
      0,
    ) ?? 0
  );
}

function getModeLabel(mode: OrderResponse["mode"]) {
  return mode === "TIME" ? "Por evento" : "Por cantidad";
}

function getUserLabel(userId: number | null) {
  return userId == null ? "Sin usuario" : `Usuario #${userId}`;
}

export function OrderHistoryTable({
  orders,
  onViewDetail,
  showUserColumn = false,
}: OrderHistoryTableProps) {
  return (
    <div className="overflow-hidden rounded-card border border-border-soft bg-surface-soft">
      <div className="overflow-x-auto">
        <table
          className={`w-full border-collapse ${
            showUserColumn ? "min-w-[960px]" : "min-w-[860px]"
          }`}
        >
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Orden
              </th>

              {showUserColumn && (
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Usuario
                </th>
              )}

              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Modo
              </th>

              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Fecha
              </th>

              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Tragos
              </th>

              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Productos
              </th>

              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Estado
              </th>

              <th className="px-4 py-3 text-right text-xs font-semibold uppercase tracking-wide text-text-muted">
                Acción
              </th>
            </tr>
          </thead>

          <tbody>
            {orders.map((order, index) => (
              <tr
                key={order.id ?? `${order.createdAt ?? "order"}-${index}`}
                className="border-b border-border-soft last:border-0"
              >
                <td className="px-4 py-4">
                  <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-background text-primary">
                      <ClipboardList size={18} />
                    </div>

                    <div>
                      <p className="font-medium text-text-main">
                        Orden #{order.id}
                      </p>

                      <p className="text-xs text-text-muted">
                        Orden guardada
                      </p>
                    </div>
                  </div>
                </td>

                {showUserColumn && (
                  <td className="px-4 py-4 text-sm text-text-muted">
                    {getUserLabel(order.userId)}
                  </td>
                )}

                <td className="px-4 py-4 text-sm text-text-muted">
                  {getModeLabel(order.mode)}
                </td>

                <td className="px-4 py-4 text-sm text-text-muted">
                  <span className="flex items-center gap-2">
                    <CalendarDays size={15} />
                    {formatDate(order.createdAt)}
                  </span>
                </td>

                <td className="px-4 py-4 text-sm font-semibold text-primary">
                  {getTotalDrinks(order)}
                </td>

                <td className="px-4 py-4 text-sm text-text-muted">
                  {order.items?.length ?? 0}
                </td>

                <td className="px-4 py-4">
                  <span className="rounded-full border border-border-soft px-2 py-1 text-xs font-medium text-success">
                    {order.status}
                  </span>
                </td>

                <td className="px-4 py-4 text-right">
                  <Button
                    type="button"
                    variant="secondary"
                    onClick={() => onViewDetail(order)}
                    disabled={order.id == null}
                  >
                    <span className="flex items-center justify-center gap-2">
                      <Eye size={16} />
                      Ver detalle
                    </span>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}