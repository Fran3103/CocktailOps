import { CheckCircle2, Martini, Sparkles } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { OrderPreset } from "../orderPresets";

type OrderPresetSelectorProps = {
  presets: OrderPreset[];
  selectedPresetId: string | null;
  onSelectPreset: (preset: OrderPreset) => void;
};

function getDrinksRuleLabel(cocktailsCount: number) {
  return cocktailsCount >= 8
    ? "2 tragos por persona/hora"
    : "1 trago por persona/hora";
}

export function OrderPresetSelector({
  presets,
  selectedPresetId,
  onSelectPreset,
}: OrderPresetSelectorProps) {
  return (
    <Card className="space-y-4 border-border-soft bg-surface-soft/80">
      <div>
        <div className="mb-2 inline-flex items-center gap-2 rounded-full border border-border-soft bg-background/40 px-3 py-1 text-sm text-primary">
          <Sparkles size={15} />
          Listas rápidas
        </div>

        <h2 className="font-heading text-xl font-semibold text-text-main">
          Elegí una lista predefinida
        </h2>

        <p className="mt-1 text-sm leading-6 text-text-muted">
          Cargá automáticamente una selección de cócteles según el tipo de
          evento. Después podés ajustar pesos, cantidades o agregar más
          cócteles manualmente.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {presets.map((preset) => {
          const isSelected = preset.id === selectedPresetId;
          const cocktailsCount = preset.cocktails.length;

          return (
            <article
              key={preset.id}
              className={`rounded-card border p-4 transition ${
                isSelected
                  ? "border-primary bg-primary/10"
                  : "border-border-soft bg-background/30 hover:border-primary/50"
              }`}
            >
              <div className="flex items-start justify-between gap-3">
                <div className="rounded-control border border-border-soft bg-background/40 p-2 text-primary">
                  <Martini size={18} />
                </div>

                {isSelected && (
                  <span className="inline-flex items-center gap-1 text-xs font-medium text-primary">
                    <CheckCircle2 size={14} />
                    Activa
                  </span>
                )}
              </div>

              <h3 className="mt-4 font-semibold text-text-main">
                {preset.title}
              </h3>

              <p className="mt-2 text-sm leading-6 text-text-muted">
                {preset.description}
              </p>

              <div className="mt-4 space-y-2 text-xs text-text-muted">
                <p>
                  <span className="font-medium text-text-main">
                    Recomendado:
                  </span>{" "}
                  {preset.recommendedFor}
                </p>

                <p>
                  <span className="font-medium text-text-main">
                    Cócteles:
                  </span>{" "}
                  {cocktailsCount}
                </p>

                <p>
                  <span className="font-medium text-text-main">
                    Regla estimada:
                  </span>{" "}
                  {getDrinksRuleLabel(cocktailsCount)}
                </p>
              </div>

              <Button
                type="button"
                variant={isSelected ? "primary" : "secondary"}
                fullWidth
                className="mt-4"
                onClick={() => onSelectPreset(preset)}
              >
                {isSelected ? "Lista cargada" : "Usar lista"}
              </Button>
            </article>
          );
        })}
      </div>
    </Card>
  );
}