import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { cocktailService } from "./cocktailService";
import type { Cocktail, CocktailPreparationType } from "./cocktail.types";
import { CocktailGrid } from "./components/CocktailGrid";
import { CocktailSearch } from "./components/CocktailSearch";

const COCKTAILS_PER_PAGE = 10;

type PreparationFilter = {
  id: "ALL" | "DIRECT" | "SHAKEN" | "STIRRED" | "FROZEN";
  label: string;
  types: CocktailPreparationType[] | null;
};

const preparationFilters: PreparationFilter[] = [
  {
    id: "ALL",
    label: "Todos",
    types: null,
  },
  {
    id: "DIRECT",
    label: "Directos",
    types: ["DIRECT"],
  },
  {
    id: "SHAKEN",
    label: "Batidos",
    types: ["SHAKEN"],
  },
  {
    id: "STIRRED",
    label: "Refrescados",
    types: ["STIRRED"],
  },
  {
    id: "FROZEN",
    label: "Frozen",
    types: ["FROZEN"],
  },
];

export function CocktailsPage() {
  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedFilterId, setSelectedFilterId] =
    useState<PreparationFilter["id"]>("ALL");
  const [currentPage, setCurrentPage] = useState(1);

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const selectedFilter =
    preparationFilters.find((filter) => filter.id === selectedFilterId) ??
    preparationFilters[0];

  useEffect(() => {
    let ignore = false;

    async function fetchCocktails() {
      try {
        const data = await cocktailService.getAll();

        if (!ignore) {
          setCocktails(data);
          setError(null);
        }
      } catch {
        if (!ignore) {
          setError("No se pudieron cargar los cócteles.");
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    }

    void fetchCocktails();

    return () => {
      ignore = true;
    };
  }, []);

  async function handleRetry() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await cocktailService.getAll();
      setCocktails(data);
    } catch {
      setError("No se pudieron cargar los cócteles.");
    } finally {
      setIsLoading(false);
    }
  }

  const filteredCocktails = useMemo(() => {
    const normalizedSearch = searchTerm.toLowerCase().trim();

    return cocktails.filter((cocktail) => {
      const name = cocktail.name.toLowerCase();
      const description = cocktail.description?.toLowerCase() ?? "";
      const ingredients =
        cocktail.ingredients
          ?.map((ingredient) => ingredient.productName ?? "")
          .join(" ")
          .toLowerCase() ?? "";

      const matchesSearch =
        !normalizedSearch ||
        name.includes(normalizedSearch) ||
        description.includes(normalizedSearch) ||
        ingredients.includes(normalizedSearch);

      const matchesPreparation =
        selectedFilter.types === null ||
        (cocktail.preparationType != null &&
          selectedFilter.types.includes(cocktail.preparationType));

      return matchesSearch && matchesPreparation;
    });
  }, [cocktails, searchTerm, selectedFilter]);

  const totalPages = Math.max(
    1,
    Math.ceil(filteredCocktails.length / COCKTAILS_PER_PAGE),
  );

  function handleSearchChange(value: string) {
    setSearchTerm(value);
    setCurrentPage(1);
  }

  function handlePreparationChange(filterId: PreparationFilter["id"]) {
    setSelectedFilterId(filterId);
    setCurrentPage(1);
  }

  const safeCurrentPage = Math.min(currentPage, totalPages);

  const visibleCocktails = useMemo(() => {
    const startIndex = (safeCurrentPage - 1) * COCKTAILS_PER_PAGE;
    const endIndex = startIndex + COCKTAILS_PER_PAGE;

    return filteredCocktails.slice(startIndex, endIndex);
  }, [filteredCocktails, safeCurrentPage]);

  const firstVisibleCocktail =
    filteredCocktails.length === 0
      ? 0
      : (safeCurrentPage - 1) * COCKTAILS_PER_PAGE + 1;

  const lastVisibleCocktail = Math.min(
    safeCurrentPage * COCKTAILS_PER_PAGE,
    filteredCocktails.length,
  );

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <PageHeader
          title="Cócteles"
          description="Explorá el catálogo de cócteles disponibles para planificar eventos."
        />

        <CocktailSearch value={searchTerm} onChange={handleSearchChange} />
      </div>

      <Card className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h2 className="font-heading text-lg font-semibold text-text-main">
            Tipo de preparación
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Filtrá el catálogo por directos, batidos, refrescados y frozen.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          {preparationFilters.map((filter) => (
            <Button
              key={filter.id}
              type="button"
              variant={selectedFilterId === filter.id ? "primary" : "secondary"}
              onClick={() => handlePreparationChange(filter.id)}
              aria-pressed={selectedFilterId === filter.id}
            >
              {filter.label}
            </Button>
          ))}
        </div>
      </Card>

      {isLoading && (
        <Card>
          <p className="text-text-muted">Cargando cócteles...</p>
        </Card>
      )}

      {error && (
        <Card>
          <p className="text-danger">{error}</p>

          <Button type="button" className="mt-4" onClick={handleRetry}>
            Reintentar
          </Button>
        </Card>
      )}

      {!isLoading && !error && filteredCocktails.length === 0 && (
        <Card>
          <p className="text-text-muted">
            No se encontraron cócteles para mostrar.
          </p>
        </Card>
      )}

      {!isLoading && !error && filteredCocktails.length > 0 && (
        <>
          <CocktailGrid
            cocktails={visibleCocktails}
            itemsPerPage={COCKTAILS_PER_PAGE}
          />

          <div className="flex flex-col gap-3 rounded-card border border-border-soft bg-surface-soft px-4 py-3 sm:flex-row sm:items-center sm:justify-between">
            <p className="text-sm text-text-muted">
              Mostrando {firstVisibleCocktail}-{lastVisibleCocktail} de{" "}
              {filteredCocktails.length} cócteles filtrados. Total del catálogo:{" "}
              {cocktails.length}.
            </p>

            {totalPages > 1 && (
              <div className="flex items-center gap-2">
                <Button
                  type="button"
                  variant="secondary"
                  disabled={safeCurrentPage === 1}
                  onClick={() =>
                    setCurrentPage((page) => Math.max(1, page - 1))
                  }
                >
                  Anterior
                </Button>

                <span className="text-sm text-text-muted">
                  Página {safeCurrentPage} de {totalPages}
                </span>

                <Button
                  type="button"
                  variant="secondary"
                  disabled={safeCurrentPage === totalPages}
                  onClick={() =>
                    setCurrentPage((page) => Math.min(totalPages, page + 1))
                  }
                >
                  Siguiente
                </Button>
              </div>
            )}
          </div>
        </>
      )}
    </section>
  );
}
