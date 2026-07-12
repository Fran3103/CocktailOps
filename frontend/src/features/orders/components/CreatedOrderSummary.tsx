import { CheckCircle2, PlusCircle } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { OrderResponse } from "../order.types";

type CreatedOrderSummaryProps = {
  order: OrderResponse;
  onCreateNewOrder: () => void;
};

export function CreatedOrderSummary({
  order,
  onCreateNewOrder,
}: CreatedOrderSummaryProps) {
  const cocktailCount = order.cocktail?.length ?? 0;
  const itemCount = order.items?.length ?? 0;

  const totalDrinks =
    order.cocktail?.reduce(
      (total, cocktail) => total + (cocktail.quantity ?? 0),
      0
    ) ?? 0;

  return (
    <Card className="overflow-hidden border-success/60 bg-[linear-gradient(135deg,rgba(181,204,192,0.16),rgba(26,46,38,0.96))] shadow-xl shadow-black/30 ring-1 ring-success/30">
      <div className="-mx-6 -mt-6 mb-6 h-4 bg-success" />

      <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
        <div className="flex gap-4">
          <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-success/15 text-success ring-1 ring-success/30">
            <CheckCircle2 size={28} />
          </div>

          <div>
            <p className="text-sm font-semibold uppercase tracking-wide text-success">
              Orden creada
            </p>

            <h2 className="mt-1 font-heading text-2xl font-bold text-text-main">
              Orden #{order.id} generada correctamente
            </h2>

            <p className="mt-2 text-sm text-text-muted">
              El backend calculó los cócteles y los productos necesarios para
              esta orden.
            </p>

            {order.userId ? (
              <p className="mt-3 rounded-control border border-success/20 bg-background/50 px-3 py-2 text-sm text-success">
                La orden quedó asociada a tu cuenta y estará disponible en tu
                historial.
              </p>
            ) : (
              <p className="mt-3 rounded-control border border-primary/20 bg-background/50 px-3 py-2 text-sm text-primary-soft">
                La orden fue generada como invitado y no estará disponible en un
                historial personal.
              </p>
            )}
          </div>
        </div>

        <Button type="button" variant="secondary" onClick={onCreateNewOrder}>
          <span className="flex items-center justify-center gap-2">
            <PlusCircle size={16} />
            Crear nueva orden
          </span>
        </Button>
      </div>

      <div className="mt-6 grid grid-cols-1 gap-3 text-sm sm:grid-cols-4">
        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Orden ID</p>
          <p className="text-lg font-semibold text-text-main">#{order.id}</p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Estado</p>
          <p className="text-lg font-semibold text-text-main">
            {order.status}
          </p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Tragos</p>
          <p className="text-lg font-semibold text-primary">{totalDrinks}</p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Productos</p>
          <p className="text-lg font-semibold text-text-main">{itemCount}</p>
        </div>
      </div>

      <p className="mt-4 text-sm text-text-muted">
        Cócteles incluidos:{" "}
        <span className="font-semibold text-text-main">{cocktailCount}</span>
      </p>
    </Card>
  );
}