import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";

export function UnauthorizedPage() {
  const navigate = useNavigate();

  return (
    <section>
      <PageHeader
        title="Acceso denegado"
        description="No tenés permisos suficientes para acceder a esta sección."
      />

      <Card className="mt-6 max-w-xl">
        <p className="text-text-muted">
          Esta funcionalidad está reservada para usuarios con permisos
          específicos dentro de CocktailOps.
        </p>

        <Button
          type="button"
          className="mt-4"
          onClick={() => navigate(ROUTES.dashboard)}
        >
          Volver al dashboard
        </Button>
      </Card>
    </section>
  );
}