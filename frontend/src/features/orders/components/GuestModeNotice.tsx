import { useNavigate } from "react-router-dom";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import { ROUTES } from "../../../shared/constants/routes";

export function GuestModeNotice() {
  const navigate = useNavigate();

  return (
    <Card className="border-primary/40 bg-surface">
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h2 className="font-heading text-lg font-semibold text-text-main">
            Estás usando CocktailOps en modo invitado
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Podés armar la orden, pero para guardarla y generar el cálculo real
            necesitás iniciar sesión.
          </p>
        </div>

        <Button
          type="button"
          variant="secondary"
          onClick={() => navigate(ROUTES.login)}
        >
          Iniciar sesión
        </Button>
      </div>
    </Card>
  );
}