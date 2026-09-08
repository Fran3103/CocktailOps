import { Link } from "react-router-dom";

export function GuestModeNotice() {
  return (
    <div className="rounded-card border border-primary/25 bg-primary/10 px-3 py-2">
      <p className="text-sm leading-6 text-text-muted">
        <span className="font-semibold text-primary">Modo invitado:</span>{" "}
        podés generar una orden temporal.{" "}
        <Link
          to="/login"
          className="font-semibold text-primary underline-offset-4 hover:underline"
        >
          Iniciá sesión
        </Link>{" "}
        para guardarla en tu historial.
      </p>
    </div>
  );
}