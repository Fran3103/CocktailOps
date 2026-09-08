import { Martini } from "lucide-react";

import { Card } from "../../../shared/components/ui/Card";
import type { Cocktail, CocktailPreparationType } from "../cocktail.types";

type CocktailCardProps = {
  cocktail: Cocktail;
};

const preparationLabels: Record<CocktailPreparationType, string> = {
  DIRECT: "Directo",
  SHAKEN: "Batido",
  STIRRED: "Refrescado",
  FROZEN: "Frozen",
};

function getPreparationLabel(preparationType?: CocktailPreparationType | null) {
  if (!preparationType) {
    return "Sin clasificar";
  }

  return preparationLabels[preparationType] ?? "Sin clasificar";
}

export function CocktailCard({ cocktail }: CocktailCardProps) {
  const ingredients = cocktail.ingredients ?? [];
  const preparationLabel = getPreparationLabel(cocktail.preparationType);

  return (
    <Card className="flex h-45 flex-col overflow-hidden p-4 transition hover:border-primary/40 sm:p-5">
      <div className="flex items-start gap-3">
        <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control border border-border-soft bg-background/60 text-primary">
          <Martini size={20} />
        </div>

        <div className="min-w-0 flex-1">
          <div className="flex flex-col gap-2 sm:flex-row sm:items-start sm:justify-between">
            <h3 className="line-clamp-1 font-heading text-lg font-semibold text-text-main">
              {cocktail.name}
            </h3>

            <span className="w-fit rounded-full border border-border-soft bg-background/40 px-2 py-1 text-[11px] font-medium uppercase tracking-wide text-primary">
              {preparationLabel}
            </span>
          </div>

          {cocktail.description && (
            <p className="mt-1 line-clamp-2 text-sm leading-6 text-text-muted">
              {cocktail.description}
            </p>
          )}
        </div>
      </div>

      {ingredients.length > 0 && (
        <div>
          <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-text-muted">
            Ingredientes
          </p>

          <div className="flex flex-wrap gap-2">
            {ingredients.slice(0, 3).map((ingredient, index) => (
              <span
                key={`${ingredient.productId ?? ingredient.productName}-${index}`}
                className="rounded-full border border-border-soft bg-background/40 px-2 py-1 text-[11px] text-text-muted"
              >
                {ingredient.productName ?? "Ingrediente"}
              </span>
            ))}

            {ingredients.length > 3 && (
              <span className="rounded-full border border-border-soft bg-background/40 px-2 py-1 text-[11px] text-primary">
                +{ingredients.length - 3}
              </span>
            )}
          </div>
        </div>
      )}
    </Card>
  );
}