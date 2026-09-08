export type OrderWizardStep = "DETAILS" | "COCKTAILS" | "SUMMARY";

export type OrderWizardStepConfig = {
  id: OrderWizardStep;
  number: string;
  label: string;
};

export const orderWizardSteps: OrderWizardStepConfig[] = [
  {
    id: "DETAILS",
    number: "1",
    label: "Detalles",
  },
  {
    id: "COCKTAILS",
    number: "2",
    label: "Cócteles",
  },
  {
    id: "SUMMARY",
    number: "3",
    label: "Cálculo",
  },
];