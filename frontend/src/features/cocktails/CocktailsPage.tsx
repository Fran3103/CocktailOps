import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { cocktailService } from "./cocktailService";
import type { Cocktail } from "./cocktail.types";
import { CocktailGrid } from "./components/CocktailGrid";
import { CocktailSearch } from "./components/CocktailSearch";

export function CocktailsPage() {
  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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

    if (!normalizedSearch) {
      return cocktails;
    }

    return cocktails.filter((cocktail) => {
      const name = cocktail.name.toLowerCase();
      const description = cocktail.description?.toLowerCase() ?? "";

      return (
        name.includes(normalizedSearch) ||
        description.includes(normalizedSearch)
      );
    });
  }, [cocktails, searchTerm]);

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <PageHeader
          title="Cócteles"
          description="Explorá el catálogo de cócteles disponibles para planificar eventos."
        />

        <CocktailSearch value={searchTerm} onChange={setSearchTerm} />
      </div>

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
          <CocktailGrid cocktails={filteredCocktails} />

          <p className="text-sm text-text-muted">
            Mostrando {filteredCocktails.length} de {cocktails.length} cócteles.
          </p>
        </>
      )}
    </section>
  );
}