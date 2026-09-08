import { useMemo } from "react";

import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  SelectedOrderCocktail,
} from "../order.types";

type UseOrderPayloadsParams = {
  orderMode: OrderMode;
  guests: string;
  durationHours: string;
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktails: SelectedOrderCocktail[];
};

export function useOrderPayloads({
  orderMode,
  guests,
  durationHours,
  totalDrinks,
  assignedDrinks,
  selectedCocktails,
}: UseOrderPayloadsParams) {
  const timePayload = useMemo<CreateTimeOrderRequest | null>(() => {
    const numericGuests = Number(guests);
    const numericDurationHours = Number(durationHours);

    if (
      numericGuests <= 0 ||
      numericDurationHours <= 0 ||
      selectedCocktails.length === 0
    ) {
      return null;
    }

    return {
      guests: numericGuests,
      durationHours: numericDurationHours,
      cocktails: selectedCocktails.map((cocktail) => ({
        cocktailId: cocktail.cocktailId,
        weight: cocktail.weight,
      })),
    };
  }, [guests, durationHours, selectedCocktails]);

  const drinksPayload = useMemo<CreateDrinksOrderRequest | null>(() => {
    const numericTotalDrinks = Number(totalDrinks);

    if (
      numericTotalDrinks <= 0 ||
      selectedCocktails.length === 0 ||
      assignedDrinks !== numericTotalDrinks
    ) {
      return null;
    }

    return {
      totalDrinks: numericTotalDrinks,
      cocktails: selectedCocktails.map((cocktail) => ({
        cocktailId: cocktail.cocktailId,
        quantity: cocktail.quantity,
      })),
    };
  }, [totalDrinks, selectedCocktails, assignedDrinks]);

  const currentPayload = orderMode === "TIME" ? timePayload : drinksPayload;

  return {
    timePayload,
    drinksPayload,
    currentPayload,
  };
}