import { Link } from "react-router-dom";
import { ROUTES } from "../../shared/constants/routes";
import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { Input } from "../../shared/components/ui/Input";

export function LoginPage() {
  return (
    <Card className="w-full max-w-md p-8">
      <h1 className="font-heading text-2xl font-bold text-primary">
        CocktailOps
      </h1>

      <p className="mt-2 text-text-muted">
        Iniciá sesión para guardar tus órdenes e historial.
      </p>

      <div className="mt-6 space-y-4">
        <Input type="email" placeholder="Email" name="email" />

        <Input type="password" placeholder="Contraseña" name="password" />

        <Button fullWidth>Iniciar sesión</Button>
      </div>

      <p className="mt-6 text-sm text-text-muted">
        ¿No tenés cuenta?{" "}
        <Link to={ROUTES.register} className="text-primary hover:underline">
          Crear cuenta
        </Link>
      </p>

      <Link
        to={ROUTES.createOrder}
        className="mt-4 inline-block text-sm text-text-muted hover:text-text-main"
      >
        Continuar como invitado
      </Link>
    </Card>
  );
}