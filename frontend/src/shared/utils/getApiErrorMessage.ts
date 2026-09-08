import { isAxiosError } from "axios";

type ApiErrorMessages = {
  defaultMessage?: string;
  networkMessage?: string;
  badRequestMessage?: string;
  unauthorizedMessage?: string;
  forbiddenMessage?: string;
  notFoundMessage?: string;
  serverMessage?: string;
};

export function getApiErrorMessage(
  error: unknown,
  messages: ApiErrorMessages = {},
) {
  const {
    defaultMessage = "Ocurrió un error inesperado.",
    networkMessage = "No se pudo conectar con el servidor. Revisá tu conexión o intentá nuevamente.",
    badRequestMessage = "La solicitud tiene datos inválidos.",
    unauthorizedMessage = "Tu sesión no está activa o venció. Iniciá sesión nuevamente.",
    forbiddenMessage = "No tenés permisos para realizar esta acción.",
    notFoundMessage = "No se encontró el recurso solicitado.",
    serverMessage = "Ocurrió un error en el servidor. Intentá nuevamente más tarde.",
  } = messages;

  if (!isAxiosError(error)) {
    return defaultMessage;
  }

  if (!error.response) {
    return networkMessage;
  }

  const status = error.response.status;

  if (status === 400) {
    return badRequestMessage;
  }

  if (status === 401) {
    return unauthorizedMessage;
  }

  if (status === 403) {
    return forbiddenMessage;
  }

  if (status === 404) {
    return notFoundMessage;
  }

  if (status >= 500) {
    return serverMessage;
  }

  return defaultMessage;
}