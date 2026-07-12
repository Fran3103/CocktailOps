import { Card } from "../../../shared/components/ui/Card";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  SelectedOrderCocktail,
} from "../order.types";

type OrderSummaryPanelProps = {
  orderMode: OrderMode;
  guests: string;
  durationHours: string;
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktails: SelectedOrderCocktail[];
  payload: CreateTimeOrderRequest | CreateDrinksOrderRequest | null;
};

export function OrderSummaryPanel({
  orderMode,
  guests,
  durationHours,
  totalDrinks,
  assignedDrinks,
  selectedCocktails,
  payload,
}: OrderSummaryPanelProps) {
  const estimatedDrinks =
    Number(guests) > 0 && Number(durationHours) > 0
      ? Number(guests) * Number(durationHours) * 2
      : 0;

  return (
    <Card className="space-y-4">
      <div>
        <h2 className="font-heading text-xl font-semibold text-text-main">
          Resumen
        </h2>

        <p className="mt-1 text-sm text-text-muted">
          Vista previa de la orden antes de enviarla al backend.
        </p>
      </div>

      <div className="rounded-control bg-background p-3">
        <p className="text-text-muted">Modo</p>
        <p className="text-lg font-semibold text-primary">
          {orderMode === "TIME" ? "Por evento" : "Por cantidad"}
        </p>
      </div>

      {orderMode === "TIME" ? (
        <div className="grid grid-cols-1 gap-3 text-sm sm:grid-cols-3">
          <div className="rounded-control bg-background p-3">
            <p className="text-text-muted">Invitados</p>
            <p className="text-lg font-semibold text-text-main">
              {guests || "-"}
            </p>
          </div>

          <div className="rounded-control bg-background p-3">
            <p className="text-text-muted">Horas</p>
            <p className="text-lg font-semibold text-text-main">
              {durationHours || "-"}
            </p>
          </div>

          <div className="rounded-control bg-background p-3">
            <p className="text-text-muted">Tragos estimados</p>
            <p className="text-lg font-semibold text-primary">
              {estimatedDrinks || "-"}
            </p>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-3 text-sm sm:grid-cols-2">
          <div className="rounded-control bg-background p-3">
            <p className="text-text-muted">Total tragos</p>
            <p className="text-lg font-semibold text-text-main">
              {totalDrinks || "-"}
            </p>
          </div>

          <div className="rounded-control bg-background p-3">
            <p className="text-text-muted">Asignados</p>
            <p
              className={`text-lg font-semibold ${
                Number(totalDrinks) === assignedDrinks
                  ? "text-success"
                  : "text-primary"
              }`}
            >
              {assignedDrinks} / {totalDrinks || "-"}
            </p>
          </div>
        </div>
      )}

      <div>
        <p className="text-sm text-text-muted">Cócteles seleccionados</p>
        <p className="text-lg font-semibold text-text-main">
          {selectedCocktails.length}
        </p>
      </div>

      {payload && (
        <pre className="max-h-72 overflow-auto rounded-control bg-background p-4 text-xs text-text-muted">
          {JSON.stringify(payload, null, 2)}
        </pre>
      )}
    </Card>
  );
}