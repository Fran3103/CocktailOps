import { useMemo, useState, type ReactNode } from "react";
import { CheckCircle2, Martini, Plus, Search } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { Cocktail } from "../../cocktails/cocktail.types";
import type { SelectedOrderCocktail } from "../order.types";

type CocktailSelectorProps = {
  cocktails: Cocktail[];
  selectedCocktails: SelectedOrderCocktail[];
  onAddCocktail: (cocktail: Cocktail) => void;
  showHeader?: boolean;
  footer?: ReactNode;
  embedded?: boolean;
};

const preparationLabels: Record<string, string> = {
  DIRECT: "Directo",
  SHAKEN: "Batido",
  STIRRED: "Refrescado",
  FROZEN: "Frozen",
};

function getCocktailId(cocktail: Cocktail) {
  return cocktail.id;
}

function isSelected(
  cocktail: Cocktail,
  selectedCocktails: SelectedOrderCocktail[],
) {
  return selectedCocktails.some(
    (selectedCocktail) =>
      selectedCocktail.cocktailId === getCocktailId(cocktail),
  );
}

function getPreparationLabel(preparationType: Cocktail["preparationType"]) {
  if (!preparationType) {
    return "Sin tipo";
  }

  return preparationLabels[preparationType] ?? preparationType;
}

export function CocktailSelector({
  cocktails,
  selectedCocktails,
  onAddCocktail,
  showHeader = true,
  embedded = false,
  footer,
}: CocktailSelectorProps) {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredCocktails = useMemo(() => {
    const normalizedSearch = searchTerm.toLowerCase().trim();

    if (!normalizedSearch) {
      return cocktails;
    }

    return cocktails.filter((cocktail) => {
      const name = cocktail.name.toLowerCase();
      const description = cocktail.description?.toLowerCase() ?? "";
      const preparationType = cocktail.preparationType?.toLowerCase() ?? "";
      const preparationLabel = getPreparationLabel(
        cocktail.preparationType,
      ).toLowerCase();

      const ingredients =
        cocktail.ingredients
          ?.map((ingredient) => ingredient.productName ?? "")
          .join(" ")
          .toLowerCase() ?? "";

      return (
        name.includes(normalizedSearch) ||
        description.includes(normalizedSearch) ||
        preparationType.includes(normalizedSearch) ||
        preparationLabel.includes(normalizedSearch) ||
        ingredients.includes(normalizedSearch)
      );
    });
  }, [cocktails, searchTerm]);

  const content = (
    <div className="space-y-4">
      {showHeader && (
        <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <h3 className="font-heading text-lg font-semibold text-text-main">
              Selección manual
            </h3>

            <p className="mt-1 text-sm text-text-muted">
              Buscá y agregá cócteles individuales a la orden.
            </p>
          </div>
        </div>
      )}

      <div className="relative w-full">
        <Search
          size={17}
          className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted"
        />

        <input
          type="search"
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.target.value)}
          placeholder="Buscar cóctel..."
          className="w-full rounded-control border border-border bg-background py-2 pl-10 pr-4 text-sm text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />
      </div>

      <div className="rounded-card border border-border-soft bg-background/30">
        <div className="flex flex-col gap-1 border-b border-border-soft px-4 py-3 text-sm sm:flex-row sm:items-center sm:justify-between">
          <p className="text-text-muted">
            Mostrando {filteredCocktails.length} de {cocktails.length}
          </p>

          <p className="text-primary">
            {selectedCocktails.length} seleccionados
          </p>
        </div>

        {filteredCocktails.length === 0 ? (
          <div className="px-4 py-6">
            <p className="text-sm text-text-muted">
              No se encontraron cócteles para esa búsqueda.
            </p>
          </div>
        ) : (
          <div className="divide-y divide-border-soft">
            {filteredCocktails.map((cocktail) => {
              const selected = isSelected(cocktail, selectedCocktails);
              const preparationLabel = getPreparationLabel(
                cocktail.preparationType,
              );

              return (
                <article
                  key={cocktail.id}
                  className="flex flex-col gap-3 px-4 py-3 transition hover:bg-surface-bright/40 sm:flex-row sm:items-center sm:justify-between"
                >
                  <div className="flex min-w-0 items-start gap-3">
                    <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-control border border-border-soft bg-surface text-primary">
                      <Martini size={18} />
                    </div>

                    <div className="min-w-0">
                      <div className="flex flex-wrap items-center gap-2">
                        <h4 className="font-medium text-text-main">
                          {cocktail.name}
                        </h4>

                        <span className="rounded-full border border-border-soft bg-background/60 px-2 py-0.5 text-[11px] uppercase tracking-wide text-primary">
                          {preparationLabel}
                        </span>
                      </div>

                      {cocktail.description && (
                        <p className="mt-1 line-clamp-2 text-sm leading-6 text-text-muted">
                          {cocktail.description}
                        </p>
                      )}

                      {cocktail.ingredients &&
                        cocktail.ingredients.length > 0 && (
                          <p className="mt-1 line-clamp-1 text-xs text-text-muted">
                            {cocktail.ingredients
                              .slice(0, 4)
                              .map(
                                (ingredient) =>
                                  ingredient.productName ?? "Ingrediente",
                              )
                              .join(" · ")}
                            {cocktail.ingredients.length > 4
                              ? ` · +${cocktail.ingredients.length - 4}`
                              : ""}
                          </p>
                        )}
                    </div>
                  </div>

                  <Button
                    type="button"
                    variant={selected ? "secondary" : "primary"}
                    disabled={selected}
                    onClick={() => onAddCocktail(cocktail)}
                    fullWidth
                    className="sm:w-auto sm:shrink-0"
                  >
                    <span className="flex items-center justify-center gap-2">
                      {selected ? (
                        <>
                          <CheckCircle2 size={16} />
                          Agregado
                        </>
                      ) : (
                        <>
                          <Plus size={16} />
                          Agregar
                        </>
                      )}
                    </span>
                  </Button>
                </article>
              );
            })}
          </div>
        )}
        {footer && (
          <div className="border-t border-border-soft px-4 py-3">{footer}</div>
        )}
      </div>
    </div>
  );

  if (embedded) {
    return content;
  }

  return <Card className="space-y-4">{content}</Card>;
}
