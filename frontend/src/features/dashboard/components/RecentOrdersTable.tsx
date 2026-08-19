import { Eye } from "lucide-react";
import { useNavigate } from "react-router-dom";

import { ROUTES } from "../../../shared/constants/routes";
import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { OrderPdfDownloadButton } from "../../orders/components/OrderPdfDownloadButton";
import type { OrderResponse } from "../../orders/order.types";

type RecentOrdersTableProps = {
  orders: OrderResponse[];
  title?: string;
  description?: string;
  showUserColumn?: boolean;
  emptyMessage?: string;
};

function getTotalDrinks(order: OrderResponse) {
  return order.cocktail.reduce(
    (total, cocktail) => total + cocktail.quantity,
    0,
  );
}

function formatOrderMode(mode: OrderResponse["mode"]) {
  return mode === "TIME" ? "Por evento" : "Por tragos";
}

function formatDate(date: string | null) {
  if (!date) return "Sin fecha";

  return new Intl.DateTimeFormat("es-AR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(new Date(date));
}

export function RecentOrdersTable({
  orders,
  title = "Últimas órdenes",
  description = "Órdenes calculadas recientemente.",
  showUserColumn = false,
  emptyMessage = "Todavía no hay órdenes para mostrar.",
}: RecentOrdersTableProps) {
  const navigate = useNavigate();

  function handleViewDetail(orderId: number) {
    navigate(ROUTES.orderDetails.replace(":id", String(orderId)));
  }

  if (orders.length === 0) {
    return (
      <Card className="border-border-soft bg-surface-soft/80">
        <h2 className="text-lg font-semibold text-text-main">{title}</h2>
        <p className="mt-2 text-sm text-text-muted">{emptyMessage}</p>

        <Button
          type="button"
          className="mt-4"
          onClick={() => navigate(ROUTES.createOrder)}
        >
          Crear nueva orden
        </Button>
      </Card>
    );
  }

  return (
    <Card className="border-border-soft bg-surface-soft/80">
      <div className="mb-4">
        <h2 className="text-lg font-semibold text-text-main">{title}</h2>
        <p className="mt-1 text-sm text-text-muted">{description}</p>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full min-w-[760px] text-left text-sm">
          <thead className="border-b border-border-soft text-text-muted">
            <tr>
              <th className="py-3 pr-4 font-medium">ID</th>

              {showUserColumn && (
                <th className="py-3 pr-4 font-medium">Usuario</th>
              )}

              <th className="py-3 pr-4 font-medium">Modo</th>
              <th className="py-3 pr-4 font-medium">Tragos</th>
              <th className="py-3 pr-4 font-medium">Fecha</th>
              <th className="py-3 pr-4 text-right font-medium">Acciones</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-border-soft">
            {orders.map((order) => {
              const orderId = order.id;

              return (
                <tr key={orderId ?? `order-${order.createdAt}`}>
                  <td className="py-4 pr-4 font-medium text-text-main">
                    {orderId ?? "Sin ID"}
                  </td>

                  {showUserColumn && (
                    <td className="py-4 pr-4 text-text-muted">
                      {order.userId ? `#${order.userId}` : "Sin usuario"}
                    </td>
                  )}

                  <td className="py-4 pr-4 text-text-muted">
                    {formatOrderMode(order.mode)}
                  </td>

                  <td className="py-4 pr-4 text-text-muted">
                    {getTotalDrinks(order)}
                  </td>

                  <td className="py-4 pr-4 text-text-muted">
                    {formatDate(order.createdAt)}
                  </td>

                  <td className="py-4 pr-0">
                    <div className="flex justify-end gap-2">
                      {orderId != null && (
                        <>
                          <Button
                            type="button"
                            variant="ghost"
                            onClick={() => handleViewDetail(orderId)}
                          >
                            <span className="flex items-center gap-2">
                              <Eye size={15} />
                              Ver
                            </span>
                          </Button>

                          <OrderPdfDownloadButton
                            source={{
                              type: "SAVED_ORDER",
                              orderId,
                            }}
                            variant="secondary"
                            label="PDF"
                            loadingLabel="PDF..."
                          />
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </Card>
  );
}
