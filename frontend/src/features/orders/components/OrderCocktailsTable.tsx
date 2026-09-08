import { Martini } from "lucide-react";

import type { OrderCocktailResponse } from "../order.types";

type OrderCocktailsTableProps = {
  cocktails: OrderCocktailResponse[];
};

function getCocktailKey(cocktail: OrderCocktailResponse, index: number) {
  return cocktail.cocktailId ?? `${cocktail.cocktailName}-${index}`;
}

export function OrderCocktailsTable({ cocktails }: OrderCocktailsTableProps) {
  if (cocktails.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Esta orden no tiene cócteles calculados.
      </p>
    );
  }

  return (
    <div className="rounded-card border border-border-soft bg-surface-soft">
      <div className="space-y-3 p-4 lg:hidden">
        {cocktails.map((cocktail, index) => (
          <article
            key={getCocktailKey(cocktail, index)}
            className="rounded-card border border-border-soft bg-background/30 p-4"
          >
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary">
                <Martini size={18} />
              </div>

              <div className="min-w-0 flex-1">
                <h3 className="font-semibold text-text-main">
                  {cocktail.cocktailName}
                </h3>

                <p className="mt-1 text-xs text-text-muted">
                  ID #{cocktail.cocktailId}
                </p>
              </div>
            </div>

            <div className="mt-4 rounded-control border border-border-soft bg-background/40 px-3 py-2">
              <p className="text-xs uppercase tracking-wide text-text-muted">
                Cantidad calculada
              </p>

              <p className="mt-1 text-lg font-semibold text-primary">
                {cocktail.quantity} tragos
              </p>
            </div>
          </article>
        ))}
      </div>

      <div className="hidden overflow-hidden rounded-card lg:block">
        <div className="overflow-x-auto">
          <table className="w-full min-w-130 border-separate border-spacing-0">
            <thead>
              <tr>
                <th className="rounded-tl-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Cóctel
                </th>

                <th className="rounded-tr-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Cantidad calculada
                </th>
              </tr>
            </thead>

            <tbody>
              {cocktails.map((cocktail, index) => (
                <tr
                  key={getCocktailKey(cocktail, index)}
                  className="border-b border-border-soft last:border-0"
                >
                  <td className="px-4 py-4">
                    <div className="flex items-center gap-3">
                      <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-background text-primary">
                        <Martini size={18} />
                      </div>

                      <div>
                        <p className="font-medium text-text-main">
                          {cocktail.cocktailName}
                        </p>

                        <p className="text-xs text-text-muted">
                          ID #{cocktail.cocktailId}
                        </p>
                      </div>
                    </div>
                  </td>

                  <td className="px-4 py-4 text-sm font-semibold text-primary">
                    {cocktail.quantity} tragos
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}