import { orderService } from "../orderService";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  OrderResponse,
} from "../order.types";

type UseCreateOrderParams = {
  orderMode: OrderMode;
  isAuthenticated: boolean;
  timePayload: CreateTimeOrderRequest | null;
  drinksPayload: CreateDrinksOrderRequest | null;
  totalDrinks: string;
  selectedCocktailsCount: number;
  assignedDrinks: number;
  setIsSubmitting: (value: boolean) => void;
  setSubmitError: (value: string | null) => void;
  setCreatedOrder: (value: OrderResponse | null) => void;
  setCreatedOrderTimePayload: (value: CreateTimeOrderRequest | null) => void;
  setCreatedOrderDrinksPayload: (
    value: CreateDrinksOrderRequest | null,
  ) => void;
  setShowSuccessToast: (value: boolean) => void;
};

export function useCreateOrder({
  orderMode,
  isAuthenticated,
  timePayload,
  drinksPayload,
  totalDrinks,
  selectedCocktailsCount,
  assignedDrinks,
  setIsSubmitting,
  setSubmitError,
  setCreatedOrder,
  setCreatedOrderTimePayload,
  setCreatedOrderDrinksPayload,
  setShowSuccessToast,
}: UseCreateOrderParams) {
  async function handleCreateOrder() {
    setSubmitError(null);
    setCreatedOrder(null);
    setCreatedOrderTimePayload(null);
    setCreatedOrderDrinksPayload(null);

    if (orderMode === "TIME") {
      if (!timePayload) {
        setSubmitError(
          "Completá invitados, duración y al menos un cóctel para crear la orden.",
        );
        return;
      }

      setIsSubmitting(true);

      try {
        const order = isAuthenticated
          ? await orderService.createTimeOrder(timePayload)
          : await orderService.createTimePreview(timePayload);

        setCreatedOrder(order);
        setCreatedOrderTimePayload(timePayload);
        setCreatedOrderDrinksPayload(null);
        setShowSuccessToast(true);
      } catch {
        setSubmitError(
          "No se pudo crear la orden. Revisá los datos o intentá nuevamente.",
        );
      } finally {
        setIsSubmitting(false);
      }

      return;
    }

    if (!drinksPayload) {
      const numericTotalDrinks = Number(totalDrinks);

      if (numericTotalDrinks <= 0) {
        setSubmitError("Indicá una cantidad total de tragos mayor a 0.");
      } else if (selectedCocktailsCount === 0) {
        setSubmitError("Seleccioná al menos un cóctel para crear la orden.");
      } else {
        setSubmitError(
          `El total asignado debe ser igual al total de tragos. Actualmente asignaste ${assignedDrinks} de ${numericTotalDrinks}.`,
        );
      }

      return;
    }

    setIsSubmitting(true);

    try {
      const order = isAuthenticated
        ? await orderService.createDrinksOrder(drinksPayload)
        : await orderService.createDrinksPreview(drinksPayload);

      setCreatedOrder(order);
      setCreatedOrderTimePayload(null);
      setCreatedOrderDrinksPayload(drinksPayload);
      setShowSuccessToast(true);
    } catch {
      setSubmitError(
        "No se pudo crear la orden. Revisá los datos o intentá nuevamente.",
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return {
    handleCreateOrder,
  };
}