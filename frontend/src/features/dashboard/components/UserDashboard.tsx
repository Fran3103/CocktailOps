import { ClipboardList, GlassWater, History } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { ErrorState } from "../../../shared/utils/ErrorState";
import { PageHeader } from "../../../shared/components/ui/PageHeader";
import { ROUTES } from "../../../shared/constants/routes";
import { getApiErrorMessage } from "../../../shared/utils/getApiErrorMessage";
import { orderService } from "../../orders/orderService";
import type { OrderResponse } from "../../orders/order.types";
import { CocktailsPreview } from "./CocktailsPreview";
import { DashboardMetricCard } from "./DashboardMetricCard";
import { RecentOrdersTable } from "./RecentOrdersTable";

const RECENT_ORDERS_LIMIT = 5;

async function getUserOrders(): Promise<OrderResponse[]> {
  return orderService.getMyOrders();
}

function getOrderTotalDrinks(order: OrderResponse) {
  return order.cocktail.reduce(
    (total, cocktail) => total + cocktail.quantity,
    0,
  );
}

function getOrdersTotalDrinks(orders: OrderResponse[]) {
  return orders.reduce((total, order) => total + getOrderTotalDrinks(order), 0);
}

function getOrdersErrorMessage(error: unknown) {
  return getApiErrorMessage(error, {
    defaultMessage: "No se pudieron cargar tus registros.",
    networkMessage:
      "No se pudo conectar con el servidor para cargar tu dashboard.",
    unauthorizedMessage:
      "Tu sesión no está activa o venció. Iniciá sesión nuevamente.",
    forbiddenMessage: "No tenés permisos para ver estos registros.",
    notFoundMessage: "No se encontró el historial de tus registros.",
    serverMessage:
      "Ocurrió un error en el servidor al cargar tu dashboard. Intentá nuevamente más tarde.",
  });
}

function sortOrdersByDateDesc(orders: OrderResponse[]) {
  return [...orders].sort((a, b) => {
    const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
    const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;

    return dateB - dateA;
  });
}

export function UserDashboard() {
  const navigate = useNavigate();

  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const sortedOrders = useMemo(() => sortOrdersByDateDesc(orders), [orders]);

  const recentOrders = useMemo(
    () => sortedOrders.slice(0, RECENT_ORDERS_LIMIT),
    [sortedOrders],
  );

  const totalDrinks = useMemo(() => getOrdersTotalDrinks(orders), [orders]);

  async function loadOrders() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await getUserOrders();
      setOrders(data);
    } catch (loadError) {
      setError(getOrdersErrorMessage(loadError));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let isMounted = true;

    async function loadInitialOrders() {
      try {
        const data = await getUserOrders();

        if (!isMounted) return;

        setOrders(data);
      } catch (loadError) {
        if (!isMounted) return;

        setError(getOrdersErrorMessage(loadError));
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    void loadInitialOrders();

    return () => {
      isMounted = false;
    };
  }, []);

  if (isLoading) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Dashboard"
          description="Cargando el resumen de tus registros."
        />

        <Card className="border-border-soft bg-surface-soft/80">
          <p className="text-text-muted">Cargando registros...</p>
        </Card>
      </section>
    );
  }

  if (error) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Dashboard"
          description="No pudimos cargar el resumen de tus registros."
        />

        <ErrorState title="No pudimos cargar tu dashboard" description={error}>
          <Button type="button" onClick={loadOrders}>
            Reintentar
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.login)}
          >
            Ir al login
          </Button>
        </ErrorState>

        <CocktailsPreview />
      </section>
    );
  }

  return (
    <section className="space-y-8">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <PageHeader
          title="Dashboard"
          description="Resumen de tus registros guardados y accesos rápidos para seguir calculando eventos."
        />

        <div className="flex flex-col gap-3 sm:flex-row">
          <Button type="button" onClick={() => navigate(ROUTES.createOrder)}>
            Crear nueva orden
          </Button>

          <Button
            type="button"
            variant="secondary"
            onClick={() => navigate(ROUTES.orders)}
          >
            Ver historial
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <DashboardMetricCard
          title="Mi historial"
          value={orders.length}
          description="Registros guardados en tu historial."
          icon={<ClipboardList size={20} />}
        />

        <DashboardMetricCard
          title="Tragos calculados"
          value={totalDrinks}
          description="Total estimado entre tus registros guardados."
          icon={<GlassWater size={20} />}
        />

        <DashboardMetricCard
          title="Últimos registros"
          value={recentOrders.length}
          description="Registros recientes disponibles para consultar o descargar."
          icon={<History size={20} />}
        />
      </div>

      <RecentOrdersTable
        orders={recentOrders}
        title="Tus últimos registros"
        description="Accedé al detalle o descargá el PDF de tus registros recientes."
       emptyMessage="Todavía no tenés registros guardados. Creá tu primera orden para verla en el historial."
      />

      <CocktailsPreview />
    </section>
  );
}