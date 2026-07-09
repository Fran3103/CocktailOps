import { useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { Input } from "../../shared/components/ui/Input";
import { ROUTES } from "../../shared/constants/routes";

import { authService } from "./authService";
import { saveAuthData } from "./authStorage";
import type { LoginRequest } from "./auth.types";

export function LoginPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState<LoginRequest>({
    email: "",
    password: "",
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function handleChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError(null);
    setIsLoading(true);

    try {
      const authData = await authService.login(formData);

      saveAuthData(authData);
      navigate(ROUTES.dashboard);
    } catch {
      setError("No se pudo iniciar sesión. Revisá tu email y contraseña.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <Card className="w-full max-w-md p-8">
      <h1 className="font-heading text-2xl font-bold text-primary">
        CocktailOps
      </h1>

      <p className="mt-2 text-text-muted">
        Iniciá sesión para guardar tus órdenes e historial.
      </p>
      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <Input
          type="email"
          placeholder="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          autoComplete="email"
          required
        />

        <Input
          type="password"
          placeholder="Contraseña"
          name="password"
          value={formData.password}
          onChange={handleChange}
          autoComplete="current-password"
          required
        />

        {error && <p className="text-sm text-danger">{error}</p>}

        <Button type="submit" fullWidth disabled={isLoading}>
          {isLoading ? "Ingresando..." : "Iniciar sesión"}
        </Button>
      </form>

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