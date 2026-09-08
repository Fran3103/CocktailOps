import { useEffect, useRef, useState } from "react";

import { useAuth } from "../auth/useAuth";

import { useOrderCocktailSelection } from "./hooks/useOrderCocktailSelection";
import { useOrderCocktailsCatalog } from "./hooks/useOrderCocktailsCatalog";
import { useOrderCreationState } from "./hooks/useOrderCreationState";
import { useOrderPayloads } from "./hooks/useOrderPayloads";
import { useCreateOrder } from "./hooks/useCreateOrder";

import { PageHeader } from "../../shared/components/ui/PageHeader";
import { SuccessToast } from "../../shared/components/feedback/SuccessToast";
import { CreatedOrderSummary } from "./components/CreatedOrderSummary";
import { OrderCocktailsSection } from "./components/OrderCocktailsSection";
import { GuestModeNotice } from "./components/GuestModeNotice";
import { OrderDetailsSection } from "./components/OrderDetailsSection";
import { OrderReviewPanel } from "./components/OrderReviewPanel";

import type { OrderWizardStep } from "./orderWizard.types";
import { orderPresets } from "./orderPresets";
import type { OrderMode } from "./order.types";

export function CreateOrderPage() {
  const { isAuthenticated } = useAuth();
  const {
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
  } = useOrderCreationState();

  const createdOrderRef = useRef<HTMLDivElement | null>(null);

  const [orderMode, setOrderMode] = useState<OrderMode>("TIME");
  const [currentStep, setCurrentStep] = useState<OrderWizardStep>("DETAILS");
  const [wizardError, setWizardError] = useState<string | null>(null);

  const [guests, setGuests] = useState("");
  const [durationHours, setDurationHours] = useState("");
  const [totalDrinks, setTotalDrinks] = useState("");

  const { cocktails, isLoadingCocktails, cocktailsError } =
    useOrderCocktailsCatalog();

  const {
    selectedCocktails,
    selectedPresetId,
    assignedDrinks,
    resetCocktailSelection,
    handleApplyPreset,
    handleAddCocktail,
    handleWeightChange,
    handleQuantityChange,
    handleRemoveCocktail,
    handleDistributeEqually,
    distributeSelectedCocktailsByTotalDrinks,
  } = useOrderCocktailSelection({
    cocktails,
    orderMode,
    totalDrinks,
    onClearResult: clearResultState,
    onSubmitError: setSubmitError,
  });

  useEffect(() => {
    if (createdOrder) {
      createdOrderRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  }, [createdOrder]);

  function handleOrderModeChange(mode: OrderMode) {
    setOrderMode(mode);
    clearResultState();
  }

  function handleTotalDrinksChange(value: string) {
    setTotalDrinks(value);
    clearResultState();

    const numericTotalDrinks = Number(value);

    if (
      !selectedPresetId ||
      orderMode !== "DRINKS" ||
      numericTotalDrinks < selectedCocktails.length
    ) {
      return;
    }

    distributeSelectedCocktailsByTotalDrinks(numericTotalDrinks);
  }

  const { timePayload, drinksPayload, currentPayload } = useOrderPayloads({
    orderMode,
    guests,
    durationHours,
    totalDrinks,
    assignedDrinks,
    selectedCocktails,
  });

  const { handleCreateOrder } = useCreateOrder({
    orderMode,
    isAuthenticated,
    timePayload,
    drinksPayload,
    totalDrinks,
    selectedCocktailsCount: selectedCocktails.length,
    assignedDrinks,
    setIsSubmitting,
    setSubmitError,
    setCreatedOrder,
    setCreatedOrderTimePayload,
    setCreatedOrderDrinksPayload,
    setShowSuccessToast,
  });

  const isDetailsStepComplete =
    orderMode === "TIME"
      ? Number(guests) > 0 && Number(durationHours) > 0
      : Number(totalDrinks) > 0;

  const isCocktailsStepComplete = selectedCocktails.length > 0;

  function scrollToTop() {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }

  function handleNextStep() {
    setSubmitError(null);
    setWizardError(null);

    if (currentStep === "DETAILS") {
      if (!isDetailsStepComplete) {
        setWizardError(
          orderMode === "TIME"
            ? "Completá invitados y duración para continuar."
            : "Indicá una cantidad total de tragos para continuar.",
        );
        return;
      }

      setCurrentStep("COCKTAILS");
      scrollToTop();
      return;
    }

    if (currentStep === "COCKTAILS") {
      if (!isCocktailsStepComplete) {
        setWizardError("Seleccioná al menos un cóctel para continuar.");
        return;
      }

      if (orderMode === "DRINKS" && !drinksPayload) {
        setWizardError(
          `El total asignado debe coincidir con el total de tragos. Actualmente asignaste ${assignedDrinks} de ${totalDrinks || "-"}.`,
        );
        return;
      }

      setCurrentStep("SUMMARY");
      scrollToTop();
    }
  }

  function handlePreviousStep() {
    setSubmitError(null);
    setWizardError(null);

    if (currentStep === "SUMMARY") {
      setCurrentStep("COCKTAILS");
      scrollToTop();
      return;
    }

    if (currentStep === "COCKTAILS") {
      setCurrentStep("DETAILS");
      scrollToTop();
    }
  }
  function handleCreateNewOrder() {
    setOrderMode("TIME");
    setCurrentStep("DETAILS");
    setGuests("");
    setDurationHours("");
    setTotalDrinks("");
    setWizardError(null);
    resetCocktailSelection();
    resetCreationState();
  }
  return (
    <section className="space-y-6">
      {showSuccessToast && createdOrder && (
        <SuccessToast
          title="Orden creada"
          message={
            createdOrder.id == null
              ? "La orden temporal se generó correctamente."
              : `La orden #${createdOrder.id} se generó correctamente.`
          }
          onClose={() => setShowSuccessToast(false)}
        />
      )}

      <div className="mx-auto w-full max-w-6xl space-y-4">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
          <PageHeader
            title="Nueva orden"
            description="Configurá un evento, seleccioná cócteles y generá la lista de insumos necesaria."
          />

          <div className="hidden rounded-full border border-primary/30 bg-primary/10 px-4 py-2 text-sm font-medium text-primary lg:block">
            Flujo guiado de cálculo
          </div>
        </div>

        {!isAuthenticated && <GuestModeNotice />}
      </div>

      {createdOrder && (
        <div ref={createdOrderRef}>
          <CreatedOrderSummary
            order={createdOrder}
            onCreateNewOrder={handleCreateNewOrder}
            timePreviewPayload={
              createdOrder.id == null ? createdOrderTimePayload : null
            }
            drinksPreviewPayload={
              createdOrder.id == null ? createdOrderDrinksPayload : null
            }
          />
        </div>
      )}

      {!createdOrder && (
        <div className="mx-auto w-full max-w-6xl space-y-6">
          {currentStep === "DETAILS" && (
            <OrderDetailsSection
              orderMode={orderMode}
              guests={guests}
              durationHours={durationHours}
              totalDrinks={totalDrinks}
              assignedDrinks={assignedDrinks}
              selectedCocktailsCount={selectedCocktails.length}
              error={wizardError}
              onOrderModeChange={handleOrderModeChange}
              onGuestsChange={(value) => {
                setGuests(value);
                clearResultState();
              }}
              onDurationHoursChange={(value) => {
                setDurationHours(value);
                clearResultState();
              }}
              onTotalDrinksChange={handleTotalDrinksChange}
              onDistributeEqually={handleDistributeEqually}
              onNext={handleNextStep}
            />
          )}

          {currentStep === "COCKTAILS" && (
            <OrderCocktailsSection
              orderMode={orderMode}
              cocktails={cocktails}
              selectedCocktails={selectedCocktails}
              selectedPresetId={selectedPresetId}
              presets={orderPresets}
              isLoadingCocktails={isLoadingCocktails}
              cocktailsError={cocktailsError}
              onSelectPreset={handleApplyPreset}
              onAddCocktail={handleAddCocktail}
              onWeightChange={handleWeightChange}
              onQuantityChange={handleQuantityChange}
              onRemoveCocktail={handleRemoveCocktail}
              error={wizardError}
              onPrevious={handlePreviousStep}
              onNext={handleNextStep}
            />
          )}

          {currentStep === "SUMMARY" && (
            <OrderReviewPanel
              isAuthenticated={isAuthenticated}
              isSubmitting={isSubmitting}
              submitError={submitError}
              orderMode={orderMode}
              guests={guests}
              durationHours={durationHours}
              totalDrinks={totalDrinks}
              assignedDrinks={assignedDrinks}
              selectedCocktails={selectedCocktails}
              payload={currentPayload}
              onPrevious={handlePreviousStep}
              onCreateOrder={handleCreateOrder}
            />
          )}
        </div>
      )}
    </section>
  );
}
