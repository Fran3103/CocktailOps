import { Search } from "lucide-react";

type ProductFiltersProps = {
  searchTerm: string;
  onSearchChange: (value: string) => void;
};

export function ProductFilters({
  searchTerm,
  onSearchChange,
}: ProductFiltersProps) {
  return (
    <div className="flex flex-col gap-4 rounded-card border border-border-soft bg-surface-soft p-4 sm:flex-row sm:items-center sm:justify-between">
      <div className="relative w-full sm:max-w-md">
        <Search
          size={18}
          className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted"
        />

        <input
          type="search"
          value={searchTerm}
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Buscar producto..."
          className="w-full rounded-control border border-border bg-background py-2 pl-10 pr-4 text-sm text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />
      </div>
    </div>
  );
}