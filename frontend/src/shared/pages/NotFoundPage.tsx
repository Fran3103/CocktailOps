import { useNavigate } from "react-router-dom";

import { Button } from "../components/ui/Button";
import { ErrorState } from "../utils/ErrorState";
import { PageHeader } from "../components/ui/PageHeader";
import { ROUTES } from "../constants/routes";

export function NotFoundPage() {
  const navigate = useNavigate();

  return (
    <section className="space-y-6 m-auto max-w-3xl pt-12">
      <PageHeader
        title="Página no encontrada"
        description="La ruta solicitada no existe dentro de CocktailOps."
      />

      <ErrorState
        status="404"
        title="No encontramos esta página"
        description="Puede que el enlace esté mal escrito, que la página haya cambiado de ubicación o que el recurso ya no esté disponible."
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