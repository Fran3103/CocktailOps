
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
  onTotalDrinksChange,
 
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

      
    </div>
  );
}