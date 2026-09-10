import { Button } from "../../../shared/components/ui/Button";

type DrinksAssignmentCounterProps = {
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktailsCount: number;
  onDistributeEqually: () => void;
};

function getCounterMessage(totalDrinks: number, assignedDrinks: number) {
  if (totalDrinks <= 0) {
    return "Indicá el total de tragos para controlar la asignación.";
  }

  const difference = totalDrinks - assignedDrinks;

  if (difference > 0) {
    return `Faltan ${difference} tragos para completar la orden.`;
  }

  if (difference === 0) {
    return "La asignación coincide con el total definido.";
  }

  return `Te excediste por ${Math.abs(difference)} tragos.`;
}

function getCounterClassName(totalDrinks: number, assignedDrinks: number) {
  if (totalDrinks <= 0) {
    return "border-border-soft bg-background/40 text-text-muted";
  }

  if (assignedDrinks === totalDrinks) {
    return "border-success/40 bg-success/10 text-success";
  }

  if (assignedDrinks > totalDrinks) {
    return "border-danger/40 bg-danger/10 text-danger";
  }

  return "border-primary/30 bg-primary/10 text-primary";
}

export function DrinksAssignmentCounter({
  totalDrinks,
  assignedDrinks,
  selectedCocktailsCount,
  onDistributeEqually,
}: DrinksAssignmentCounterProps) {
  const numericTotalDrinks = Number(totalDrinks);
  const message = getCounterMessage(numericTotalDrinks, assignedDrinks);
  const className = getCounterClassName(numericTotalDrinks, assignedDrinks);

  const isDistributeDisabled =
    numericTotalDrinks <= 0 || selectedCocktailsCount === 0;

  return (
    <div className={`rounded-control border p-4 ${className}`}>
      <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-wide">
            Asignación de tragos
          </p>

          <p className="mt-1 text-2xl font-semibold">
            {assignedDrinks} / {totalDrinks || "-"}
          </p>

          <p className="mt-1 text-sm font-medium">{message}</p>
        </div>

        <Button
          type="button"
          variant="secondary"
          onClick={onDistributeEqually}
          disabled={isDistributeDisabled}
        >
          Repartir equitativamente
        </Button>
      </div>
    </div>
  );
}