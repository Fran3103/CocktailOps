import { Download } from "lucide-react";
import { useState } from "react";
import { isAxiosError } from "axios";

import { Button } from "../../../shared/components/ui/Button";
import { downloadBlobFile } from "../../../shared/utils/downloadBlobFile";

import { orderService } from "../orderService";
import type { PdfSource } from "../order.types";

type ButtonVariant = "primary" | "secondary" | "ghost";

type OrderPdfDownloadButtonProps = {
  source: PdfSource;
  label?: string;
  loadingLabel?: string;
  variant?: ButtonVariant;
  fullWidth?: boolean;
  className?: string;
};

function getPdfFilename(source: PdfSource) {
  if (source.type === "SAVED_ORDER") {
    return `order-${source.orderId}.pdf`;
  }

  return "order-preview.pdf";
}

async function getPdfBlob(source: PdfSource) {
  if (source.type === "SAVED_ORDER") {
    return orderService.downloadPdf(source.orderId);
  }

  if (source.type === "TIME_PREVIEW") {
    return orderService.downloadTimePreviewPdf(source.payload);
  }

  return orderService.downloadDrinksPreviewPdf(source.payload);
}

function getPdfDownloadErrorMessage(error: unknown) {
  if (!isAxiosError(error)) {
    return "No se pudo descargar el PDF.";
  }

  const status = error.response?.status;

  if (status === 400) {
    return "No se pudo generar el PDF porque los datos de la orden son inválidos.";
  }

  if (status === 401) {
    return "Tu sesión no está activa o venció. Iniciá sesión nuevamente.";
  }

  if (status === 403) {
    return "No tenés permisos para descargar este PDF.";
  }

  if (status === 404) {
    return "No se encontró la orden solicitada.";
  }

  if (status && status >= 500) {
    return "Ocurrió un error en el servidor al generar el PDF.";
  }

  return "No se pudo descargar el PDF.";
}

export function OrderPdfDownloadButton({
  source,
  label = "Descargar PDF",
  loadingLabel = "Descargando...",
  variant = "primary",
  fullWidth = false,
  className = "",
}: OrderPdfDownloadButtonProps) {
  const [isDownloading, setIsDownloading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleDownloadPdf() {
    setIsDownloading(true);
    setError(null);

    try {
      const pdfBlob = await getPdfBlob(source);
      downloadBlobFile(pdfBlob, getPdfFilename(source));
    } catch (downloadError) {
      setError(getPdfDownloadErrorMessage(downloadError));
    } finally {
      setIsDownloading(false);
    }
  }

  return (
    <div>
      <Button
        type="button"
        variant={variant}
        fullWidth={fullWidth}
        onClick={handleDownloadPdf}
        disabled={isDownloading}
        className={className}
      >
        <span className="flex items-center justify-center gap-2">
          <Download size={16} />
          {isDownloading ? loadingLabel : label}
        </span>
      </Button>

      {error && <p className="mt-2 text-sm text-danger">{error}</p>}
    </div>
  );
}
