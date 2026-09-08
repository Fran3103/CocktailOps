import { apiClient } from "../../api/apiClient";
import type { Cocktail } from "./cocktail.types";

function sortCocktailsByName(cocktails: Cocktail[]) {
  return [...cocktails].sort((a, b) =>
    a.name.localeCompare(b.name, "es", { sensitivity: "base" }),
  );
}

async function getAll(): Promise<Cocktail[]> {
  const response = await apiClient.get<Cocktail[]>("/cocktails");

  return sortCocktailsByName(response.data);
}

export const cocktailService = {
  getAll,
};