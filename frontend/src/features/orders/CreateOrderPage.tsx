import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { cocktailService } from "../cocktails/cocktailService";
import type { Cocktail } from "../cocktails/cocktail.types";
import { CocktailSelector } from "./components/CocktailSelector";
import { EventDetailsForm } from "./components/EventDetailsForm";
import { OrderSummaryPanel } from "./components/OrderSummaryPanel";
import { SelectedCocktailsList } from "./components/SelectedCocktailsList";
import type {
  CreateTimeOrderRequest,
  SelectedOrderCocktail,
} from "./order.types";

export function CreateOrderPage() {
  const [guests, setGuests] = useState("");
  const [durationHours, setDurationHours] = useState("");

  const [cocktails, setCocktails] = useState<Cocktail[]>([]);
  const [selectedCocktails, setSelectedCocktails] = useState<
    SelectedOrderCocktail[]
  >([]);

  const [isLoadingCocktails, setIsLoadingCocktails] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let ignore = false;

    async function fetchCocktails() {
      try {
        const data = await cocktailService.getAll();

        if (!ignore) {
          setCocktails(data);
          setError(null);
        }
      } catch {
        if (!ignore) {
          setError("No se pudieron cargar los cócteles.");
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
  }

  function handleRemoveCocktail(cocktailId: number) {
    setSelectedCocktails((currentCocktails) =>
      currentCocktails.filter((cocktail) => cocktail.cocktailId !== cocktailId)
    );
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

  function handleCreateOrderPreview() {
    if (!payload) {
      setError(
        "Completá invitados, duración y al menos un cóctel para crear la orden."
      );
      return;
    }

    setError(null);
    console.log("Create order payload:", payload);
  }

  return (
    <section className="space-y-6">
      <PageHeader
        title="Nueva orden"
        description="Armá una orden por invitados, duración del evento y cócteles seleccionados."
      />

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
          onGuestsChange={setGuests}
          onDurationHoursChange={setDurationHours}
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

            {!isLoadingCocktails && error && (
              <Card>
                <p className="text-danger">{error}</p>
              </Card>
            )}

            {!isLoadingCocktails && !error && (
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

          <Button
            type="button"
            fullWidth
            className="mt-4"
            onClick={handleCreateOrderPreview}
          >
            Crear orden
          </Button>
        </div>
      </div>
    </section>
  );
}