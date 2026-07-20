import { Download } from "lucide-react";
import { useState } from "react";

import { Button } from "../../../shared/components/ui/Button";
import { orderService } from "../orderService";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
} from "../order.types";

type OrderPdfDownloadButtonProps = {
  orderId: number;
  isGuestOrder: boolean;
  timePreviewPayload?: CreateTimeOrderRequest | null;
  drinksPreviewPayload?: CreateDrinksOrderRequest | null;
};

export function OrderPdfDownloadButton({
  orderId,
  isGuestOrder,
  timePreviewPayload,
  drinksPreviewPayload,
}: OrderPdfDownloadButtonProps) {
  const [isDownloading, setIsDownloading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function getPdfBlob() {
    if (timePreviewPayload) {
      return orderService.downloadTimePreviewPdf(timePreviewPayload);
    }

    if (drinksPreviewPayload) {
      return orderService.downloadDrinksPreviewPdf(drinksPreviewPayload);
    }

    if (isGuestOrder) {
      throw new Error("Missing guest preview payload");
    }

    return orderService.downloadPdf(orderId);
  }

  async function handleDownloadPdf() {
    setIsDownloading(true);
    setError(null);

    try {
      const pdfBlob = await getPdfBlob();

      const fileUrl = window.URL.createObjectURL(pdfBlob);

      const link = document.createElement("a");
      link.href = fileUrl;
      link.download = `order-${orderId}.pdf`;

      document.body.appendChild(link);
      link.click();
      link.remove();

      window.URL.revokeObjectURL(fileUrl);
    } catch {
      setError("No se pudo descargar el PDF.");
    } finally {
      setIsDownloading(false);
    }
  }

  return (
    <div>
      <Button
        type="button"
        onClick={handleDownloadPdf}
        disabled={isDownloading}
      >
        <span className="flex items-center justify-center gap-2">
          <Download size={16} />
          {isDownloading ? "Descargando..." : "Descargar PDF"}
        </span>
      </Button>

      {error && <p className="mt-2 text-sm text-danger">{error}</p>}
    </div>
  );
}