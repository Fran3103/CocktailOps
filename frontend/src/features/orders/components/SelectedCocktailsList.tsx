import { Martini, Trash2 } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import type { OrderMode, SelectedOrderCocktail } from "../order.types";

type SelectedCocktailsListProps = {
  orderMode: OrderMode;
  selectedCocktails: SelectedOrderCocktail[];
  onWeightChange: (cocktailId: number, weight: number) => void;
  onQuantityChange: (cocktailId: number, quantity: number) => void;
  onRemoveCocktail: (cocktailId: number) => void;
};

function getInputLabel(orderMode: OrderMode) {
  return orderMode === "TIME" ? "Peso" : "Tragos";
}

function getInputValue(
  orderMode: OrderMode,
  cocktail: SelectedOrderCocktail,
) {
  return orderMode === "TIME" ? cocktail.weight : cocktail.quantity;
}

function getDescription(orderMode: OrderMode) {
  return orderMode === "TIME"
    ? "Participación relativa en el evento"
    : "Cantidad asignada para esta orden";
}

export function SelectedCocktailsList({
  orderMode,
  selectedCocktails,
  onWeightChange,
  onQuantityChange,
  onRemoveCocktail,
}: SelectedCocktailsListProps) {
  const inputLabel = getInputLabel(orderMode);

  function handleValueChange(cocktailId: number, value: string) {
    const numericValue = Number(value);

    if (orderMode === "TIME") {
      onWeightChange(cocktailId, numericValue);
      return;
    }

    onQuantityChange(cocktailId, numericValue);
  }

  if (selectedCocktails.length === 0) {
    return (
      <div className="rounded-card border border-dashed border-border-soft bg-background/30 px-4 py-8 text-center">
        <div className="mx-auto flex h-11 w-11 items-center justify-center rounded-control border border-border-soft bg-surface text-primary">
          <Martini size={20} />
        </div>

        <h4 className="mt-4 font-heading font-semibold text-text-main">
          Todavía no seleccionaste cócteles
        </h4>

        <p className="mx-auto mt-2 max-w-sm text-sm leading-6 text-text-muted">
          Usá una lista rápida o agregá cócteles desde el catálogo manual.
        </p>
      </div>
    );
  }

  return (
    <>
      <div className="space-y-3 md:hidden">
        {selectedCocktails.map((cocktail) => (
          <article
            key={cocktail.cocktailId}
            className="rounded-card border border-border-soft bg-background/30 p-4"
          >
            <div className="flex items-start gap-3">
              <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-control border border-border-soft bg-surface text-primary">
                <Martini size={18} />
              </div>

              <div className="min-w-0 flex-1">
                <h4 className="font-medium text-text-main">
                  {cocktail.cocktailName}
                </h4>

                <p className="mt-1 text-xs text-text-muted">
                  {getDescription(orderMode)}
                </p>
              </div>

              <button
                type="button"
                onClick={() => onRemoveCocktail(cocktail.cocktailId)}
                className="flex h-9 w-9 shrink-0 items-center justify-center rounded-control border border-border-soft bg-background text-danger transition hover:border-danger/60"
                aria-label={`Quitar ${cocktail.cocktailName}`}
              >
                <Trash2 size={16} />
              </button>
            </div>

            <label className="mt-4 block text-sm text-text-muted">
              {inputLabel}
            </label>

            <input
              type="number"
              min={1}
              value={getInputValue(orderMode, cocktail)}
              onChange={(event) =>
                handleValueChange(cocktail.cocktailId, event.target.value)
              }
              className="mt-2 w-full rounded-control border border-border bg-background px-3 py-2 text-sm font-semibold text-text-main outline-none focus:border-primary"
            />
          </article>
        ))}
      </div>

      <div className="hidden overflow-hidden rounded-card border border-border-soft bg-background/30 md:block">
        <div className="grid grid-cols-[minmax(0,1fr)_170px_52px] gap-3 border-b border-border-soft px-4 py-3 text-xs font-semibold uppercase tracking-wide text-text-muted">
          <span>Cóctel</span>
          <span>{inputLabel}</span>
          <span />
        </div>

        <div className="divide-y divide-border-soft">
          {selectedCocktails.map((cocktail) => (
            <article
              key={cocktail.cocktailId}
              className="grid grid-cols-[minmax(0,1fr)_170px_52px] items-center gap-3 px-4 py-3"
            >
              <div className="flex min-w-0 items-center gap-3">
                <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-control border border-border-soft bg-surface text-primary">
                  <Martini size={18} />
                </div>

                <div className="min-w-0">
                  <h4 className="truncate font-medium text-text-main">
                    {cocktail.cocktailName}
                  </h4>

                  <p className="text-xs text-text-muted">
                    {getDescription(orderMode)}
                  </p>
                </div>
              </div>

              <input
                type="number"
                min={1}
                value={getInputValue(orderMode, cocktail)}
                onChange={(event) =>
                  handleValueChange(cocktail.cocktailId, event.target.value)
                }
                className="w-full rounded-control border border-border bg-background px-3 py-2 text-sm font-semibold text-text-main outline-none focus:border-primary"
              />

              <Button
                type="button"
                variant="secondary"
                onClick={() => onRemoveCocktail(cocktail.cocktailId)}
                aria-label={`Quitar ${cocktail.cocktailName}`}
              >
                <Trash2 size={16} />
              </Button>
            </article>
          ))}
        </div>
      </div>
    </>
  );
}