import { CheckCircle2, X } from "lucide-react";

type SuccessToastProps = {
  title: string;
  message?: string;
  onClose: () => void;
};

export function SuccessToast({ title, message, onClose }: SuccessToastProps) {
  return (
    <div className="fixed right-4 bottom-4 z-50 w-[calc(100%-2rem)] max-w-sm rounded-card border border-success/40 bg-surface shadow-xl shadow-black/40 ring-1 ring-success/30">
      <div className="flex gap-3 p-4">
        <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-success/15 text-success">
          <CheckCircle2 size={22} />
        </div>

        <div className="flex-1">
          <p className="font-heading text-sm font-semibold text-success">
            {title}
          </p>

          {message && (
            <p className="mt-1 text-sm text-text-muted">{message}</p>
          )}
        </div>

        <button
          type="button"
          onClick={onClose}
          className="rounded-control p-1 text-text-muted hover:bg-surface-bright hover:text-text-main"
          aria-label="Cerrar notificación"
        >
          <X size={16} />
        </button>
      </div>
    </div>
  );
}