import { CheckCircle2, Clock, Martini, Users } from "lucide-react";

import { Card } from "../../../shared/components/ui/Card";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  SelectedOrderCocktail,
} from "../order.types";
import { getPriorityLabelByWeight } from "../orderPriority";

type OrderSummaryPanelProps = {
  orderMode: OrderMode;
  guests: string;
  durationHours: string;
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktails: SelectedOrderCocktail[];
  payload: CreateTimeOrderRequest | CreateDrinksOrderRequest | null;
  onEditSelection?: () => void;
};
function getPriorityText(weight: number) {
  return `Prioridad ${getPriorityLabelByWeight(weight).toLowerCase()}`;
}
export function OrderSummaryPanel({
  orderMode,
  guests,
  durationHours,
  totalDrinks,
  assignedDrinks,
  selectedCocktails,
  payload,
  onEditSelection,
}: OrderSummaryPanelProps) {
  const numericGuests = Number(guests);
  const numericDurationHours = Number(durationHours);
  const numericTotalDrinks = Number(totalDrinks);

  const drinksPerPersonPerHour = 1;

  const estimatedDrinks =
    orderMode === "TIME" && numericGuests > 0 && numericDurationHours > 0
      ? numericGuests * numericDurationHours * drinksPerPersonPerHour
      : 0;

  const isReady = payload != null;

  return (
    <Card className="overflow-hidden p-0">
      <div className="border-b border-primary/20 bg-primary/10 px-5 py-4">
        <p className="text-xs font-semibold uppercase tracking-wide text-primary">
          Estimación en tiempo real
        </p>

        <h2 className="mt-1 font-heading text-xl font-semibold text-text-main">
          Resumen de orden
        </h2>
      </div>

      <div className="space-y-4 p-5">
        <div className="grid grid-cols-2 gap-3">
          <div className="rounded-control bg-background p-3">
            <div className="mb-2 flex items-center gap-2 text-primary">
              <Martini size={16} />
              <p className="text-xs uppercase tracking-wide">Modo</p>
            </div>

            <p className="text-sm font-semibold text-text-main">
              {orderMode === "TIME" ? "Por evento" : "Por cantidad"}
            </p>
          </div>

          <div className="rounded-control bg-background p-3">
            <div className="mb-2 flex items-center gap-2 text-primary">
              <CheckCircle2 size={16} />
              <p className="text-xs uppercase tracking-wide">Cócteles</p>
            </div>

            <p className="text-lg font-semibold text-text-main">
              {selectedCocktails.length}
            </p>
          </div>
        </div>

        {orderMode === "TIME" ? (
          <div className="grid grid-cols-2 gap-3">
            <div className="rounded-control bg-background p-3">
              <div className="mb-2 flex items-center gap-2 text-text-muted">
                <Users size={16} />
                <p className="text-xs uppercase tracking-wide">Invitados</p>
              </div>

              <p className="text-lg font-semibold text-text-main">
                {guests || "-"}
              </p>
            </div>

            <div className="rounded-control bg-background p-3">
              <div className="mb-2 flex items-center gap-2 text-text-muted">
                <Clock size={16} />
                <p className="text-xs uppercase tracking-wide">Horas</p>
              </div>

              <p className="text-lg font-semibold text-text-main">
                {durationHours || "-"}
              </p>
            </div>

            <div className="col-span-2 rounded-control border border-primary/20 bg-background p-4">
              <p className="text-xs uppercase tracking-wide text-text-muted">
                Tragos estimados
              </p>

              <p className="mt-1 text-3xl font-semibold text-primary">
                {estimatedDrinks || "-"}
              </p>

              <p className="mt-2 text-xs leading-5 text-text-muted">
                Estimación basada en invitados y duración del evento.
              </p>
            </div>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3">
            <div className="rounded-control bg-background p-3">
              <p className="text-xs uppercase tracking-wide text-text-muted">
                Total tragos
              </p>

              <p className="mt-1 text-lg font-semibold text-text-main">
                {totalDrinks || "-"}
              </p>
            </div>

            <div className="rounded-control bg-background p-3">
              <p className="text-xs uppercase tracking-wide text-text-muted">
                Asignados
              </p>

              <p
                className={`mt-1 text-lg font-semibold ${
                  numericTotalDrinks > 0 &&
                  assignedDrinks === numericTotalDrinks
                    ? "text-success"
                    : "text-primary"
                }`}
              >
                {assignedDrinks} / {totalDrinks || "-"}
              </p>
            </div>
          </div>
        )}

        <div className="rounded-card border border-border-soft bg-background/40 p-4">
          <div className="flex items-center justify-between gap-3">
            <h3 className="text-xs font-semibold uppercase tracking-wide text-text-muted">
              Selección
            </h3>

            {onEditSelection && (
              <button
                type="button"
                onClick={onEditSelection}
                className="text-xs font-semibold text-primary underline-offset-4 hover:underline"
              >
                Editar
              </button>
            )}
          </div>

          {selectedCocktails.length === 0 ? (
            <p className="mt-3 text-sm text-text-muted">
              Todavía no seleccionaste cócteles.
            </p>
          ) : (
            <details className="group mt-3">
              <summary className="flex cursor-pointer list-none items-center justify-between gap-3">
                <div className="min-w-0">
                  <p className="font-semibold text-text-main">
                    {selectedCocktails.length} cócteles seleccionados
                  </p>

                  <p className="mt-1 text-xs text-text-muted">
                    Tocá para ver el detalle completo.
                  </p>
                </div>

                <span className="shrink-0 rounded-full border border-border-soft px-3 py-1 text-xs font-semibold text-primary">
                  <span className="group-open:hidden">Ver</span>
                  <span className="hidden group-open:inline">Ocultar</span>
                </span>
              </summary>

              <div className="mt-4 space-y-2">
                {selectedCocktails.map((cocktail) => (
                  <div
                    key={cocktail.cocktailId}
                    className="flex items-center justify-between gap-4 text-sm"
                  >
                    <span className="min-w-0 truncate text-text-main">
                      {cocktail.cocktailName}
                    </span>

                    <span className="shrink-0 text-primary">
                      {orderMode === "TIME"
                        ? getPriorityText(cocktail.weight)
                        : `${cocktail.quantity} tragos`}
                    </span>
                  </div>
                ))}
              </div>
            </details>
          )}
        </div>

        <div
          className={`rounded-control border px-3 py-2 text-sm ${
            isReady
              ? "border-success/40 bg-success/10 text-success"
              : "border-primary/30 bg-primary/10 text-primary"
          }`}
        >
          {isReady
            ? "La orden está lista para generarse."
            : "Completá los datos requeridos para generar la orden."}
        </div>
      </div>
    </Card>
  );
}
