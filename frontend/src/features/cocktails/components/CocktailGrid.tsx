import type { Cocktail } from "../cocktail.types";
import { CocktailCard } from "./CocktailCard";

type CocktailGridProps = {
  cocktails: Cocktail[];
};

export function CocktailGrid({ cocktails }: CocktailGridProps) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
      {cocktails.map((cocktail) => (
        <CocktailCard key={cocktail.id} cocktail={cocktail} />
      ))}
    </div>
  );
}