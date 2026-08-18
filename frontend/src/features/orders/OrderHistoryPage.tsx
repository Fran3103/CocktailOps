import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";
import { OrderHistoryTable } from "./components/OrderHistoryTable";
import { orderService } from "./orderService";
import type { OrderResponse } from "./order.types";

export function OrderHistoryPage() {
  const navigate = useNavigate();

  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let ignore = false;

    async function fetchOrders() {
      try {
        const data = await orderService.getMyOrders();

        if (!ignore) {
          setOrders(data);
          setError(null);
        }
      } catch {
        if (!ignore) {
          setError("No se pudieron cargar tus órdenes.");
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    }

    void fetchOrders();

    return () => {
      ignore = true;
    };
  }, []);

  async function handleRetry() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await orderService.getMyOrders();
      setOrders(data);
    } catch {
      setError("No se pudieron cargar tus órdenes.");
    } finally {
      setIsLoading(false);
    }
  }

  function handleViewDetail(order: OrderResponse) {
    navigate(ROUTES.orderDetails.replace(":id", String(order.id)), {
      state: { order },
    });
  }

  const sortedOrders = useMemo(() => {
    return [...orders].sort((a, b) => {
      const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
      const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;

      return dateB - dateA;
    });
  }, [orders]);

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <PageHeader
          title="Historial de órdenes"
          description="Consultá las órdenes generadas y asociadas a tu cuenta."
        />

        <Button type="button" onClick={() => navigate(ROUTES.createOrder)}>
          Crear nueva orden
        </Button>
      </div>

      {isLoading && (
        <Card>
          <p className="text-text-muted">Cargando historial...</p>
        </Card>
      )}

      {!isLoading && error && (
        <Card>
          <p className="text-danger">{error}</p>

          <Button type="button" className="mt-4" onClick={handleRetry}>
            Reintentar
          </Button>
        </Card>
      )}

      {!isLoading && !error && sortedOrders.length === 0 && (
        <Card className="border-primary/30 bg-[linear-gradient(135deg,rgba(197,160,89,0.10),rgba(26,46,38,0.92))]">
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Todavía no tenés órdenes guardadas
          </h2>

          <p className="mt-2 max-w-2xl text-sm text-text-muted">
            Cuando generes una orden estando logueado, aparecerá en este
            historial para que puedas consultarla más adelante.
          </p>

          <Button
            type="button"
            className="mt-4"
            onClick={() => navigate(ROUTES.createOrder)}
          >
            Crear primera orden
          </Button>
        </Card>
      )}

      {!isLoading && !error && sortedOrders.length > 0 && (
        <>
          <Card className="grid grid-cols-1 gap-3 sm:grid-cols-3">
            <div className="rounded-control bg-background p-4">
              <p className="text-sm text-text-muted">Órdenes guardadas</p>
              <p className="mt-1 text-2xl font-semibold text-text-main">
                {sortedOrders.length}
              </p>
            </div>

            <div className="rounded-control bg-background p-4">
              <p className="text-sm text-text-muted">Última orden</p>
              <p className="mt-1 text-2xl font-semibold text-primary">
                #{sortedOrders[0].id}
              </p>
            </div>

            <div className="rounded-control bg-background p-4">
              <p className="text-sm text-text-muted">Productos calculados</p>
              <p className="mt-1 text-2xl font-semibold text-text-main">
                {sortedOrders.reduce(
                  (total, order) => total + (order.items?.length ?? 0),
                  0
                )}
              </p>
            </div>
          </Card>

          <OrderHistoryTable
            orders={sortedOrders}
            onViewDetail={handleViewDetail}
          />
        </>
      )}
    </section>
  );
}