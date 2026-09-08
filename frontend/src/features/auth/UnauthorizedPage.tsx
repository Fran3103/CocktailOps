import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { ErrorState } from "../../shared/utils/ErrorState";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";

export function UnauthorizedPage() {
  const navigate = useNavigate();

  return (
    <section className="space-y-6">
      <PageHeader
        title="Acceso denegado"
        description="Esta sección requiere permisos adicionales."
      />

      <ErrorState
        status="403"
        title="No tenés permisos para acceder"
        description="La ruta existe, pero tu usuario no tiene autorización para ver esta sección. Si necesitás acceder como administrador, iniciá sesión con una cuenta que tenga ese rol."
      >
        <Button type="button" onClick={() => navigate(ROUTES.dashboard)}>
          Volver al dashboard
        </Button>

        <Button
          type="button"
          variant="secondary"
          onClick={() => navigate(ROUTES.createOrder)}
        >
          Crear una orden
        </Button>
      </ErrorState>
    </section>
  );
}