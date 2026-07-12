import type { OrderMode } from "../order.types";

type OrderModeSelectorProps = {
  value: OrderMode;
  onChange: (mode: OrderMode) => void;
};

export function OrderModeSelector({ value, onChange }: OrderModeSelectorProps) {
  return (
    <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
      <button
        type="button"
        onClick={() => onChange("TIME")}
        className={`rounded-card border p-4 text-left transition ${
          value === "TIME"
            ? "border-primary bg-surface text-text-main"
            : "border-border-soft bg-surface-soft text-text-muted hover:bg-surface-bright"
        }`}
      >
        <p className="font-heading text-lg font-semibold">Por evento</p>
        <p className="mt-1 text-sm">
          Calcula tragos según invitados, duración y peso de cada cóctel.
        </p>
      </button>

      <button
        type="button"
        onClick={() => onChange("DRINKS")}
        className={`rounded-card border p-4 text-left transition ${
          value === "DRINKS"
            ? "border-primary bg-surface text-text-main"
            : "border-border-soft bg-surface-soft text-text-muted hover:bg-surface-bright"
        }`}
      >
        <p className="font-heading text-lg font-semibold">Por cantidad</p>
        <p className="mt-1 text-sm">
          Calcula insumos según una cantidad total de tragos definida.
        </p>
      </button>
    </div>
  );
}