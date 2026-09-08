import { ArrowLeft, CalendarDays, ClipboardList } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { ErrorState } from "../../shared/utils/ErrorState";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";
import { getApiErrorMessage } from "../../shared/utils/getApiErrorMessage";
import { useAuth } from "../auth/useAuth";
import { OrderCocktailsTable } from "./components/OrderCocktailsTable";
import { OrderItemsTable } from "./components/OrderItemsTable";
import { OrderPdfDownloadButton } from "./components/OrderPdfDownloadButton";
import { orderService } from "./orderService";
import type { OrderResponse } from "./order.types";

type OrderDetailLocationState = {
  order?: OrderResponse;
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

function getOrderDetailErrorMessage(error: unknown) {
  return getApiErrorMessage(error, {
    defaultMessage: "No se pudo cargar el detalle de la orden.",
    networkMessage: "No se pudo conectar con el servidor para cargar la orden.",
    unauthorizedMessage:
      "Tu sesión no está activa o venció. Iniciá sesión nuevamente para ver esta orden.",
    forbiddenMessage: "No tenés permisos para ver esta orden.",
    notFoundMessage: "No se encontró la orden solicitada.",
    serverMessage:
      "Ocurrió un error en el servidor al cargar el detalle de la orden. Intentá nuevamente más tarde.",
  });
}

export function OrderDetailPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();
  const { user } = useAuth();

  const locationState = location.state as OrderDetailLocationState | null;
  const initialOrder = locationState?.order ?? null;

  const orderId = useMemo(() => {
    const numericId = Number(id);
    return Number.isFinite(numericId) && numericId > 0 ? numericId : null;
  }, [id]);

  const [order, setOrder] = useState<OrderResponse | null>(initialOrder);
  const [isLoading, setIsLoading] = useState(!initialOrder);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!orderId) {
      return;
    }

    const validOrderId = orderId;

    if (initialOrder?.id === validOrderId) {
      return;
    }

    let ignore = false;

    async function fetchOrder() {
      try {
        const data = await orderService.getOrderById(validOrderId);

        if (!ignore) {
          setOrder(data);
          setError(null);
        }
      } catch (fetchOrderError) {
        if (!ignore) {
          setError(getOrderDetailErrorMessage(fetchOrderError));
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    }

    void fetchOrder();

    return () => {
      ignore = true;
    };
  }, [orderId, initialOrder]);

  if (!orderId) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Detalle de orden"
          description="El identificador de la orden no es válido."
        />

        <ErrorState
          status="400"
          title="ID de orden inválido"
          description="La URL no contiene un identificador válido para consultar una orden guardada."
        >
          <Button type="button" onClick={() => navigate(ROUTES.orders)}>
            Volver al historial
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.createOrder)}
          >
            Crear una nueva orden
          </Button>
        </ErrorState>
      </section>
    );
  }

  if (isLoading) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Detalle de orden"
          description="Cargando información de la orden."
        />

        <Card>
          <p className="text-text-muted">Cargando orden...</p>
        </Card>
      </section>
    );
  }

  if (error || !order) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Detalle de orden"
          description="No se pudo mostrar la información de esta orden."
        />

        <ErrorState
          title="No pudimos cargar la orden"
          description={error ?? "No se encontró la orden solicitada."}
        >
          <Button type="button" onClick={() => navigate(ROUTES.orders)}>
            Volver al historial
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.createOrder)}
          >
            Crear una nueva orden
          </Button>
        </ErrorState>
      </section>
    );
  }

  const totalDrinks = getTotalDrinks(order);
  const isTimeMode = order.mode === "TIME";
  const orderOwnerLabel =
    order.userId == null
      ? "Orden temporal"
      : user?.id === order.userId
        ? `Orden asociada a ${user.firstName}`
        : `Orden de usuario #${order.userId}`;

  if (order.id == null) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Detalle de orden"
          description="Esta orden no tiene un identificador válido."
        />

        <ErrorState
          status="400"
          title="Orden sin ID"
          description="Esta orden no puede recuperarse como orden guardada porque no tiene identificador."
        >
          <Button type="button" onClick={() => navigate(ROUTES.createOrder)}>
            Crear una nueva orden
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.orders)}
          >
            Volver al historial
          </Button>
        </ErrorState>
      </section>
    );
  }

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <PageHeader
          title={`Orden #${order.id}`}
          description="Detalle del cálculo generado por CocktailOps."
        />

        <div className="flex flex-col gap-3 sm:flex-row">
          <OrderPdfDownloadButton
            source={{ type: "SAVED_ORDER", orderId: order.id }}
          />

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.createOrder)}
          >
            Crear nueva orden
          </Button>

          <Button type="button" variant="ghost" onClick={() => navigate(-1)}>
            <span className="flex items-center justify-center gap-2">
              <ArrowLeft size={16} />
              Volver
            </span>
          </Button>
        </div>
      </div>

      <Card className="border-primary/30 bg-[linear-gradient(135deg,rgba(197,160,89,0.12),rgba(26,46,38,0.92))]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
          <div className="rounded-control border border-border-soft bg-background/70 p-4">
            <p className="text-sm text-text-muted">Modo</p>
            <p className="mt-1 text-xl font-semibold text-primary">
              {isTimeMode ? "Por evento" : "Por cantidad"}
            </p>
          </div>

          <div className="rounded-control border border-border-soft bg-background/70 p-4">
            <p className="text-sm text-text-muted">Estado</p>
            <p className="mt-1 text-xl font-semibold text-text-main">
              {order.status}
            </p>
          </div>

          <div className="rounded-control border border-border-soft bg-background/70 p-4">
            <p className="text-sm text-text-muted">Total tragos</p>
            <p className="mt-1 text-xl font-semibold text-primary">
              {totalDrinks}
            </p>
          </div>

          <div className="rounded-control border border-border-soft bg-background/70 p-4">
            <p className="text-sm text-text-muted">Productos calculados</p>
            <p className="mt-1 text-xl font-semibold text-text-main">
              {order.items?.length ?? 0}
            </p>
          </div>
        </div>

        <div className="mt-4 flex flex-col gap-3 text-sm text-text-muted md:flex-row md:items-center md:justify-between">
          <span className="flex items-center gap-2">
            <CalendarDays size={16} />
            Creada: {formatDate(order.createdAt)}
          </span>

          <span className="flex items-center gap-2">
            <ClipboardList size={16} />
            {orderOwnerLabel}
          </span>
        </div>
      </Card>

      {isTimeMode && (
        <Card className="space-y-4">
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Datos del evento
          </h2>

          <div className="grid grid-cols-1 gap-3 sm:grid-cols-3">
            <div className="rounded-control bg-background p-3">
              <p className="text-sm text-text-muted">Invitados</p>
              <p className="text-lg font-semibold text-text-main">
                {order.guests ?? "-"}
              </p>
            </div>

            <div className="rounded-control bg-background p-3">
              <p className="text-sm text-text-muted">Horas</p>
              <p className="text-lg font-semibold text-text-main">
                {order.durationHours ?? "-"}
              </p>
            </div>

            <div className="rounded-control bg-background p-3">
              <p className="text-sm text-text-muted">Tragos/persona/hora</p>
              <p className="text-lg font-semibold text-text-main">
                {order.drinksPerPerson ?? "-"}
              </p>
            </div>
          </div>
        </Card>
      )}

      <Card className="space-y-4">
        <div>
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Cócteles calculados
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Cantidad de tragos calculada para cada cóctel.
          </p>
        </div>

        <OrderCocktailsTable cocktails={order.cocktail ?? []} />
      </Card>

      <Card className="space-y-4">
        <div>
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Productos a comprar
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Packs calculados automáticamente a partir de los ingredientes de los
            cócteles seleccionados.
          </p>
        </div>

        <OrderItemsTable items={order.items ?? []} />
      </Card>
    </section>
  );
}