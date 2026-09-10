import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { ErrorState } from "../../shared/utils/ErrorState";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";
import { getApiErrorMessage } from "../../shared/utils/getApiErrorMessage";
import { useAuth } from "../auth/useAuth";
import { OrderHistoryTable } from "./components/OrderHistoryTable";
import { orderService } from "./orderService";
import type { OrderResponse } from "./order.types";

type HistoryScope = "ALL" | "MINE";

function getTotalProducts(orders: OrderResponse[]) {
  return orders.reduce((total, order) => total + (order.items?.length ?? 0), 0);
}

function getHistoryErrorMessage(error: unknown, scope: HistoryScope) {
  const isAllOrdersScope = scope === "ALL";

  return getApiErrorMessage(error, {
    defaultMessage: isAllOrdersScope
      ? "No se pudo cargar el historial general."
      : "No se pudocargar tu historial.",
    networkMessage:
      "No se pudo conectar con el servidor para cargar el historial.",
    unauthorizedMessage:
      "Tu sesión no está activa o venció. Iniciá sesión nuevamente para ver el historial.",
    forbiddenMessage: isAllOrdersScope
      ? "No tenés permisos para consultar el historial completo del sistema."
      : "No tenés permisos para consultar este historial.",
    notFoundMessage: "No se encontró el historial solicitado.",
    serverMessage:
      "Ocurrió un error en el servidor al cargar el historial. Intentá nuevamente más tarde.",
  });
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
      } catch (loadOrdersError) {
        if (!ignore) {
          setError(
            getHistoryErrorMessage(loadOrdersError, effectiveHistoryScope),
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
    } catch (retryError) {
      setError(getHistoryErrorMessage(retryError, effectiveHistoryScope));
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

  const pageTitle = isShowingAllOrders ? "Historial general" : "Mi historial";

  const pageDescription = isShowingAllOrders
    ? "Consultá el historial completo generado por los usuarios."
    : "Consultá el historial asociado a tu cuenta.";

  const emptyTitle = isShowingAllOrders
    ? "Todavía no hay registros guardados"
    : "Todavía no tenés registros guardados";

  const emptyDescription = isShowingAllOrders
    ? "Cuando un usuario genere una orden estando logueado, aparecerá en este historial general."
    : "Cuando generes una orden estando logueado, aparecerá en este historial para consultarla más adelante.";
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
            Como administrador podés revisar tu historial personal o el historial completo del sistema.
            </p>
          </div>

          <div className="flex gap-2">
            <Button
              type="button"
              variant={isShowingAllOrders ? "primary" : "secondary"}
              onClick={() => setHistoryScope("ALL")}
            >
              Todo el historial
            </Button>

            <Button
              type="button"
              variant={!isShowingAllOrders ? "primary" : "secondary"}
              onClick={() => setHistoryScope("MINE")}
            >
              Mi historial
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
        <ErrorState title="No pudimos cargar el historial" description={error}>
          <Button type="button" onClick={handleRetry}>
            Reintentar
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.dashboard)}
          >
            Volver al dashboard
          </Button>
        </ErrorState>
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
                {isShowingAllOrders ? "Registros totales" : "Registros guardados"}
              </p>

              <p className="mt-1 text-2xl font-semibold text-text-main">
                {sortedOrders.length}
              </p>
            </div>

            <div className="rounded-control bg-background p-4">
              <p className="text-sm text-text-muted">Último registro</p>

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
