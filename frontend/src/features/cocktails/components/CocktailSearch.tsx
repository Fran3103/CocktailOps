import { Search } from "lucide-react";

type CocktailSearchProps = {
  value: string;
  onChange: (value: string) => void;
};

export function CocktailSearch({ value, onChange }: CocktailSearchProps) {
  return (
    <div className="relative w-full max-w-md">
      <Search
        size={18}
        className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted"
      />

      <input
        type="search"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Buscar cóctel..."
        className="w-full rounded-control border border-border bg-background py-2 pl-10 pr-4 text-sm text-text-main outline-none placeholder:text-text-muted focus:border-primary"
      />
    </div>
  );
}