import { useEffect, useMemo, useState } from "react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { Cocktail } from "../../cocktails/cocktail.types";
import { CocktailSelector } from "./CocktailSelector";
import { OrderPresetSelector } from "./OrderPresetSelector";
import { SelectedCocktailsList } from "./SelectedCocktailsList";
import { StepHeader } from "./StepHeader";
import type { OrderPreset } from "../orderPresets";
import type { OrderMode, SelectedOrderCocktail } from "../order.types";

const MOBILE_CATALOG_PAGE_SIZE = 2;
const DESKTOP_CATALOG_PAGE_SIZE = 5;

const MOBILE_PRESET_PAGE_SIZE = 1;
const DESKTOP_PRESET_PAGE_SIZE = 4;

const MOBILE_MEDIA_QUERY = "(max-width: 767px)";

type CocktailSelectionMode = "PRESETS" | "MANUAL";

type CocktailPreparationFilter =
  | "ALL"
  | "DIRECT"
  | "SHAKEN"
  | "STIRRED"
  | "FROZEN";

const preparationFilters: {
  value: CocktailPreparationFilter;
  label: string;
}[] = [
  { value: "ALL", label: "Todos" },
  { value: "DIRECT", label: "Directos" },
  { value: "SHAKEN", label: "Batidos" },
  { value: "STIRRED", label: "Refrescados" },
  { value: "FROZEN", label: "Frozen" },
];

const preparationSearchLabels: Record<string, string> = {
  DIRECT: "directo directos",
  SHAKEN: "batido batidos",
  STIRRED: "refrescado refrescados",
  FROZEN: "frozen",
};

type OrderCocktailsSectionProps = {
  orderMode: OrderMode;
  cocktails: Cocktail[];
  selectedCocktails: SelectedOrderCocktail[];
  selectedPresetId: string | null;
  presets: OrderPreset[];
  isLoadingCocktails: boolean;
  cocktailsError: string | null;
  error: string | null;
  onSelectPreset: (preset: OrderPreset) => void;
  onAddCocktail: (cocktail: Cocktail) => void;
  onWeightChange: (cocktailId: number, weight: number) => void;
  onQuantityChange: (cocktailId: number, quantity: number) => void;
  onRemoveCocktail: (cocktailId: number) => void;
  onPrevious: () => void;
  onNext: () => void;
};

function isMobileViewport() {
  if (typeof window === "undefined") {
    return false;
  }

  return window.matchMedia(MOBILE_MEDIA_QUERY).matches;
}

function getCatalogPageSize() {
  return isMobileViewport()
    ? MOBILE_CATALOG_PAGE_SIZE
    : DESKTOP_CATALOG_PAGE_SIZE;
}

function getPresetPageSize() {
  return isMobileViewport()
    ? MOBILE_PRESET_PAGE_SIZE
    : DESKTOP_PRESET_PAGE_SIZE;
}

export function OrderCocktailsSection({
  orderMode,
  cocktails,
  selectedCocktails,
  selectedPresetId,
  presets,
  isLoadingCocktails,
  cocktailsError,
  error,
  onSelectPreset,
  onAddCocktail,
  onWeightChange,
  onQuantityChange,
  onRemoveCocktail,
  onPrevious,
  onNext,
}: OrderCocktailsSectionProps) {
  const [selectionMode, setSelectionMode] =
    useState<CocktailSelectionMode>("PRESETS");

  const [preparationFilter, setPreparationFilter] =
    useState<CocktailPreparationFilter>("ALL");

  const [catalogSearchTerm, setCatalogSearchTerm] = useState("");

  const [catalogPage, setCatalogPage] = useState(1);
  const [presetPage, setPresetPage] = useState(1);

  const [catalogPageSize, setCatalogPageSize] = useState(getCatalogPageSize);
  const [presetPageSize, setPresetPageSize] = useState(getPresetPageSize);

  useEffect(() => {
    if (typeof window === "undefined") {
      return;
    }

    const mediaQuery = window.matchMedia(MOBILE_MEDIA_QUERY);

    function handleViewportChange() {
      setCatalogPageSize(getCatalogPageSize());
      setPresetPageSize(getPresetPageSize());
      setCatalogPage(1);
      setPresetPage(1);
    }

    mediaQuery.addEventListener("change", handleViewportChange);

    return () => {
      mediaQuery.removeEventListener("change", handleViewportChange);
    };
  }, []);

  const filteredCatalogCocktails = useMemo(() => {
    const normalizedSearch = catalogSearchTerm.toLowerCase().trim();

    return cocktails.filter((cocktail) => {
      const matchesPreparation =
        preparationFilter === "ALL" ||
        cocktail.preparationType === preparationFilter;

      if (!matchesPreparation) {
        return false;
      }

      if (!normalizedSearch) {
        return true;
      }

      const searchableText = [
        cocktail.name,
        cocktail.description ?? "",
        cocktail.preparationType ?? "",
        cocktail.preparationType
          ? preparationSearchLabels[cocktail.preparationType] ?? ""
          : "",
        ...(cocktail.ingredients?.map(
          (ingredient) => ingredient.productName ?? "",
        ) ?? []),
      ]
        .join(" ")
        .toLowerCase();

      return searchableText.includes(normalizedSearch);
    });
  }, [cocktails, preparationFilter, catalogSearchTerm]);

  const totalCatalogPages = Math.max(
    1,
    Math.ceil(filteredCatalogCocktails.length / catalogPageSize),
  );

  const safeCatalogPage = Math.min(catalogPage, totalCatalogPages);
  const catalogStartIndex = (safeCatalogPage - 1) * catalogPageSize;
  const catalogEndIndex = catalogStartIndex + catalogPageSize;

  const paginatedCocktails = useMemo(() => {
    return filteredCatalogCocktails.slice(catalogStartIndex, catalogEndIndex);
  }, [filteredCatalogCocktails, catalogStartIndex, catalogEndIndex]);

  const totalPresetPages = Math.max(
    1,
    Math.ceil(presets.length / presetPageSize),
  );

  const safePresetPage = Math.min(presetPage, totalPresetPages);
  const presetStartIndex = (safePresetPage - 1) * presetPageSize;
  const presetEndIndex = presetStartIndex + presetPageSize;

  const paginatedPresets = useMemo(() => {
    return presets.slice(presetStartIndex, presetEndIndex);
  }, [presets, presetStartIndex, presetEndIndex]);

  function handleSelectMode(mode: CocktailSelectionMode) {
    setSelectionMode(mode);
  }

  function handlePreviousCatalogPage() {
    setCatalogPage((currentPage) => Math.max(currentPage - 1, 1));
  }

  function handleNextCatalogPage() {
    setCatalogPage((currentPage) =>
      Math.min(currentPage + 1, totalCatalogPages),
    );
  }

  function handlePreviousPresetPage() {
    setPresetPage((currentPage) => Math.max(currentPage - 1, 1));
  }

  function handleNextPresetPage() {
    setPresetPage((currentPage) => Math.min(currentPage + 1, totalPresetPages));
  }

  function handlePreparationFilterChange(filter: CocktailPreparationFilter) {
    setPreparationFilter(filter);
    setCatalogPage(1);
  }

  function handleCatalogSearchTermChange(value: string) {
    setCatalogSearchTerm(value);
    setCatalogPage(1);
  }

  return (
    <Card className="space-y-6">
      <StepHeader
        step="Paso 2"
        title="Elegí los cócteles"
        description="Seleccioná una lista predefinida o armá una selección manual."
      />

      <div className="grid grid-cols-1 gap-3 md:grid-cols-2">
        <button
          type="button"
          onClick={() => handleSelectMode("PRESETS")}
          className={`rounded-card border p-4 text-left transition ${
            selectionMode === "PRESETS"
              ? "border-primary bg-surface text-text-main"
              : "border-border-soft bg-background/40 text-text-muted hover:border-primary/60"
          }`}
        >
          <h3 className="font-heading text-lg font-semibold">
            Listas predefinidas
          </h3>

          <p className="mt-2 text-sm leading-6">
            Usá una selección pensada para distintos tipos de evento.
          </p>
        </button>

        <button
          type="button"
          onClick={() => handleSelectMode("MANUAL")}
          className={`rounded-card border p-4 text-left transition ${
            selectionMode === "MANUAL"
              ? "border-primary bg-surface text-text-main"
              : "border-border-soft bg-background/40 text-text-muted hover:border-primary/60"
          }`}
        >
          <h3 className="font-heading text-lg font-semibold">
            Selección manual
          </h3>

          <p className="mt-2 text-sm leading-6">
            Buscá y agregá cócteles individuales desde el catálogo.
          </p>
        </button>
      </div>

      {selectionMode === "PRESETS" && (
        <div className="space-y-4">
          {isLoadingCocktails && (
            <div className="rounded-card border border-border-soft bg-background/30 p-4">
              <p className="text-sm text-text-muted">Cargando listas...</p>
            </div>
          )}

          {!isLoadingCocktails && cocktailsError && (
            <div className="rounded-card border border-danger/40 bg-danger/5 p-4">
              <p className="text-sm text-danger">{cocktailsError}</p>
            </div>
          )}

          {!isLoadingCocktails && !cocktailsError && (
            <OrderPresetSelector
              embedded
              showHeader={false}
              presets={paginatedPresets}
              selectedPresetId={selectedPresetId}
              onSelectPreset={onSelectPreset}
              footer={
                presets.length > presetPageSize ? (
                  <div className="space-y-3">
                    <p className="text-sm text-text-muted">
                      Página {safePresetPage} de {totalPresetPages}
                    </p>

                    <div className="grid grid-cols-2 gap-3">
                      <Button
                        type="button"
                        variant="secondary"
                        onClick={handlePreviousPresetPage}
                        disabled={safePresetPage === 1}
                        fullWidth
                      >
                        Anterior
                      </Button>

                      <Button
                        type="button"
                        variant="secondary"
                        onClick={handleNextPresetPage}
                        disabled={safePresetPage === totalPresetPages}
                        fullWidth
                      >
                        Siguiente
                      </Button>
                    </div>
                  </div>
                ) : null
              }
            />
          )}
        </div>
      )}

      {selectionMode === "MANUAL" && (
        <div className="space-y-4">
          <div className="flex flex-wrap gap-2">
            {preparationFilters.map((filter) => {
              const isActive = preparationFilter === filter.value;

              return (
                <button
                  key={filter.value}
                  type="button"
                  onClick={() => handlePreparationFilterChange(filter.value)}
                  className={`rounded-full border px-3 py-1.5 text-xs font-semibold transition ${
                    isActive
                      ? "border-primary bg-primary text-background"
                      : "border-border-soft bg-background/40 text-text-muted hover:border-primary/60 hover:text-text-main"
                  }`}
                >
                  {filter.label}
                </button>
              );
            })}
          </div>

          {isLoadingCocktails && (
            <div className="rounded-card border border-border-soft bg-background/30 p-4">
              <p className="text-sm text-text-muted">Cargando cócteles...</p>
            </div>
          )}

          {!isLoadingCocktails && cocktailsError && (
            <div className="rounded-card border border-danger/40 bg-danger/5 p-4">
              <p className="text-sm text-danger">{cocktailsError}</p>
            </div>
          )}

          {!isLoadingCocktails && !cocktailsError && (
            <CocktailSelector
              embedded
              showHeader={false}
              cocktails={paginatedCocktails}
              selectedCocktails={selectedCocktails}
              onAddCocktail={onAddCocktail}
              searchTerm={catalogSearchTerm}
              onSearchTermChange={handleCatalogSearchTermChange}
              totalCocktailsCount={filteredCatalogCocktails.length}
              footer={
                filteredCatalogCocktails.length > catalogPageSize ? (
                  <div className="space-y-3">
                    <p className="text-sm text-text-muted">
                      Página {safeCatalogPage} de {totalCatalogPages}
                    </p>

                    <div className="grid grid-cols-2 gap-3">
                      <Button
                        type="button"
                        variant="secondary"
                        onClick={handlePreviousCatalogPage}
                        disabled={safeCatalogPage === 1}
                        fullWidth
                      >
                        Anterior
                      </Button>

                      <Button
                        type="button"
                        variant="secondary"
                        onClick={handleNextCatalogPage}
                        disabled={safeCatalogPage === totalCatalogPages}
                        fullWidth
                      >
                        Siguiente
                      </Button>
                    </div>
                  </div>
                ) : null
              }
            />
          )}
        </div>
      )}

      <div className="space-y-4 rounded-card border border-border-soft bg-background/40 p-4">
        <div>
          <h3 className="font-heading text-lg font-semibold text-text-main">
            Cócteles seleccionados
          </h3>

          <p className="mt-1 text-sm text-text-muted">
            {orderMode === "TIME"
              ? "El peso define qué cócteles tienen más presencia dentro del evento."
              : "La cantidad define cuántos tragos se calcularán por cada cóctel."}
          </p>
        </div>

        <SelectedCocktailsList
          orderMode={orderMode}
          selectedCocktails={selectedCocktails}
          onWeightChange={onWeightChange}
          onQuantityChange={onQuantityChange}
          onRemoveCocktail={onRemoveCocktail}
        />
      </div>

      {error && (
        <div className="rounded-card border border-danger/40 bg-danger/5 p-3">
          <p className="text-sm text-danger">{error}</p>
        </div>
      )}

      <div className="flex flex-col gap-3 border-t border-border-soft pt-4 sm:flex-row sm:justify-between">
        <Button type="button" variant="secondary" onClick={onPrevious}>
          Volver
        </Button>

        <Button type="button" onClick={onNext}>
          Siguiente: revisar cálculo
        </Button>
      </div>
    </Card>
  );
}