import { useEffect, useMemo, useRef, useState } from "react";

import { SuccessToast } from "../../shared/components/feedback/SuccessToast";
import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { useAuth } from "../auth/useAuth";
import { cocktailService } from "../cocktails/cocktailService";
import type { Cocktail } from "../cocktails/cocktail.types";
import { CocktailSelector } from "./components/CocktailSelector";
import { CreatedOrderSummary } from "./components/CreatedOrderSummary";
import { DrinksDetailsForm } from "./components/DrinksDetailsForm";
import { EventDetailsForm } from "./components/EventDetailsForm";
import { GuestModeNotice } from "./components/GuestModeNotice";
import { OrderModeSelector } from "./components/OrderModeSelector";
import { OrderPresetSelector } from "./components/OrderPresetSelector";
import { OrderSummaryPanel } from "./components/OrderSummaryPanel";
import { SelectedCocktailsList } from "./components/SelectedCocktailsList";
import { CalculationNotice } from "./components/CalculationNotice";
import { orderPresets, type OrderPreset } from "./orderPresets";
import {
  buildSelectedCocktailsFromPreset,
  distributeQuantitiesByWeight,
} from "./orderPresetUtils";
import { orderService } from "./orderService";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderMode,
  OrderResponse,
  SelectedOrderCocktail,
} from "./order.types";

export function CreateOrderPage() {
  const { isAuthenticated } = useAuth();

  const createdOrderRef = useRef<HTMLDivElement | null>(null);

  const [orderMode, setOrderMode] = useState<OrderMode>("TIME");

  const [guests, setGuests] = useState("");
  const [durationHours, setDurationHours] = useState("");
  const [totalDrinks, setTotalDrinks] = useState("");

  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [selectedCocktails, setSelectedCocktails] = useState<
    SelectedOrderCocktail[]
  >([]);
  const [selectedPresetId, setSelectedPresetId] = useState<string | null>(null);

  const [isLoadingCocktails, setIsLoadingCocktails] = useState(true);
  const [cocktailsError, setCocktailsError] = useState<string | null>(null);

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const [createdOrder, setCreatedOrder] = useState<OrderResponse | null>(null);
  const [showSuccessToast, setShowSuccessToast] = useState(false);

  const [createdOrderTimePayload, setCreatedOrderTimePayload] =
    useState<CreateTimeOrderRequest | null>(null);

  const [createdOrderDrinksPayload, setCreatedOrderDrinksPayload] =
    useState<CreateDrinksOrderRequest | null>(null);

  useEffect(() => {
    let ignore = false;

    async function fetchCocktails() {
      try {
        const data = await cocktailService.getAll();

        if (!ignore) {
          setCocktails(data);
          setCocktailsError(null);
        }
      } catch {
        if (!ignore) {
          setCocktailsError("No se pudieron cargar los cócteles.");
        }
      } finally {
        if (!ignore) {
          setIsLoadingCocktails(false);
        }
      }
    }

    void fetchCocktails();

    return () => {
      ignore = true;
    };
  }, []);

  useEffect(() => {
    if (createdOrder) {
      createdOrderRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }
  }, [createdOrder]);

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

  function handleOrderModeChange(mode: OrderMode) {
    setOrderMode(mode);
    clearResultState();
  }

  function handleApplyPreset(preset: OrderPreset) {
    clearResultState();

    const { selectedCocktails: presetSelectedCocktails, missingCocktailNames } =
      buildSelectedCocktailsFromPreset(preset, cocktails);

    if (presetSelectedCocktails.length === 0) {
      setSubmitError(
        "No se pudo cargar la lista porque sus cócteles no están disponibles en el catálogo.",
      );
      return;
    }

    const numericTotalDrinks = Number(totalDrinks);

    const shouldDistributeByTotalDrinks =
      orderMode === "DRINKS" &&
      numericTotalDrinks >= presetSelectedCocktails.length;

    const nextSelectedCocktails = shouldDistributeByTotalDrinks
      ? distributeQuantitiesByWeight(
          presetSelectedCocktails,
          numericTotalDrinks,
        )
      : presetSelectedCocktails;

    setSelectedCocktails(nextSelectedCocktails);
    setSelectedPresetId(preset.id);

    if (missingCocktailNames.length > 0) {
      setSubmitError(
        `La lista se cargó parcialmente. Faltan en el catálogo: ${missingCocktailNames.join(", ")}.`,
      );
    }
  }

  function handleAddCocktail(cocktail: Cocktail) {
    const cocktailId = cocktail.id;

    const alreadySelected = selectedCocktails.some(
      (selectedCocktail) => selectedCocktail.cocktailId === cocktailId,
    );

    if (alreadySelected) {
      return;
    }

    setSelectedCocktails((currentCocktails) => [
      ...currentCocktails,
      {
        cocktailId,
        cocktailName: cocktail.name,
        weight: 1,
        quantity: 1,
      },
    ]);

    setSelectedPresetId(null);
    clearResultState();
  }

  function handleWeightChange(cocktailId: number, weight: number) {
    const safeWeight = Number.isNaN(weight) || weight < 1 ? 1 : weight;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail) =>
        cocktail.cocktailId === cocktailId
          ? { ...cocktail, weight: safeWeight }
          : cocktail,
      ),
    );

    setSelectedPresetId(null);
    clearResultState();
  }

  function handleQuantityChange(cocktailId: number, quantity: number) {
    const safeQuantity = Number.isNaN(quantity) || quantity < 1 ? 1 : quantity;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail) =>
        cocktail.cocktailId === cocktailId
          ? { ...cocktail, quantity: safeQuantity }
          : cocktail,
      ),
    );

    setSelectedPresetId(null);
    clearResultState();
  }

  function handleRemoveCocktail(cocktailId: number) {
    setSelectedCocktails((currentCocktails) =>
      currentCocktails.filter((cocktail) => cocktail.cocktailId !== cocktailId),
    );

    setSelectedPresetId(null);
    clearResultState();
  }

  const assignedDrinks = useMemo(() => {
    return selectedCocktails.reduce(
      (total, cocktail) => total + cocktail.quantity,
      0,
    );
  }, [selectedCocktails]);

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

    setSelectedCocktails((currentCocktails) =>
      distributeQuantitiesByWeight(currentCocktails, numericTotalDrinks),
    );
  }

  function handleDistributeEqually() {
    const numericTotalDrinks = Number(totalDrinks);

    if (numericTotalDrinks <= 0) {
      setSubmitError("Indicá una cantidad total de tragos mayor a 0.");
      return;
    }

    if (selectedCocktails.length === 0) {
      setSubmitError("Seleccioná al menos un cóctel para dividir la cantidad.");
      return;
    }

    if (numericTotalDrinks < selectedCocktails.length) {
      setSubmitError(
        "El total de tragos debe ser mayor o igual a la cantidad de cócteles seleccionados.",
      );
      return;
    }

    const baseQuantity = Math.floor(
      numericTotalDrinks / selectedCocktails.length,
    );

    const remainder = numericTotalDrinks % selectedCocktails.length;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail, index) => ({
        ...cocktail,
        quantity: index < remainder ? baseQuantity + 1 : baseQuantity,
      })),
    );

    setSelectedPresetId(null);
    clearResultState();
  }

  const timePayload = useMemo<CreateTimeOrderRequest | null>(() => {
    const numericGuests = Number(guests);
    const numericDurationHours = Number(durationHours);

    if (
      numericGuests <= 0 ||
      numericDurationHours <= 0 ||
      selectedCocktails.length === 0
    ) {
      return null;
    }

    return {
      guests: numericGuests,
      durationHours: numericDurationHours,
      cocktails: selectedCocktails.map((cocktail) => ({
        cocktailId: cocktail.cocktailId,
        weight: cocktail.weight,
      })),
    };
  }, [guests, durationHours, selectedCocktails]);

  const drinksPayload = useMemo<CreateDrinksOrderRequest | null>(() => {
    const numericTotalDrinks = Number(totalDrinks);

    if (
      numericTotalDrinks <= 0 ||
      selectedCocktails.length === 0 ||
      assignedDrinks !== numericTotalDrinks
    ) {
      return null;
    }

    return {
      totalDrinks: numericTotalDrinks,
      cocktails: selectedCocktails.map((cocktail) => ({
        cocktailId: cocktail.cocktailId,
        quantity: cocktail.quantity,
      })),
    };
  }, [totalDrinks, selectedCocktails, assignedDrinks]);

  const currentPayload = orderMode === "TIME" ? timePayload : drinksPayload;

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
        if (!isAuthenticated) {
          const order = await orderService.createTimePreview(timePayload);
          setCreatedOrder(order);
        } else {
          const order = await orderService.createTimeOrder(timePayload);
          setCreatedOrder(order);
        }

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
      } else if (selectedCocktails.length === 0) {
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
      let order: OrderResponse;

      if (!isAuthenticated) {
        order = await orderService.createDrinksPreview(drinksPayload);
      } else {
        order = await orderService.createDrinksOrder(drinksPayload);
      }

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

  function handleCreateNewOrder() {
    setOrderMode("TIME");
    setGuests("");
    setDurationHours("");
    setTotalDrinks("");
    setSelectedCocktails([]);
    setSelectedPresetId(null);
    setSubmitError(null);
    setCreatedOrder(null);
    setCreatedOrderTimePayload(null);
    setCreatedOrderDrinksPayload(null);
    setShowSuccessToast(false);
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

      <PageHeader
        title="Nueva orden"
        description="Armá una orden por evento o por cantidad total de tragos."
      />

      {!isAuthenticated && <GuestModeNotice />}

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

      <Card className="space-y-4">
        <div>
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Modo de cálculo
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Elegí cómo querés calcular la orden.
          </p>
        </div>

        <OrderModeSelector value={orderMode} onChange={handleOrderModeChange} />
      </Card>

      <Card className="space-y-4">
        <div>
          <h2 className="font-heading text-xl font-semibold text-text-main">
            {orderMode === "TIME" ? "Datos del evento" : "Cantidad de tragos"}
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            {orderMode === "TIME"
              ? "Calcula la orden según invitados, duración y peso de cada cóctel."
              : "Calcula la orden según la cantidad total de tragos y la cantidad elegida por cóctel."}
          </p>
        </div>

        {orderMode === "TIME" ? (
          <EventDetailsForm
            guests={guests}
            durationHours={durationHours}
            onGuestsChange={(value) => {
              setGuests(value);
              clearResultState();
            }}
            onDurationHoursChange={(value) => {
              setDurationHours(value);
              clearResultState();
            }}
          />
        ) : (
          <DrinksDetailsForm
            totalDrinks={totalDrinks}
            assignedDrinks={assignedDrinks}
            selectedCocktailsCount={selectedCocktails.length}
            onTotalDrinksChange={handleTotalDrinksChange}
            onDistributeEqually={handleDistributeEqually}
          />
        )}
      </Card>

      {!isLoadingCocktails && !cocktailsError && (
        <OrderPresetSelector
          presets={orderPresets}
          selectedPresetId={selectedPresetId}
          onSelectPreset={handleApplyPreset}
        />
      )}

      <div className="grid grid-cols-1 gap-6 xl:grid-cols-[1fr_380px]">
        <div className="space-y-6">
          <Card className="space-y-4">
            <div>
              <h2 className="font-heading text-xl font-semibold text-text-main">
                Cócteles seleccionados
              </h2>

              <p className="mt-1 text-sm text-text-muted">
                {orderMode === "TIME"
                  ? "El peso define la importancia relativa de cada cóctel."
                  : "La cantidad define cuántos tragos de cada cóctel se calcularán."}
              </p>
            </div>

            <SelectedCocktailsList
              orderMode={orderMode}
              selectedCocktails={selectedCocktails}
              onWeightChange={handleWeightChange}
              onQuantityChange={handleQuantityChange}
              onRemoveCocktail={handleRemoveCocktail}
            />
          </Card>

          <div className="space-y-4">
            <div>
              <h2 className="font-heading text-xl font-semibold text-text-main">
                Catálogo de cócteles
              </h2>

              <p className="mt-1 text-sm text-text-muted">
                Elegí los cócteles que formarán parte de la orden.
              </p>
            </div>

            {isLoadingCocktails && (
              <Card>
                <p className="text-text-muted">Cargando cócteles...</p>
              </Card>
            )}

            {!isLoadingCocktails && cocktailsError && (
              <Card>
                <p className="text-danger">{cocktailsError}</p>
              </Card>
            )}

            {!isLoadingCocktails && !cocktailsError && (
              <CocktailSelector
                cocktails={cocktails}
                selectedCocktails={selectedCocktails}
                onAddCocktail={handleAddCocktail}
              />
            )}
          </div>
        </div>
       
        <div className="xl:sticky xl:top-8 xl:self-start gap-4 flex flex-col">
            <CalculationNotice  />
          <OrderSummaryPanel
            orderMode={orderMode}
            guests={guests}
            durationHours={durationHours}
            totalDrinks={totalDrinks}
            assignedDrinks={assignedDrinks}
            selectedCocktails={selectedCocktails}
            payload={currentPayload}
          />

          {submitError && (
            <Card className="mt-4 border-danger/40">
              <p className="text-sm text-danger">{submitError}</p>
            </Card>
          )}

          <Button
            type="button"
            fullWidth
            className="mt-4"
            onClick={handleCreateOrder}
            disabled={isSubmitting}
          >
            {isSubmitting
              ? "Generando orden..."
              : isAuthenticated
                ? "Crear y guardar orden"
                : "Generar orden"}
          </Button>
        </div>
      </div>
    </section>
  );
}
