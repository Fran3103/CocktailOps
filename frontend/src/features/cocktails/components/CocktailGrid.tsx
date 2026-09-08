import type { Cocktail } from "../cocktail.types";
import { CocktailCard } from "./CocktailCard";

type CocktailGridProps = {
  cocktails: Cocktail[];
  itemsPerPage?: number;
};

export function CocktailGrid({
  cocktails,
  itemsPerPage = cocktails.length,
}: CocktailGridProps) {
  const emptySlots = Math.max(0, itemsPerPage - cocktails.length);

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 2xl:grid-cols-3">
      {cocktails.map((cocktail) => (
        <CocktailCard key={cocktail.id} cocktail={cocktail} />
      ))}

      {Array.from({ length: emptySlots }).map((_, index) => (
        <div
          key={`empty-cocktail-slot-${index}`}
          aria-hidden="true"
          className="hidden h-45 md:block"
        />
      ))}
    </div>
  );
}