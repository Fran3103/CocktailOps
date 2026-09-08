import { useEffect, useState } from "react";

import { cocktailService } from "../../cocktails/cocktailService";
import type { Cocktail } from "../../cocktails/cocktail.types";

export function useOrderCocktailsCatalog() {
  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [isLoadingCocktails, setIsLoadingCocktails] = useState(true);
  const [cocktailsError, setCocktailsError] = useState<string | null>(null);

  useEffect(() => {
    let ignore = false;

    async function fetchCocktails() {
      try {
        const data = await cocktailService.getAll();

        if (!ignore) {
          setCocktails(data);
          setCocktailsError(null);
        }
      } catch {
        if (!ignore) {
          setCocktailsError("No se pudieron cargar los cócteles.");
        }
      } finally {
        if (!ignore) {
          setIsLoadingCocktails(false);
        }
      }
    }

    void fetchCocktails();

    return () => {
      ignore = true;
    };
  }, []);

  return {
    cocktails,
    isLoadingCocktails,
    cocktailsError,
  };
}