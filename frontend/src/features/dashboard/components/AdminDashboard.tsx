import { isAxiosError } from "axios";
import { ClipboardList, Clock, GlassWater, Martini } from "lucide-react";
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

async function getAdminOrders(): Promise<OrderResponse[]> {
  return orderService.getAllOrders();
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

function getOrdersByMode(orders: OrderResponse[], mode: OrderResponse["mode"]) {
  return orders.filter((order) => order.mode === mode).length;
}

function getOrdersErrorMessage(error: unknown) {
  if (!isAxiosError(error)) {
    return "No se pudieron cargar las órdenes del sistema.";
  }

  const status = error.response?.status;

  if (status === 401) {
    return "Tu sesión venció. Iniciá sesión nuevamente.";
  }

  if (status === 403) {
    return "No tenés permisos para ver el dashboard de administración.";
  }

  return "No se pudieron cargar las órdenes del sistema.";
}

function sortOrdersByDateDesc(orders: OrderResponse[]) {
  return [...orders].sort((a, b) => {
    const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
    const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;

    return dateB - dateA;
  });
}

export function AdminDashboard() {
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

  const timeOrdersCount = useMemo(
    () => getOrdersByMode(orders, "TIME"),
    [orders],
  );

  const drinksOrdersCount = useMemo(
    () => getOrdersByMode(orders, "DRINKS"),
    [orders],
  );

  async function loadOrders() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await getAdminOrders();
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
        const data = await getAdminOrders();

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
          title="Dashboard admin"
          description="Cargando el resumen general de órdenes."
        />

        <Card className="border-border-soft bg-surface-soft/80">
          <p className="text-text-muted">Cargando órdenes del sistema...</p>
        </Card>
      </section>
    );
  }

  if (error) {
    return (
      <section className="space-y-6">
        <PageHeader
          title="Dashboard admin"
          description="No pudimos cargar el resumen general de órdenes."
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
          title="Dashboard admin"
          description="Resumen general de órdenes calculadas en CocktailOps."
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
            Ver mis órdenes
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <DashboardMetricCard
          title="Órdenes del sistema"
          value={orders.length}
          description="Total de órdenes guardadas en la aplicación."
          icon={<ClipboardList size={20} />}
        />

        <DashboardMetricCard
          title="Tragos calculados"
          value={totalDrinks}
          description="Total estimado entre todas las órdenes guardadas."
          icon={<GlassWater size={20} />}
        />

        <DashboardMetricCard
          title="Órdenes por evento"
          value={timeOrdersCount}
          description="Órdenes calculadas por invitados y duración."
          icon={<Clock size={20} />}
        />

        <DashboardMetricCard
          title="Órdenes por tragos"
          value={drinksOrdersCount}
          description="Órdenes calculadas por cantidad total de tragos."
          icon={<Martini size={20} />}
        />
      </div>

      <RecentOrdersTable
        orders={recentOrders}
        title="Últimas órdenes del sistema"
        description="Órdenes recientes creadas por usuarios registrados."
        showUserColumn
        emptyMessage="Todavía no hay órdenes guardadas en el sistema."
      />

      <CocktailsPreview />
    </section>
  );
}