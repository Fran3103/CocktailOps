import { Link } from "react-router-dom";
import { ROUTES } from "../../shared/constants/routes";

export function RegisterPage() {
  return (
    <section className="w-full max-w-md rounded-card border border-border-soft bg-surface-soft p-8">
      <h1 className="font-heading text-2xl font-bold text-primary">
        Crear cuenta
      </h1>

      <p className="mt-2 text-text-muted">
        Registrate para guardar tu historial de órdenes.
      </p>

      <div className="mt-6 space-y-4">
        <input
          type="text"
          placeholder="Nombre"
          className="w-full rounded-control border border-border bg-background px-4 py-2 text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />

        <input
          type="email"
          placeholder="Email"
          className="w-full rounded-control border border-border bg-background px-4 py-2 text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />

        <input
          type="password"
          placeholder="Contraseña"
          className="w-full rounded-control border border-border bg-background px-4 py-2 text-text-main outline-none placeholder:text-text-muted focus:border-primary"
        />

        <button className="w-full rounded-control bg-primary px-4 py-2 font-semibold text-background transition hover:bg-primary-soft">
          Crear cuenta
        </button>
      </div>

      <p className="mt-6 text-sm text-text-muted">
        ¿Ya tenés cuenta?{" "}
        <Link to={ROUTES.login} className="text-primary hover:underline">
          Iniciar sesión
        </Link>
      </p>
    </section>
  );
}