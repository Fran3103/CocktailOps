import { useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { ROUTES } from "../../shared/constants/routes";
import { useAuth } from "../auth/useAuth";

export function ProfilePage() {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuth();

  function handleLogout() {
    logout();
    navigate(ROUTES.login);
  }

  if (!isAuthenticated || !user) {
    return (
      <section>
        <PageHeader
          title="Perfil"
          description="Iniciá sesión para ver tus datos de usuario."
        />

        <Card className="mt-6">
          <p className="text-text-muted">
            Actualmente estás usando CocktailOps en modo invitado.
          </p>

          <Button
            type="button"
            className="mt-4"
            onClick={() => navigate(ROUTES.login)}
          >
            Iniciar sesión
          </Button>
        </Card>
      </section>
    );
  }

  return (
    <section>
      <PageHeader
        title="Perfil"
        description="Datos básicos de la cuenta autenticada."
      />

      <Card className="mt-6 max-w-xl space-y-4">
        <div>
          <p className="text-sm text-text-muted">Nombre</p>
          <p className="font-medium text-text-main">
            {user.firstName} {user.lastName}
          </p>
        </div>

        <div>
          <p className="text-sm text-text-muted">Email</p>
          <p className="font-medium text-text-main">{user.email}</p>
        </div>

        <div>
          <p className="text-sm text-text-muted">Rol</p>
          <p className="font-medium text-primary">{user.role}</p>
        </div>

        <Button type="button" variant="secondary" onClick={handleLogout}>
          Cerrar sesión
        </Button>
      </Card>
    </section>
  );
}