import { Trash2 } from "lucide-react";

import { Input } from "../../../shared/components/ui/Input";
import type { SelectedOrderCocktail } from "../order.types";

type SelectedCocktailsListProps = {
  selectedCocktails: SelectedOrderCocktail[];
  onWeightChange: (cocktailId: number, weight: number) => void;
  onRemoveCocktail: (cocktailId: number) => void;
};

export function SelectedCocktailsList({
  selectedCocktails,
  onWeightChange,
  onRemoveCocktail,
}: SelectedCocktailsListProps) {
  if (selectedCocktails.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Todavía no seleccionaste cócteles para esta orden.
      </p>
    );
  }

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
              Peso: {cocktail.weight}
            </p>
          </div>

          <div className="flex items-end gap-3">
            <div className="w-28">
              <Input
                label="Peso"
                type="number"
                min="1"
                value={cocktail.weight}
                onChange={(event) =>
                  onWeightChange(
                    cocktail.cocktailId,
                    Number(event.target.value)
                  )
                }
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