import { useMemo, useState } from "react";

import type { Cocktail } from "../../cocktails/cocktail.types";
import type { OrderPreset } from "../orderPresets";
import {
  buildSelectedCocktailsFromPreset,
  distributeQuantitiesByWeight,
} from "../orderPresetUtils";
import type { OrderMode, SelectedOrderCocktail } from "../order.types";

type UseOrderCocktailSelectionParams = {
  cocktails: Cocktail[];
  orderMode: OrderMode;
  totalDrinks: string;
  onClearResult: () => void;
  onSubmitError: (message: string | null) => void;
};

export function useOrderCocktailSelection({
  cocktails,
  orderMode,
  totalDrinks,
  onClearResult,
  onSubmitError,
}: UseOrderCocktailSelectionParams) {
  const [selectedCocktails, setSelectedCocktails] = useState<
    SelectedOrderCocktail[]
  >([]);
  const [selectedPresetId, setSelectedPresetId] = useState<string | null>(null);

  const assignedDrinks = useMemo(() => {
    return selectedCocktails.reduce(
      (total, cocktail) => total + cocktail.quantity,
      0,
    );
  }, [selectedCocktails]);

  function resetCocktailSelection() {
    setSelectedCocktails([]);
    setSelectedPresetId(null);
  }

  function handleApplyPreset(preset: OrderPreset) {
    onClearResult();

    const { selectedCocktails: presetSelectedCocktails, missingCocktailNames } =
      buildSelectedCocktailsFromPreset(preset, cocktails);

    if (presetSelectedCocktails.length === 0) {
      onSubmitError(
        "No se pudo cargar la lista porque sus cócteles no están disponibles en el catálogo.",
      );
      return;
    }

    const numericTotalDrinks = Number(totalDrinks);

    const shouldDistributeByTotalDrinks =
      orderMode === "DRINKS" &&
      numericTotalDrinks >= presetSelectedCocktails.length;

    const nextSelectedCocktails = shouldDistributeByTotalDrinks
      ? distributeQuantitiesByWeight(
          presetSelectedCocktails,
          numericTotalDrinks,
        )
      : presetSelectedCocktails;

    setSelectedCocktails(nextSelectedCocktails);
    setSelectedPresetId(preset.id);

    if (missingCocktailNames.length > 0) {
      onSubmitError(
        `La lista se cargó parcialmente. Faltan en el catálogo: ${missingCocktailNames.join(", ")}.`,
      );
    }
  }

  function handleAddCocktail(cocktail: Cocktail) {
    const cocktailId = cocktail.id;

    const alreadySelected = selectedCocktails.some(
      (selectedCocktail) => selectedCocktail.cocktailId === cocktailId,
    );

    if (alreadySelected) {
      return;
    }

    setSelectedCocktails((currentCocktails) => [
      ...currentCocktails,
      {
        cocktailId,
        cocktailName: cocktail.name,
        weight: 1,
        quantity: 1,
      },
    ]);

    setSelectedPresetId(null);
    onClearResult();
  }

  function handleWeightChange(cocktailId: number, weight: number) {
    const safeWeight = Number.isNaN(weight) || weight < 1 ? 1 : weight;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail) =>
        cocktail.cocktailId === cocktailId
          ? { ...cocktail, weight: safeWeight }
          : cocktail,
      ),
    );

    setSelectedPresetId(null);
    onClearResult();
  }

  function handleQuantityChange(cocktailId: number, quantity: number) {
    const safeQuantity = Number.isNaN(quantity) || quantity < 1 ? 1 : quantity;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail) =>
        cocktail.cocktailId === cocktailId
          ? { ...cocktail, quantity: safeQuantity }
          : cocktail,
      ),
    );

    setSelectedPresetId(null);
    onClearResult();
  }

  function handleRemoveCocktail(cocktailId: number) {
    setSelectedCocktails((currentCocktails) =>
      currentCocktails.filter((cocktail) => cocktail.cocktailId !== cocktailId),
    );

    setSelectedPresetId(null);
    onClearResult();
  }

  function distributeSelectedCocktailsByTotalDrinks(
    numericTotalDrinks: number,
  ) {
    setSelectedCocktails((currentCocktails) =>
      distributeQuantitiesByWeight(currentCocktails, numericTotalDrinks),
    );
  }

  function handleDistributeEqually() {
    const numericTotalDrinks = Number(totalDrinks);

    if (numericTotalDrinks <= 0) {
      onSubmitError("Indicá una cantidad total de tragos mayor a 0.");
      return;
    }

    if (selectedCocktails.length === 0) {
      onSubmitError("Seleccioná al menos un cóctel para dividir la cantidad.");
      return;
    }

    if (numericTotalDrinks < selectedCocktails.length) {
      onSubmitError(
        "El total de tragos debe ser mayor o igual a la cantidad de cócteles seleccionados.",
      );
      return;
    }

    const baseQuantity = Math.floor(
      numericTotalDrinks / selectedCocktails.length,
    );

    const remainder = numericTotalDrinks % selectedCocktails.length;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail, index) => ({
        ...cocktail,
        quantity: index < remainder ? baseQuantity + 1 : baseQuantity,
      })),
    );

    setSelectedPresetId(null);
    onClearResult();
  }

  return {
    selectedCocktails,
    selectedPresetId,
    assignedDrinks,
    resetCocktailSelection,
    handleApplyPreset,
    handleAddCocktail,
    handleWeightChange,
    handleQuantityChange,
    handleRemoveCocktail,
    handleDistributeEqually,
    distributeSelectedCocktailsByTotalDrinks,
  };
}