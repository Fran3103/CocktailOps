import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { useAuth } from "../auth/useAuth";
import { cocktailService } from "../cocktails/cocktailService";
import type { Cocktail } from "../cocktails/cocktail.types";
import { CocktailSelector } from "./components/CocktailSelector";
import { CreatedOrderSummary } from "./components/CreatedOrderSummary";
import { EventDetailsForm } from "./components/EventDetailsForm";
import { GuestModeNotice } from "./components/GuestModeNotice";
import { OrderSummaryPanel } from "./components/OrderSummaryPanel";
import { SelectedCocktailsList } from "./components/SelectedCocktailsList";
import { orderService } from "./orderService";
import type {
  CreateTimeOrderRequest,
  OrderResponse,
  SelectedOrderCocktail,
} from "./order.types";

export function CreateOrderPage() {
  const { isAuthenticated } = useAuth();

  const [guests, setGuests] = useState("");
  const [durationHours, setDurationHours] = useState("");

  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [selectedCocktails, setSelectedCocktails] = useState<
    SelectedOrderCocktail[]
  >([]);

  const [isLoadingCocktails, setIsLoadingCocktails] = useState(true);
  const [cocktailsError, setCocktailsError] = useState<string | null>(null);

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [createdOrder, setCreatedOrder] = useState<OrderResponse | null>(null);

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

  function handleAddCocktail(cocktail: Cocktail) {
    const cocktailId = cocktail.id;

    const alreadySelected = selectedCocktails.some(
      (selectedCocktail) => selectedCocktail.cocktailId === cocktailId
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
      },
    ]);

    setCreatedOrder(null);
    setSubmitError(null);
  }

  function handleWeightChange(cocktailId: number, weight: number) {
    const safeWeight = Number.isNaN(weight) || weight < 1 ? 1 : weight;

    setSelectedCocktails((currentCocktails) =>
      currentCocktails.map((cocktail) =>
        cocktail.cocktailId === cocktailId
          ? { ...cocktail, weight: safeWeight }
          : cocktail
      )
    );

    setCreatedOrder(null);
    setSubmitError(null);
  }

  function handleRemoveCocktail(cocktailId: number) {
    setSelectedCocktails((currentCocktails) =>
      currentCocktails.filter((cocktail) => cocktail.cocktailId !== cocktailId)
    );

    setCreatedOrder(null);
    setSubmitError(null);
  }

  const payload = useMemo<CreateTimeOrderRequest | null>(() => {
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

  async function handleCreateOrder() {
    if (!payload) {
      setSubmitError(
        "Completá invitados, duración y al menos un cóctel para crear la orden."
      );
      return;
    }

    if (!isAuthenticated) {
      setSubmitError("Necesitás iniciar sesión para crear y guardar la orden.");
      return;
    }

    setIsSubmitting(true);
    setSubmitError(null);
    setCreatedOrder(null);

    try {
      const order = await orderService.createTimeOrder(payload);
      console.log("Orden creada:", order);
      setCreatedOrder(order);
    } catch(error) {
      console.error("Error al crear la orden:", error);
      console.log("Payload enviado:", payload);
      setSubmitError(
        "No se pudo crear la orden. Revisá los datos o intentá nuevamente."
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="space-y-6">
      <PageHeader
        title="Nueva orden"
        description="Armá una orden por invitados, duración del evento y cócteles seleccionados."
      />

      {!isAuthenticated && <GuestModeNotice />}

      {createdOrder && <CreatedOrderSummary order={createdOrder} />}

      <Card className="space-y-4">
        <div>
          <h2 className="font-heading text-xl font-semibold text-text-main">
            Datos del evento
          </h2>

          <p className="mt-1 text-sm text-text-muted">
            Esta primera versión usa el modo TIME del backend.
          </p>
        </div>

        <EventDetailsForm
          guests={guests}
          durationHours={durationHours}
          onGuestsChange={(value) => {
            setGuests(value);
            setCreatedOrder(null);
            setSubmitError(null);
          }}
          onDurationHoursChange={(value) => {
            setDurationHours(value);
            setCreatedOrder(null);
            setSubmitError(null);
          }}
        />
      </Card>

      <div className="grid grid-cols-1 gap-6 xl:grid-cols-[1fr_380px]">
        <div className="space-y-6">
          <Card className="space-y-4">
            <div>
              <h2 className="font-heading text-xl font-semibold text-text-main">
                Cócteles seleccionados
              </h2>

              <p className="mt-1 text-sm text-text-muted">
                El peso define la importancia relativa de cada cóctel.
              </p>
            </div>

            <SelectedCocktailsList
              selectedCocktails={selectedCocktails}
              onWeightChange={handleWeightChange}
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

        <div className="xl:sticky xl:top-8 xl:self-start">
          <OrderSummaryPanel
            guests={guests}
            durationHours={durationHours}
            selectedCocktails={selectedCocktails}
            payload={payload}
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
            {isSubmitting ? "Creando orden..." : "Crear orden"}
          </Button>
        </div>
      </div>
    </section>
  );
}