import { Button } from "../../../shared/components/ui/Button";
import { Input } from "../../../shared/components/ui/Input";

type DrinksDetailsFormProps = {
  totalDrinks: string;
  assignedDrinks: number;
  selectedCocktailsCount: number;
  onTotalDrinksChange: (value: string) => void;
  onDistributeEqually: () => void;
};

export function DrinksDetailsForm({
  totalDrinks,
  assignedDrinks,
  selectedCocktailsCount,
  onTotalDrinksChange,
  onDistributeEqually,
}: DrinksDetailsFormProps) {
  return (
    <div className="space-y-4">
      <Input
        label="Cantidad total de tragos"
        type="number"
        min="1"
        value={totalDrinks}
        onChange={(event) => onTotalDrinksChange(event.target.value)}
        placeholder="Ej: 100"
      />

      <div className="flex flex-col gap-3 rounded-control border border-border-soft bg-background p-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p className="text-sm text-text-muted">Total asignado</p>
          <p className="text-lg font-semibold text-text-main">
            {assignedDrinks} / {totalDrinks || "-"}
          </p>
        </div>

        <Button
          type="button"
          variant="secondary"
          onClick={onDistributeEqually}
          disabled={!totalDrinks || selectedCocktailsCount === 0}
        >
          Dividir equitativamente
        </Button>
      </div>
    </div>
  );
}