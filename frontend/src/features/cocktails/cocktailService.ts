import { apiClient } from "../../api/apiClient";
import type { Cocktail } from "./cocktail.types";

async function getAll(): Promise<Cocktail[]> {
  const response = await apiClient.get<Cocktail[]>("/cocktails");
  return response.data;
}

export const cocktailService = {
  getAll,
};