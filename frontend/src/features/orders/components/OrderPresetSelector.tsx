import type { ReactNode } from "react";
import { CheckCircle2, Martini, Sparkles } from "lucide-react";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type { OrderPreset } from "../orderPresets";

type OrderPresetSelectorProps = {
  presets: OrderPreset[];
  selectedPresetId: string | null;
  onSelectPreset: (preset: OrderPreset) => void;
  showHeader?: boolean;
  embedded?: boolean;
  footer?: ReactNode;
};

function getDrinksRuleLabel(cocktailsCount: number) {
  return cocktailsCount >= 8 ? "2 pax/hora" : "1 pax/hora";
}

export function OrderPresetSelector({
  presets,
  selectedPresetId,
  onSelectPreset,
  showHeader = true,
  embedded = false,
  footer,
}: OrderPresetSelectorProps) {
  const activePreset = presets.find((preset) => preset.id === selectedPresetId);

  const content = (
    <div className="space-y-4">
      {showHeader && (
        <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <div className="mb-2 inline-flex items-center gap-2 rounded-full border border-border-soft bg-background/40 px-3 py-1 text-sm text-primary">
              <Sparkles size={15} />
              Listas rápidas
            </div>

            <h3 className="font-heading text-lg font-semibold text-text-main">
              Elegí una lista predefinida
            </h3>

            <p className="mt-1 max-w-2xl text-sm leading-6 text-text-muted">
              Cargá una base de cócteles según el tipo de evento y ajustala
              después si hace falta.
            </p>
          </div>

          <p className="text-xs font-medium uppercase tracking-wide text-text-muted">
            {presets.length} opciones
          </p>
        </div>
      )}

      <div className="rounded-card border border-border-soft bg-background/30">
        <div className="flex flex-col gap-1 border-b border-border-soft px-4 py-3 text-sm sm:flex-row sm:items-center sm:justify-between">
          <p className="text-text-muted">
            Mostrando {presets.length} {presets.length === 1 ? "lista" : "listas"}
          </p>

          <p className="text-primary">
            {activePreset ? "1 lista activa" : "Sin lista activa"}
          </p>
        </div>

        {presets.length === 0 ? (
          <div className="px-4 py-6">
            <p className="text-sm text-text-muted">
              No hay listas disponibles para mostrar.
            </p>
          </div>
        ) : (
          <div className="divide-y divide-border-soft">
            {presets.map((preset) => {
              const isSelected = preset.id === selectedPresetId;
              const cocktailsCount = preset.cocktails.length;

              return (
                <article
                  key={preset.id}
                  className={`flex flex-col gap-3 px-4 py-3 transition hover:bg-surface-bright/40 sm:flex-row sm:items-center sm:justify-between ${
                    isSelected ? "bg-primary/10" : ""
                  }`}
                >
                  <div className="flex min-w-0 items-start gap-3">
                    <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-control border border-border-soft bg-surface text-primary">
                      <Martini size={18} />
                    </div>

                    <div className="min-w-0">
                      <div className="flex flex-wrap items-center gap-2">
                        <h4 className="font-medium text-text-main">
                          {preset.title}
                        </h4>

                        <span className="rounded-full border border-border-soft bg-background/60 px-2 py-0.5 text-[11px] uppercase tracking-wide text-primary">
                          {getDrinksRuleLabel(cocktailsCount)}
                        </span>

                        {isSelected && (
                          <span className="inline-flex items-center gap-1 rounded-full bg-primary/10 px-2 py-0.5 text-[11px] font-medium text-primary">
                            <CheckCircle2 size={13} />
                            Activa
                          </span>
                        )}
                      </div>

                      <p className="mt-1 line-clamp-2 text-sm leading-6 text-text-muted">
                        {preset.description}
                      </p>

                      <p className="mt-1 text-xs text-text-muted">
                        {cocktailsCount} cócteles
                      </p>
                    </div>
                  </div>

                  <Button
                    type="button"
                    variant={isSelected ? "primary" : "secondary"}
                    fullWidth
                    className="sm:w-auto sm:min-w-36 sm:shrink-0"
                    onClick={() => onSelectPreset(preset)}
                  >
                    {isSelected ? "Lista cargada" : "Usar lista"}
                  </Button>
                </article>
              );
            })}
          </div>
        )}

        {footer && (
          <div className="border-t border-border-soft px-4 py-3">
            {footer}
          </div>
        )}
      </div>
    </div>
  );

  if (embedded) {
    return content;
  }

  return (
    <Card className="space-y-4 border-border-soft bg-surface-soft/80">
      {content}
    </Card>
  );
}