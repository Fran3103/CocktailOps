import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";
import { useAuth } from "../auth/useAuth";
import { OrderHistoryTable } from "./components/OrderHistoryTable";
import { orderService } from "./orderService";
import type { OrderResponse } from "./order.types";

type HistoryScope = "ALL" | "MINE";

function getTotalProducts(orders: OrderResponse[]) {
  return orders.reduce(
    (total, order) => total + (order.items?.length ?? 0),
    0,
  );
}

export function OrderHistoryPage() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const isAdmin = user?.role === "ADMIN";

  const [historyScope, setHistoryScope] = useState<HistoryScope>(() =>
    user?.role === "ADMIN" ? "ALL" : "MINE",
  );

  const effectiveHistoryScope: HistoryScope = isAdmin ? historyScope : "MINE";

  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function fetchOrders(scope: HistoryScope) {
    return scope === "ALL"
      ? orderService.getAllOrders()
      : orderService.getMyOrders();
  }

  useEffect(() => {
    let ignore = false;

    async function loadOrders() {
      setIsLoading(true);
      setError(null);

      try {
        const data = await fetchOrders(effectiveHistoryScope);

        if (!ignore) {
          setOrders(data);
        }
      } catch {
        if (!ignore) {
          setError(
            effectiveHistoryScope === "ALL"
              ? "No se pudieron cargar todas las órdenes."
              : "No se pudieron cargar tus órdenes.",
          );
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    }

    void loadOrders();

    return () => {
      ignore = true;
    };
  }, [effectiveHistoryScope]);

  async function handleRetry() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await fetchOrders(effectiveHistoryScope);
      setOrders(data);
    } catch {
      setError(
        effectiveHistoryScope === "ALL"
          ? "No se pudieron cargar todas las órdenes."
          : "No se pudieron cargar tus órdenes.",
      );
    } finally {
      setIsLoading(false);
    }
  }

  function handleViewDetail(order: OrderResponse) {
    if (order.id == null) {
      return;
    }

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

  const isShowingAllOrders = effectiveHistoryScope === "ALL";

  const pageTitle = isShowingAllOrders
    ? "Todas las órdenes"
    : "Historial de órdenes";

  const pageDescription = isShowingAllOrders
    ? "Consultá las órdenes generadas por todos los usuarios."
    : "Consultá las órdenes generadas y asociadas a tu cuenta.";

  const emptyTitle = isShowingAllOrders
    ? "Todavía no hay órdenes guardadas"
    : "Todavía no tenés órdenes guardadas";

  const emptyDescription = isShowingAllOrders
    ? "Cuando los usuarios generen órdenes autenticadas, aparecerán en este historial general."
    : "Cuando generes una orden estando logueado, aparecerá en este historial para que puedas consultarla más adelante.";

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <PageHeader title={pageTitle} description={pageDescription} />

        <Button type="button" onClick={() => navigate(ROUTES.createOrder)}>
          Crear nueva orden
        </Button>
      </div>

      {isAdmin && (
        <Card className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="font-heading text-lg font-semibold text-text-main">
              Vista de historial
            </h2>

            <p className="mt-1 text-sm text-text-muted">
              Como administrador podés revisar tus órdenes o el historial
              completo del sistema.
            </p>
          </div>

          <div className="flex gap-2">
            <Button
              type="button"
              variant={isShowingAllOrders ? "primary" : "secondary"}
              onClick={() => setHistoryScope("ALL")}
            >
              Todas las órdenes
            </Button>

            <Button
              type="button"
              variant={!isShowingAllOrders ? "primary" : "secondary"}
              onClick={() => setHistoryScope("MINE")}
            >
              Mis órdenes
            </Button>
          </div>
        </Card>
      )}

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
            {emptyTitle}
          </h2>

          <p className="mt-2 max-w-2xl text-sm text-text-muted">
            {emptyDescription}
          </p>

          <Button
            type="button"
            className="mt-4"
            onClick={() => navigate(ROUTES.createOrder)}
          >
            Crear nueva orden
          </Button>
        </Card>
      )}

      {!isLoading && !error && sortedOrders.length > 0 && (
        <>
          <Card className="grid grid-cols-1 gap-3 sm:grid-cols-3">
            <div className="rounded-control bg-background p-4">
              <p className="text-sm text-text-muted">
                {isShowingAllOrders ? "Órdenes totales" : "Órdenes guardadas"}
              </p>

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
                {getTotalProducts(sortedOrders)}
              </p>
            </div>
          </Card>

          <OrderHistoryTable
            orders={sortedOrders}
            onViewDetail={handleViewDetail}
            showUserColumn={isShowingAllOrders}
          />
        </>
      )}
    </section>
  );
}