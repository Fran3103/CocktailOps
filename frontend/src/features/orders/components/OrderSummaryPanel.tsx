import { Card } from "../../../shared/components/ui/Card";
import type {
  CreateTimeOrderRequest,
  SelectedOrderCocktail,
} from "../order.types";

type OrderSummaryPanelProps = {
  guests: string;
  durationHours: string;
  selectedCocktails: SelectedOrderCocktail[];
  payload: CreateTimeOrderRequest | null;
};

export function OrderSummaryPanel({
  guests,
  durationHours,
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