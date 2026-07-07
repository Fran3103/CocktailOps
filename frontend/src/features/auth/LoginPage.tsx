import { Link } from "react-router-dom";
import { ROUTES } from "../../shared/constants/routes";

export function LoginPage() {
  return (
    <section className="w-full max-w-md rounded-card border border-border-soft bg-surface-soft p-8">
      <h1 className="font-heading text-2xl font-bold text-primary">
        CocktailOps
      </h1>

      <p className="mt-2 text-text-muted">
        Inicia sesión para guardar tus pedidos y acceder a todas las
        funcionalidades de la aplicación.
      </p>

      <div className="mt-6 space-y-4">
        <input
          type="email"
          placeholder="Email"
          className="w-full rounded-control border border-border bg-background px-4 py-2 text-text-main
                     placeholder:text-text-muted focus:border-primary focus:ring-1 focus:ring-primary outline-none"
        />
        <input
          type="password"
          placeholder="Contraseña"
          className="w-full rounded-control border border-border bg-background px-4 py-2 text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />

        <button className="w-full rounded-control bg-primary px-4 py-2 font-semibold text-background transition hover:bg-primary-soft">
          Iniciar sesión
        </button>
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
    </section>
  );
}
