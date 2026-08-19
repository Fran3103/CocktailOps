import { isAxiosError } from "axios";
import { ClipboardList, GlassWater, History } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { ROUTES } from "../../../shared/constants/routes";
import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { PageHeader } from "../../../shared/components/ui/PageHeader";
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
  if (!isAxiosError(error)) {
    return "No se pudieron cargar tus órdenes.";
  }

  const status = error.response?.status;

  if (status === 401) {
    return "Tu sesión venció. Iniciá sesión nuevamente.";
  }

  if (status === 403) {
    return "No tenés permisos para ver estas órdenes.";
  }

  return "No se pudieron cargar tus órdenes.";
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
          description="Cargando el resumen de tus órdenes."
        />

        <Card className="border-border-soft bg-surface-soft/80">
          <p className="text-text-muted">Cargando órdenes...</p>
        </Card>
      </section>
    );
  }

  if (error) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Dashboard"
          description="No pudimos cargar el resumen de tus órdenes."
        />

        <Card className="border-danger/30 bg-surface-soft/80">
          <p className="text-sm text-danger">{error}</p>

          <div className="mt-4 flex flex-col gap-3 sm:flex-row">
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
          </div>
        </Card>

        <CocktailsPreview />
      </section>
    );
  }

  return (
    <section className="space-y-8">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <PageHeader
          title="Dashboard"
          description="Resumen de tus órdenes guardadas y accesos rápidos para seguir calculando eventos."
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
          title="Órdenes propias"
          value={orders.length}
          description="Órdenes guardadas en tu historial."
          icon={<ClipboardList size={20} />}
        />

        <DashboardMetricCard
          title="Tragos calculados"
          value={totalDrinks}
          description="Total estimado entre tus órdenes guardadas."
          icon={<GlassWater size={20} />}
        />

        <DashboardMetricCard
          title="Últimas órdenes"
          value={recentOrders.length}
          description="Órdenes recientes disponibles para consultar o descargar."
          icon={<History size={20} />}
        />
      </div>

      <RecentOrdersTable
        orders={recentOrders}
        title="Tus últimas órdenes"
        description="Accedé al detalle o descargá el PDF de tus órdenes recientes."
        emptyMessage="Todavía no tenés órdenes guardadas. Creá tu primera orden para verla en el historial."
      />

      <CocktailsPreview />
    </section>
  );
}