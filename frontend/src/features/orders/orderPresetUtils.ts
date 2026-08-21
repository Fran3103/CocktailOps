import type { Cocktail } from "../cocktails/cocktail.types";
import type { OrderPreset } from "./orderPresets";
import type { SelectedOrderCocktail } from "./order.types";

function normalizeCocktailName(name: string) {
  return name.trim().toLowerCase();
}

export function buildSelectedCocktailsFromPreset(
  preset: OrderPreset,
  cocktails: Cocktail[],
) {
  const cocktailsByName = new Map(
    cocktails.map((cocktail) => [
      normalizeCocktailName(cocktail.name),
      cocktail,
    ]),
  );

  const missingCocktailNames: string[] = [];
  const selectedCocktails: SelectedOrderCocktail[] = [];

  preset.cocktails.forEach((presetCocktail) => {
    const cocktail = cocktailsByName.get(
      normalizeCocktailName(presetCocktail.cocktailName),
    );

    if (!cocktail) {
      missingCocktailNames.push(presetCocktail.cocktailName);
      return;
    }

    selectedCocktails.push({
      cocktailId: cocktail.id,
      cocktailName: cocktail.name,
      weight: presetCocktail.weight,
      quantity: 1,
    });
  });

  return {
    selectedCocktails,
    missingCocktailNames,
  };
}

export function distributeQuantitiesByWeight(
  selectedCocktails: SelectedOrderCocktail[],
  totalDrinks: number,
) {
  if (selectedCocktails.length === 0 || totalDrinks < selectedCocktails.length) {
    return selectedCocktails;
  }

  const totalWeight = selectedCocktails.reduce(
    (total, cocktail) => total + cocktail.weight,
    0,
  );

  const drinksAfterMinimum = totalDrinks - selectedCocktails.length;

  const cocktailsWithDistribution = selectedCocktails.map((cocktail, index) => {
    const rawExtraQuantity =
      totalWeight > 0
        ? (drinksAfterMinimum * cocktail.weight) / totalWeight
        : 0;

    const extraQuantity = Math.floor(rawExtraQuantity);

    return {
      cocktail: {
        ...cocktail,
        quantity: 1 + extraQuantity,
      },
      fraction: rawExtraQuantity - extraQuantity,
      index,
    };
  });

  const assignedDrinks = cocktailsWithDistribution.reduce(
    (total, item) => total + item.cocktail.quantity,
    0,
  );

  let remainingDrinks = totalDrinks - assignedDrinks;

  const sortedByFraction = [...cocktailsWithDistribution].sort(
    (a, b) => b.fraction - a.fraction,
  );

  for (const item of sortedByFraction) {
    if (remainingDrinks <= 0) {
      break;
    }

    item.cocktail.quantity += 1;
    remainingDrinks -= 1;
  }

  return cocktailsWithDistribution
    .sort((a, b) => a.index - b.index)
    .map((item) => item.cocktail);
}