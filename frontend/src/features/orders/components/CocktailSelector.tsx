import { Martini, Plus } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { Cocktail } from "../../cocktails/cocktail.types";
import type { SelectedOrderCocktail } from "../order.types";

type CocktailSelectorProps = {
  cocktails: Cocktail[];
  selectedCocktails: SelectedOrderCocktail[];
  onAddCocktail: (cocktail: Cocktail) => void;
};

function getCocktailId(cocktail: Cocktail) {
  return cocktail.id;
}

function isSelected(
  cocktail: Cocktail,
  selectedCocktails: SelectedOrderCocktail[]
) {
  return selectedCocktails.some(
    (selectedCocktail) => selectedCocktail.cocktailId === getCocktailId(cocktail)
  );
}

export function CocktailSelector({
  cocktails,
  selectedCocktails,
  onAddCocktail,
}: CocktailSelectorProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
      {cocktails.map((cocktail) => {
        const selected = isSelected(cocktail, selectedCocktails);

        return (
          <Card key={cocktail.id} className="flex flex-col gap-4">
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary">
                <Martini size={20} />
              </div>

              <div>
                <h3 className="font-heading text-lg font-semibold text-text-main">
                  {cocktail.name}
                </h3>

                {cocktail.description && (
                  <p className="mt-1 text-sm text-text-muted">
                    {cocktail.description}
                  </p>
                )}
              </div>
            </div>

            <Button
              type="button"
              variant={selected ? "secondary" : "primary"}
              disabled={selected}
              onClick={() => onAddCocktail(cocktail)}
            >
              <span className="flex items-center justify-center gap-2">
                <Plus size={16} />
                {selected ? "Agregado" : "Agregar"}
              </span>
            </Button>
          </Card>
        );
      })}
    </div>
  );
}