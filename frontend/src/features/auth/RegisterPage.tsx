import { Link } from "react-router-dom";
import { ROUTES } from "../../shared/constants/routes";
import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { Input } from "../../shared/components/ui/Input";

export function RegisterPage() {
  return (
    <Card className="w-full max-w-md p-8">
      <h1 className="font-heading text-2xl font-bold text-primary">
        Crear cuenta
      </h1>

      <p className="mt-2 text-text-muted">
        Registrate para guardar tu historial de órdenes.
      </p>

      <div className="mt-6 space-y-4">
        <Input type="text" placeholder="Nombre" name="firstName" />

        <Input type="text" placeholder="Apellido" name="lastName" />

        <Input type="email" placeholder="Email" name="email" />

        <Input type="password" placeholder="Contraseña" name="password" />

        <Button fullWidth>Crear cuenta</Button>
      </div>

      <p className="mt-6 text-sm text-text-muted">
        ¿Ya tenés cuenta?{" "}
        <Link to={ROUTES.login} className="text-primary hover:underline">
          Iniciar sesión
        </Link>
      </p>
    </Card>
  );
}