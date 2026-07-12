import { Card } from "../../../shared/components/ui/Card";
import type { OrderResponse } from "../order.types";

type CreatedOrderSummaryProps = {
  order: OrderResponse;
};

export function CreatedOrderSummary({ order }: CreatedOrderSummaryProps) {
  const cocktailCount = order.cocktail?.length ?? 0;
  const itemCount = order.items?.length ?? 0;

  const totalDrinks =
    order.cocktail?.reduce(
      (total, cocktail) => total + (cocktail.quantity ?? 0),
      0
    ) ?? 0;

  return (
    <Card className="border-success/40 bg-surface">
      <h2 className="font-heading text-xl font-semibold text-success">
        Orden creada correctamente
      </h2>

      <p className="mt-1 text-sm text-text-muted">
        El backend calculó los cócteles y los productos necesarios para la
        orden.
      </p>

      <div className="mt-4 grid grid-cols-1 gap-3 text-sm sm:grid-cols-4">
        <div className="rounded-control bg-background p-3">
          <p className="text-text-muted">Orden ID</p>
          <p className="text-lg font-semibold text-text-main">#{order.id}</p>
        </div>

        <div className="rounded-control bg-background p-3">
          <p className="text-text-muted">Estado</p>
          <p className="text-lg font-semibold text-text-main">
            {order.status}
          </p>
        </div>

        <div className="rounded-control bg-background p-3">
          <p className="text-text-muted">Tragos</p>
          <p className="text-lg font-semibold text-primary">{totalDrinks}</p>
        </div>

        <div className="rounded-control bg-background p-3">
          <p className="text-text-muted">Productos</p>
          <p className="text-lg font-semibold text-text-main">{itemCount}</p>
        </div>
      </div>

      <p className="mt-4 text-sm text-text-muted">
        Cócteles incluidos: {cocktailCount}
      </p>
    </Card>
  );
}