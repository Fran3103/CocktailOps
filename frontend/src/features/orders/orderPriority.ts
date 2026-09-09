export type CocktailPriority = "LOW" | "NORMAL" | "MEDIUM" | "HIGH";

export type CocktailPriorityOption = {
  id: CocktailPriority;
  label: string;
  description: string;
  weight: number;
};

export const cocktailPriorityOptions: CocktailPriorityOption[] = [
  {
    id: "LOW",
    label: "Baja",
    description: "Va a salir poco, pero se incluye en el cálculo.",
    weight: 1,
  },
  {
    id: "NORMAL",
    label: "Normal",
    description: "Participación estándar dentro de la barra.",
    weight: 2,
  },
  {
    id: "MEDIUM",
    label: "Media",
    description: "Un poco más presente que el resto.",
    weight: 3,
  },
  {
    id: "HIGH",
    label: "Alta",
    description: "Cóctel protagonista, se calcula más cantidad.",
    weight: 4,
  },
];

export function getPriorityByWeight(weight: number): CocktailPriority {
  if (weight <= 1) return "LOW";
  if (weight === 2) return "NORMAL";
  if (weight === 3) return "MEDIUM";

  return "HIGH";
}

export function getWeightByPriority(priority: CocktailPriority) {
  return (
    cocktailPriorityOptions.find((option) => option.id === priority)?.weight ?? 2
  );
}

export function getPriorityLabelByWeight(weight: number) {
  return (
    cocktailPriorityOptions.find(
      (option) => option.id === getPriorityByWeight(weight),
    )?.label ?? "Normal"
  );
}