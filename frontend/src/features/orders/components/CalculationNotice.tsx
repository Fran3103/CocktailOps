import { Info } from "lucide-react";

export function CalculationNotice() {
  return (
    <details className="group rounded-card border border-border-soft bg-background/40 p-4">
      <summary className="flex cursor-pointer list-none items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <Info size={18} className="text-primary" />

          <h3 className="font-heading text-base font-semibold text-text-main">
            Nota sobre el cálculo
          </h3>
        </div>

        <span className="shrink-0 rounded-full border border-border-soft px-3 py-1 text-xs font-semibold text-text-muted">
          <span className="group-open:hidden">Ver</span>
          <span className="hidden group-open:inline">Ocultar</span>
        </span>
      </summary>

      <p className="mt-4 text-sm leading-6 text-text-muted">
        Las cantidades sugeridas representan insumos suficientes para preparar
        la cantidad estimada de tragos. Cuando un producto se compra en unidades
        comerciales completas, como botellas o packs, el sistema puede redondear
        hacia arriba para asegurar disponibilidad. Por eso puede quedar sobrante
        de algunos insumos. El cálculo no descuenta stock previo ni optimiza
        compras mínimas.
      </p>
    </details>
  );
}