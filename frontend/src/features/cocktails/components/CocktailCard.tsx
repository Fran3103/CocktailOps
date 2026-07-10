import { Martini } from "lucide-react";

import { Card } from "../../../shared/components/ui/Card";
import type { Cocktail } from "../cocktail.types";

type CocktailCardProps = {
  cocktail: Cocktail;
};

export function CocktailCard({ cocktail }: CocktailCardProps) {
  return (
    <Card className="overflow-hidden p-0">
      {cocktail.imageUrl ? (
        <img
          src={cocktail.imageUrl}
          alt={cocktail.imageAlt ?? cocktail.name}
          className="h-40 w-full object-cover sm:h-48"
        />
      ) : (
        <div className="flex h-40 items-center justify-center bg-surface sm:h-48">
          <Martini className="text-primary" size={44} />
        </div>
      )}

      <div className="p-4 sm:p-5">
        <h3 className="font-heading text-lg font-semibold text-text-main sm:text-xl">
          {cocktail.name}
        </h3>

        {cocktail.description && (
          <p className="mt-2 text-sm text-text-muted">
            {cocktail.description}
          </p>
        )}

        {cocktail.ingredients && cocktail.ingredients.length > 0 && (
          <div className="mt-4 flex flex-wrap gap-2">
            {cocktail.ingredients.slice(0, 4).map((ingredient, index) => (
              <span
                key={`${ingredient.productId ?? ingredient.productName}-${index}`}
                className="rounded-full border border-border-soft px-2 py-1 text-[11px] uppercase tracking-wide text-text-muted"
              >
                {ingredient.productName ?? "Ingrediente"}
              </span>
            ))}
          </div>
        )}

        <button
          type="button"
          className="mt-5 text-sm font-semibold text-primary hover:text-primary-soft"
        >
          Ver detalle
        </button>
      </div>
    </Card>
  );
}