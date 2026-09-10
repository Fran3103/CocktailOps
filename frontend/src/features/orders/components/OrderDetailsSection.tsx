import { Card } from "../../../shared/components/ui/Card";
import { DrinksDetailsForm } from "./DrinksDetailsForm";
import { EventDetailsForm } from "./EventDetailsForm";
import { OrderModeSelector } from "./OrderModeSelector";
import { Button } from "../../../shared/components/ui/Button";
import { StepHeader } from "./StepHeader";
import type { OrderMode } from "../order.types";

type OrderDetailsSectionProps = {
  orderMode: OrderMode;
  guests: string;
  durationHours: string;
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktailsCount: number;
  onOrderModeChange: (mode: OrderMode) => void;
  onGuestsChange: (value: string) => void;
  onDurationHoursChange: (value: string) => void;
  onTotalDrinksChange: (value: string) => void;
  onDistributeEqually: () => void;
  onNext: () => void;
  error?: string | null;
};

export function OrderDetailsSection({
  orderMode,
  guests,
  durationHours,
  totalDrinks,
  assignedDrinks,
  selectedCocktailsCount,
  onOrderModeChange,
  onGuestsChange,
  onDurationHoursChange,
  onTotalDrinksChange,
  onDistributeEqually,
  onNext,
  error,
}: OrderDetailsSectionProps) {
  return (
    <Card className="space-y-5">
      <StepHeader
        step="Paso 1"
        title="Configurá la orden"
        description="Elegí el tipo de cálculo y cargá los datos principales del evento."
      />

      <OrderModeSelector value={orderMode} onChange={onOrderModeChange} />

      <div className="rounded-card border border-border-soft bg-background/40 p-4">
        <div className="mb-4">
          <h3 className="font-heading text-lg font-semibold text-text-main">
            {orderMode === "TIME"
              ? "Datos del evento"
              : "Cantidad total de tragos"}
          </h3>

          <p className="mt-1 text-sm text-text-muted">
            {orderMode === "TIME"
             ? "Calculá la orden según invitados, duración y prioridad de cada cóctel."
              : "Calculá la orden según una cantidad final de tragos y la distribución por cóctel."}
          </p>
        </div>

        {orderMode === "TIME" ? (
          <EventDetailsForm
            guests={guests}
            durationHours={durationHours}
            onGuestsChange={onGuestsChange}
            onDurationHoursChange={onDurationHoursChange}
          />
        ) : (
          <DrinksDetailsForm
            totalDrinks={totalDrinks}
            assignedDrinks={assignedDrinks}
            selectedCocktailsCount={selectedCocktailsCount}
            onTotalDrinksChange={onTotalDrinksChange}
            onDistributeEqually={onDistributeEqually}
          />
        )}
        {error && (
          <div className="rounded-card border mt-2 border-danger/40 bg-danger/5 p-3">
            <p className="text-sm text-danger">{error}</p>
          </div>
        )}
      </div>
      <div className="flex justify-end">
        <Button type="button" onClick={onNext}>
          Siguiente: elegir cócteles
        </Button>
      </div>
    </Card>
  );
}
