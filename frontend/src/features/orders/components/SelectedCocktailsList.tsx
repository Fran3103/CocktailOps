import { Trash2 } from "lucide-react";

import { Input } from "../../../shared/components/ui/Input";
import type { OrderMode, SelectedOrderCocktail } from "../order.types";

type SelectedCocktailsListProps = {
  orderMode: OrderMode;
  selectedCocktails: SelectedOrderCocktail[];
  onWeightChange: (cocktailId: number, weight: number) => void;
  onQuantityChange: (cocktailId: number, quantity: number) => void;
  onRemoveCocktail: (cocktailId: number) => void;
};

export function SelectedCocktailsList({
  orderMode,
  selectedCocktails,
  onWeightChange,
  onQuantityChange,
  onRemoveCocktail,
}: SelectedCocktailsListProps) {
  if (selectedCocktails.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Todavía no seleccionaste cócteles para esta orden.
      </p>
    );
  }

  const isTimeMode = orderMode === "TIME";

  return (
    <div className="space-y-3">
      {selectedCocktails.map((cocktail) => (
        <div
          key={cocktail.cocktailId}
          className="flex flex-col gap-3 rounded-control border border-border-soft bg-background p-4 sm:flex-row sm:items-center sm:justify-between"
        >
          <div>
            <p className="font-medium text-text-main">
              {cocktail.cocktailName}
            </p>

            <p className="text-sm text-text-muted">
              {isTimeMode
                ? `Peso: ${cocktail.weight}`
                : `Cantidad: ${cocktail.quantity}`}
            </p>
          </div>

          <div className="flex items-end gap-3">
            <div className="w-32">
              <Input
                label={isTimeMode ? "Peso" : "Cantidad"}
                type="number"
                min="1"
                value={isTimeMode ? cocktail.weight : cocktail.quantity}
                onChange={(event) => {
                  const value = Number(event.target.value);

                  if (isTimeMode) {
                    onWeightChange(cocktail.cocktailId, value);
                  } else {
                    onQuantityChange(cocktail.cocktailId, value);
                  }
                }}
              />
            </div>

            <button
              type="button"
              onClick={() => onRemoveCocktail(cocktail.cocktailId)}
              className="rounded-control border border-border-soft p-2 text-danger hover:bg-surface-bright"
              aria-label="Quitar cóctel"
            >
              <Trash2 size={18} />
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}