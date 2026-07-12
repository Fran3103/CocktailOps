import { Martini } from "lucide-react";

import type { OrderCocktailResponse } from "../order.types";

type OrderCocktailsTableProps = {
  cocktails: OrderCocktailResponse[];
};

export function OrderCocktailsTable({ cocktails }: OrderCocktailsTableProps) {
  if (cocktails.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Esta orden no tiene cócteles calculados.
      </p>
    );
  }

  return (
    <div className="overflow-hidden rounded-card border border-border-soft bg-surface-soft">
      <div className="overflow-x-auto">
        <table className="w-full min-w-520px border-collapse">
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Cóctel
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Cantidad calculada
              </th>
            </tr>
          </thead>

          <tbody>
            {cocktails.map((cocktail) => (
              <tr
                key={cocktail.cocktailId}
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
  );
}