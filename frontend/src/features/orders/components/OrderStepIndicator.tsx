import { Card } from "../../../shared/components/ui/Card";
import { orderWizardSteps, type OrderWizardStep } from "../orderWizard.types";

type OrderStepIndicatorProps = {
  currentStep: OrderWizardStep;
};

export function OrderStepIndicator({ currentStep }: OrderStepIndicatorProps) {
  const currentStepIndex = orderWizardSteps.findIndex(
    (step) => step.id === currentStep,
  );

  return (
    <Card className="grid grid-cols-1 gap-3 border-border-soft bg-surface-soft/80 p-4 md:grid-cols-3">
      {orderWizardSteps.map((step, index) => {
        const isActive = step.id === currentStep;
        const isCompleted = index < currentStepIndex;

        return (
          <div key={step.id} className="flex items-center gap-3">
            <div
              className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full border text-sm font-semibold ${
                isActive || isCompleted
                  ? "border-primary bg-primary text-background"
                  : "border-border-soft bg-background text-text-muted"
              }`}
            >
              {step.number}
            </div>

            <div>
              <p
                className={`text-xs font-semibold uppercase tracking-wide ${
                  isActive || isCompleted ? "text-primary" : "text-text-muted"
                }`}
              >
                Paso {step.number}
              </p>

              <p
                className={`text-sm ${
                  isActive ? "text-text-main" : "text-text-muted"
                }`}
              >
                {step.label}
              </p>
            </div>

            {index < orderWizardSteps.length - 1 && (
              <div className="hidden h-px flex-1 bg-border-soft md:block" />
            )}
          </div>
        );
      })}
    </Card>
  );
}