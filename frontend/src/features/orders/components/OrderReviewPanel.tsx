import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { CalculationNotice } from "./CalculationNotice";
import { OrderSummaryPanel } from "./OrderSummaryPanel";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  SelectedOrderCocktail,
} from "../order.types";

type OrderReviewPanelProps = {
  isAuthenticated: boolean;
  isSubmitting: boolean;
  submitError: string | null;
  orderMode: OrderMode;
  guests: string;
  durationHours: string;
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktails: SelectedOrderCocktail[];
  payload: CreateTimeOrderRequest | CreateDrinksOrderRequest | null;
  onCreateOrder: () => void;
  onPrevious: () => void;
};

export function OrderReviewPanel({
  isAuthenticated,
  isSubmitting,
  submitError,
  orderMode,
  guests,
  durationHours,
  totalDrinks,
  assignedDrinks,
  selectedCocktails,
  payload,
  onCreateOrder,
  onPrevious,
}: OrderReviewPanelProps) {
  return (
    <aside className="space-y-4 xl:sticky xl:top-8 xl:self-start">
      <OrderSummaryPanel
        orderMode={orderMode}
        guests={guests}
        durationHours={durationHours}
        totalDrinks={totalDrinks}
        assignedDrinks={assignedDrinks}
        selectedCocktails={selectedCocktails}
        payload={payload}
        onEditSelection={onPrevious}
      />

      <CalculationNotice />

      {submitError && (
        <Card className="border-danger/40 bg-danger/5">
          <p className="text-sm text-danger">{submitError}</p>
        </Card>
      )}
      <Button
        type="button"
        fullWidth
        className="py-3 text-base font-semibold"
        onClick={onCreateOrder}
        disabled={isSubmitting}
      >
        {isSubmitting
          ? "Generando orden..."
          : isAuthenticated
            ? "Crear y guardar orden"
            : "Generar orden"}
      </Button>
      <Button type="button" variant="secondary" fullWidth onClick={onPrevious}>
        Volver a cócteles
      </Button>
    </aside>
  );
}
