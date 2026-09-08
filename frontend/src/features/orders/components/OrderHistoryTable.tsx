import { CalendarDays, ClipboardList, Eye, Package } from "lucide-react";

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

function getUserLabel(userId: number | null | undefined) {
  return userId == null ? "Sin usuario" : `Usuario #${userId}`;
}

function getOrderLabel(order: OrderResponse) {
  return order.id == null ? "Orden sin ID" : `Orden #${order.id}`;
}

function getOrderKey(order: OrderResponse, index: number) {
  return order.id ?? `${order.createdAt ?? "order"}-${index}`;
}

export function OrderHistoryTable({
  orders,
  onViewDetail,
  showUserColumn = false,
}: OrderHistoryTableProps) {
  return (
    <div className="rounded-card border border-border-soft bg-surface-soft">
      <div className="space-y-3 p-4 lg:hidden">
        {orders.map((order, index) => (
          <article
            key={getOrderKey(order, index)}
            className="rounded-card border border-border-soft bg-background/30 p-4"
          >
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary rounded-t-2xl">
                <ClipboardList size={18} />
              </div>

              <div className="min-w-0 flex-1">
                <div className="flex flex-wrap items-center gap-2">
                  <h3 className="font-semibold text-text-main">
                    {getOrderLabel(order)}
                  </h3>

                  <span className="rounded-full border border-border-soft px-2 py-0.5 text-[11px] font-medium text-success">
                    {order.status ?? "Guardada"}
                  </span>
                </div>

                <p className="mt-1 text-sm text-text-muted">
                  {getModeLabel(order.mode)}
                </p>
              </div>
            </div>

            <div className="mt-4 grid grid-cols-1 gap-3 text-sm sm:grid-cols-2">
              {showUserColumn && (
                <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                  <p className="text-xs uppercase tracking-wide text-text-muted">
                    Usuario
                  </p>
                  <p className="mt-1 font-medium text-text-main">
                    {getUserLabel(order.userId)}
                  </p>
                </div>
              )}

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Fecha
                </p>
                <p className="mt-1 flex items-center gap-2 font-medium text-text-main">
                  <CalendarDays size={15} />
                  {formatDate(order.createdAt)}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Tragos
                </p>
                <p className="mt-1 font-semibold text-primary">
                  {getTotalDrinks(order)}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Productos
                </p>
                <p className="mt-1 flex items-center gap-2 font-medium text-text-main">
                  <Package size={15} />
                  {order.items?.length ?? 0}
                </p>
              </div>
            </div>

            <Button
              type="button"
              variant="secondary"
              fullWidth
              className="mt-4"
              onClick={() => onViewDetail(order)}
              disabled={order.id == null}
            >
              <span className="flex items-center justify-center gap-2">
                <Eye size={16} />
                Ver detalle
              </span>
            </Button>
          </article>
        ))}
      </div>

      <div className="hidden overflow-hidden rounded-card lg:block">
        <div className="overflow-x-auto">
          <table
            className={`w-full border-separate border-spacing-0 ${
              showUserColumn ? "min-w-240" : "min-w-215"
            }`}
          >
            <thead>
              <tr>
                <th className="rounded-tl-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Orden
                </th>

                {showUserColumn && (
                  <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                    Usuario
                  </th>
                )}

                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Modo
                </th>

                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Fecha
                </th>

                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Tragos
                </th>

                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Productos
                </th>

                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Estado
                </th>

                <th className="rounded-tr-card bg-surface px-4 py-3 text-right text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Acción
                </th>
              </tr>
            </thead>

            <tbody>
              {orders.map((order, index) => (
                <tr
                  key={getOrderKey(order, index)}
                  className="border-b border-border-soft last:border-0"
                >
                  <td className="px-4 py-4">
                    <div className="flex items-center gap-3">
                      <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-background text-primary">
                        <ClipboardList size={18} />
                      </div>

                      <div>
                        <p className="font-medium text-text-main">
                          {getOrderLabel(order)}
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
                      {order.status ?? "Guardada"}
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
    </div>
  );
}
