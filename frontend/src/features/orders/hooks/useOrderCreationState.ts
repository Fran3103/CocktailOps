import { useEffect, useState } from "react";

import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderResponse,
} from "../order.types";

export function useOrderCreationState() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const [createdOrder, setCreatedOrder] = useState<OrderResponse | null>(null);
  const [showSuccessToast, setShowSuccessToast] = useState(false);

  const [createdOrderTimePayload, setCreatedOrderTimePayload] =
    useState<CreateTimeOrderRequest | null>(null);

  const [createdOrderDrinksPayload, setCreatedOrderDrinksPayload] =
    useState<CreateDrinksOrderRequest | null>(null);

  useEffect(() => {
    if (!showSuccessToast) {
      return;
    }

    const timeoutId = window.setTimeout(() => {
      setShowSuccessToast(false);
    }, 3000);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [showSuccessToast]);

  function clearResultState() {
    setCreatedOrder(null);
    setCreatedOrderTimePayload(null);
    setCreatedOrderDrinksPayload(null);
    setSubmitError(null);
  }

  function resetCreationState() {
    setIsSubmitting(false);
    setSubmitError(null);
    setCreatedOrder(null);
    setCreatedOrderTimePayload(null);
    setCreatedOrderDrinksPayload(null);
    setShowSuccessToast(false);
  }

  return {
    isSubmitting,
    setIsSubmitting,
    submitError,
    setSubmitError,
    createdOrder,
    setCreatedOrder,
    showSuccessToast,
    setShowSuccessToast,
    createdOrderTimePayload,
    setCreatedOrderTimePayload,
    createdOrderDrinksPayload,
    setCreatedOrderDrinksPayload,
    clearResultState,
    resetCreationState,
  };
}