import { CheckCircle2, Eye, PlusCircle, Download } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { ROUTES } from "../../../shared/constants/routes";

import { Button } from "../../../shared/components/ui/Button";
import { Card } from "../../../shared/components/ui/Card";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderResponse,
} from "../order.types";
import { orderService } from "../orderService";
import { useState } from "react";

type CreatedOrderSummaryProps = {
  order: OrderResponse;
  onCreateNewOrder: () => void;
  timePreviewPayload?: CreateTimeOrderRequest | null;
  drinksPreviewPayload?: CreateDrinksOrderRequest | null;
};

export function CreatedOrderSummary({
  order,
  onCreateNewOrder,
  timePreviewPayload,
  drinksPreviewPayload,
}: CreatedOrderSummaryProps) {
  const cocktailCount = order.cocktail?.length ?? 0;
  const itemCount = order.items?.length ?? 0;
  const isTemporaryOrder = order.id == null;
  const totalDrinks =
    order.cocktail?.reduce(
      (total, cocktail) => total + (cocktail.quantity ?? 0),
      0,
    ) ?? 0;
  const navigate = useNavigate();
  const [isDownloadingPdf, setIsDownloadingPdf] = useState(false);
  const [downloadError, setDownloadError] = useState<string | null>(null);

  function handleViewDetail() {
    if (order.id == null) return;

    navigate(ROUTES.orderDetails.replace(":id", String(order.id)), {
      state: {
        order,
        timePreviewPayload,
        drinksPreviewPayload,
      },
    });
  }
  async function handleDownloadPdf() {
    setDownloadError(null);
    setIsDownloadingPdf(true);

    try {
      let pdfBlob: Blob;

      if (isTemporaryOrder) {
        if (timePreviewPayload) {
          pdfBlob =
            await orderService.downloadTimePreviewPdf(timePreviewPayload);
        } else if (drinksPreviewPayload) {
          pdfBlob =
            await orderService.downloadDrinksPreviewPdf(drinksPreviewPayload);
        } else {
          setDownloadError(
            "No se encontraron los datos necesarios para generar el PDF.",
          );
          return;
        }
      } else {
        pdfBlob = await orderService.downloadPdf(order.id);
      }

      const url = window.URL.createObjectURL(pdfBlob);
      const link = document.createElement("a");

      link.href = url;
      link.download = isTemporaryOrder
        ? "cocktailops-order-preview.pdf"
        : `cocktailops-order-${order.id}.pdf`;

      document.body.appendChild(link);
      link.click();
      link.remove();

      window.URL.revokeObjectURL(url);
    } catch {
      setDownloadError("No se pudo descargar el PDF. Intentá nuevamente.");
    } finally {
      setIsDownloadingPdf(false);
    }
  }
  return (
    <Card className="relative overflow-hidden border-success/70 bg-[linear-gradient(135deg,rgba(181,204,192,0.18),rgba(26,46,38,0.98))] shadow-[0_0_45px_rgba(181,204,192,0.18)] ring-1 ring-success/40 motion-safe:animate-card-arrive">
      <div className="-mx-6 -mt-6 mb-6 h-4 bg-success" />

      <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
        <div className="flex gap-4">
          <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-success/15 text-success ring-1 ring-success/30">
            <CheckCircle2 size={28} />
          </div>

          <div>
            <p className="text-sm font-semibold uppercase tracking-wide text-success">
              Orden creada
            </p>

            <h2 className="mt-1 font-heading text-2xl font-bold text-text-main">
              {isTemporaryOrder
                ? "Orden generada correctamente"
                : `Orden #${order.id} generada correctamente`}
            </h2>

            <p className="mt-2 text-sm text-text-muted">
              El backend calculó los cócteles y los productos necesarios para
              esta orden.
            </p>

            {order.userId ? (
              <p className="mt-3 rounded-control border border-success/20 bg-background/50 px-3 py-2 text-sm text-success">
                La orden quedó asociada a tu cuenta y estará disponible en tu
                historial.
              </p>
            ) : (
              <p className="mt-3 rounded-control border border-primary/20 bg-background/50 px-3 py-2 text-sm text-primary-soft">
                La orden fue generada como invitado y no estará disponible en un
                historial personal.
              </p>
            )}
          </div>
        </div>

        <div className="flex flex-col gap-3 sm:flex-row lg:flex-col xl:flex-row">
          {isTemporaryOrder ? (
            <div className="relative max-w-sm rounded-control border border-primary/50 bg-primary/10 p-4 shadow-[0_0_28px_rgba(212,169,83,0.22)] ring-1 ring-primary/30 motion-safe:animate-soft-glow">
              <div className="absolute -right-2 -top-2 flex h-5 w-5">
                <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-primary opacity-60" />
                <span className="relative inline-flex h-5 w-5 rounded-full bg-primary" />
              </div>

              <p className="mb-3 text-sm font-medium text-text-main">
                Tu orden temporal ya está lista.
              </p>

              <p className="mb-4 text-sm text-text-muted">
                Descargá el PDF ahora. Esta orden no se guardará en el
                historial.
              </p>

              <Button
                type="button"
                onClick={handleDownloadPdf}
                disabled={isDownloadingPdf}
                className="w-full bg-primary text-background shadow-lg shadow-primary/30 transition hover:-translate-y-0.5 hover:bg-primary-soft hover:shadow-primary/50 motion-safe:animate-download-nudge"
              >
                <span className="flex items-center justify-center gap-2 font-semibold">
                  <Download size={17} />
                  {isDownloadingPdf ? "Generando PDF..." : "Descargar PDF"}
                </span>
              </Button>
            </div>
          ) : (
            <>
              <Button className="flex gap-2 items-center text-white relative max-w-sm rounded-control border border-primary/50 bg-primary/10 p-4 shadow-[0_0_28px_rgba(212,169,83,0.22)] ring-1 ring-primary/30 motion-safe:animate-soft-glow" type="button" onClick={handleViewDetail}>
                Ver detalle<Eye size={16} />
              </Button>

              <Button
                type="button"
                variant="secondary"
                onClick={handleDownloadPdf}
                disabled={isDownloadingPdf}
              >
                <span className="flex items-center justify-center gap-2">
                  <Download size={16} />
                  {isDownloadingPdf ? "Generando PDF..." : "Descargar PDF"}
                </span>
              </Button>
            </>
          )}
          {downloadError && (
            <p className="text-sm text-danger">{downloadError}</p>
          )}

          <Button type="button" variant="secondary" onClick={onCreateNewOrder}>
            <span className="flex items-center justify-center gap-2">
              <PlusCircle size={16} />
              Crear nueva orden
            </span>
          </Button>
        </div>
      </div>

      <div className="mt-6 grid grid-cols-1 gap-3 text-sm sm:grid-cols-4">
        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Orden ID</p>
          <p className="text-lg font-semibold text-text-main">
            {isTemporaryOrder ? "Temporal" : `#${order.id}`}
          </p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Estado</p>
          <p className="text-lg font-semibold text-text-main">{order.status}</p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Tragos</p>
          <p className="text-lg font-semibold text-primary">{totalDrinks}</p>
        </div>

        <div className="rounded-control border border-success/20 bg-background/70 p-3">
          <p className="text-text-muted">Productos</p>
          <p className="text-lg font-semibold text-text-main">{itemCount}</p>
        </div>
      </div>

      <p className="mt-4 text-sm text-text-muted">
        Cócteles incluidos:{" "}
        <span className="font-semibold text-text-main">{cocktailCount}</span>
      </p>
    </Card>
  );
}
