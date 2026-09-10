import { Martini, RefreshCcw } from "lucide-react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { ROUTES } from "../../../shared/constants/routes";
import type { Cocktail } from "../../cocktails/cocktail.types";
import { cocktailService } from "../../cocktails/cocktailService";

const COCKTAILS_LIMIT = 6;

const popularEventCocktailNames = [
  "Fernet Cola",
  "Gin Tonic",
  "Aperol Spritz",
  "Negroni",
  "Mojito",
  "Caipiroska",
  "Daiquiri",
  "Caipirinha",
  "Cuba Libre",
];

function normalizeCocktailName(name: string) {
  return name.trim().toLowerCase();
}

function removeDuplicatedCocktails(cocktails: Cocktail[]) {
  const seenNames = new Set<string>();

  return cocktails.filter((cocktail) => {
    const normalizedName = normalizeCocktailName(cocktail.name);

    if (seenNames.has(normalizedName)) {
      return false;
    }

    seenNames.add(normalizedName);
    return true;
  });
}

function sortPopularCocktailsFirst(cocktails: Cocktail[]) {
  const priorityByName = new Map(
    popularEventCocktailNames.map((name, index) => [
      normalizeCocktailName(name),
      index,
    ]),
  );

  return [...cocktails].sort((a, b) => {
    const priorityA = priorityByName.get(normalizeCocktailName(a.name));
    const priorityB = priorityByName.get(normalizeCocktailName(b.name));

    if (priorityA != null && priorityB != null) {
      return priorityA - priorityB;
    }

    if (priorityA != null) {
      return -1;
    }

    if (priorityB != null) {
      return 1;
    }

    return a.name.localeCompare(b.name, "es", { sensitivity: "base" });
  });
}

async function getCocktailsPreview(): Promise<Cocktail[]> {
  const data = await cocktailService.getAll();
  const uniqueCocktails = removeDuplicatedCocktails(data);
  const orderedCocktails = sortPopularCocktailsFirst(uniqueCocktails);

  return orderedCocktails.slice(0, COCKTAILS_LIMIT);
}

export function CocktailsPreview() {
  const navigate = useNavigate();

  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  async function loadCocktails() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await getCocktailsPreview();
      setCocktails(data);
    } catch {
      setError("No se pudieron cargar los cócteles sugeridos.");
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    let isMounted = true;

    async function loadInitialCocktails() {
      try {
        const data = await getCocktailsPreview();

        if (!isMounted) return;

        setCocktails(data);
      } catch {
        if (!isMounted) return;

        setError("No se pudieron cargar los cócteles sugeridos.");
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    void loadInitialCocktails();

    return () => {
      isMounted = false;
    };
  }, []);

  if (isLoading) {
    return (
      <Card className="border-border-soft bg-surface-soft/80">
        <div className="flex items-center gap-3 text-text-muted">
          <RefreshCcw size={18} className="animate-spin" />
          <p>Cargando cócteles sugeridos...</p>
        </div>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="border-danger/30 bg-surface-soft/80">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <p className="text-sm text-danger">{error}</p>

          <Button type="button" variant="secondary" onClick={loadCocktails}>
            Reintentar
          </Button>
        </div>
      </Card>
    );
  }

  if (cocktails.length === 0) {
    return (
      <Card className="border-border-soft bg-surface-soft/80">
        <div>
          <h2 className="text-lg font-semibold text-text-main">
            Cócteles sugeridos
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Todavía no hay cócteles cargados para calcular órdenes.
          </p>
        </div>
      </Card>
    );
  }

  return (
    <section className="space-y-4">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h2 className="text-xl font-semibold text-text-main">
            Cócteles populares para eventos
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Selección orientativa de tragos frecuentes para usar como punto de
            partida.
          </p>
        </div>

        <Button
          type="button"
          variant="secondary"
          onClick={() => navigate(ROUTES.cocktails)}
        >
          Ver catálogo completo
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {cocktails.map((cocktail) => (
          <Card
            key={cocktail.id}
            className="flex h-full flex-col gap-4 border-border-soft bg-surface-soft/80"
          >
            <div className="flex items-start gap-3">
              <div className="rounded-control border border-border-soft bg-background/40 p-2 text-primary">
                <Martini size={18} />
              </div>

              <div>
                <h3 className="font-semibold text-text-main">
                  {cocktail.name}
                </h3>

                <p className="mt-1 line-clamp-2 text-sm leading-6 text-text-muted">
                  {cocktail.description ||
                    "Cóctel disponible para calcular órdenes."}
                </p>
              </div>
            </div>

            {cocktail.ingredients && cocktail.ingredients.length > 0 && (
              <p className="mt-auto text-xs text-text-muted">
                {cocktail.ingredients.length} insumo
                {cocktail.ingredients.length === 1 ? "" : "s"} asociado
                {cocktail.ingredients.length === 1 ? "" : "s"}
              </p>
            )}
          </Card>
        ))}
      </div>
    </section>
  );
}