import {
  Calculator,
  FileText,
  History,
  Martini,
  PackageCheck,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import { ROUTES } from "../../../shared/constants/routes";    
import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { PageHeader } from "../../../shared/components/ui/PageHeader";
import { CocktailsPreview } from "./CocktailsPreview";

const guestFeatures = [
  {
    title: "Cálculo de tragos",
    description:
      "Estimá la cantidad de bebidas necesarias según invitados, duración o cantidad total de tragos.",
    icon: Calculator,
  },
  {
    title: "Cálculo de insumos",
    description:
      "Convertí la selección de cócteles en una lista clara de productos y packs necesarios.",
    icon: PackageCheck,
  },
  {
    title: "PDF descargable",
    description:
      "Generá un resumen en PDF para guardar o compartir la orden calculada.",
    icon: FileText,
  },
  {
    title: "Historial registrado",
    description:
      "Los usuarios registrados pueden guardar órdenes y consultarlas más adelante.",
    icon: History,
  },
];

export function GuestDashboard() {
  const navigate = useNavigate();

  return (
    <section className="space-y-8">
      <PageHeader
        title="Planificá bebidas para eventos"
        description="CocktailOps calcula tragos, insumos y packs necesarios para preparar órdenes de cócteles de forma rápida."
      />

      <Card className="border-border-soft bg-surface-soft/80">
        <div className="grid gap-6 lg:grid-cols-[1.4fr_0.8fr] lg:items-center">
          <div>
            <div className="mb-4 inline-flex items-center gap-2 rounded-full border border-border-soft bg-background/40 px-3 py-1 text-sm text-primary">
              <Martini size={16} />
              Orden temporal para invitados
            </div>

            <h2 className="text-2xl font-bold text-text-main">
              Calculá una orden sin crear una cuenta
            </h2>

            <p className="mt-3 max-w-2xl leading-7 text-text-muted">
              Como invitado podés generar una orden temporal, ver el resumen en
              pantalla y descargar el PDF en el momento. La orden no se guarda
              en historial y no se puede recuperar después.
            </p>

            <div className="mt-6 flex flex-col gap-3 sm:flex-row">
              <Button
                type="button"
                onClick={() => navigate(ROUTES.createOrder)}
              >
                Generar orden
              </Button>

              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate(ROUTES.cocktails)}
              >
                Ver cócteles
              </Button>
            </div>
          </div>

          <div className="rounded-card border border-border-soft bg-background/40 p-5">
            <p className="text-sm font-medium text-text-muted">
              Flujo invitado
            </p>

            <div className="mt-4 space-y-3 text-sm text-text-muted">
              <p>1. Elegís cócteles.</p>
              <p>2. Indicás invitados, duración o total de tragos.</p>
              <p>3. CocktailOps calcula insumos y packs.</p>
              <p>4. Descargás el PDF de la orden temporal.</p>
            </div>
          </div>
        </div>
      </Card>

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {guestFeatures.map((feature) => {
          const Icon = feature.icon;

          return (
            <Card
              key={feature.title}
              className="border-border-soft bg-surface-soft/80"
            >
              <div className="mb-4 inline-flex rounded-control border border-border-soft bg-background/40 p-2 text-primary">
                <Icon size={18} />
              </div>

              <h3 className="font-semibold text-text-main">
                {feature.title}
              </h3>

              <p className="mt-2 text-sm leading-6 text-text-muted">
                {feature.description}
              </p>
            </Card>
          );
        })}
      </div>

      <CocktailsPreview />
    </section>
  );
}