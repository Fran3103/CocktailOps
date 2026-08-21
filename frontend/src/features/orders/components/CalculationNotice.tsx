import { Info } from "lucide-react";

import { Card } from "../../../shared/components/ui/Card";

export function CalculationNotice() {
  return (
    <Card className="border-primary/30 bg-primary/5">
      <div className="flex gap-3">
        <div className="mt-1 text-primary">
          <Info size={18} />
        </div>

        <div>
          <h3 className="font-medium text-text-main">Nota sobre el cálculo</h3>

          <p className="mt-1 text-sm leading-6 text-text-muted">
            Las cantidades sugeridas representan insumos suficientes para
            preparar la cantidad estimada de tragos. Cuando un producto se
            compra en unidades comerciales completas, como botellas o packs, el
            sistema puede redondear hacia arriba para asegurar disponibilidad.
            Por eso puede quedar sobrante de algunos insumos. El cálculo no
            descuenta stock previo ni optimiza compras mínimas.
          </p>
        </div>
      </div>
    </Card>
  );
}