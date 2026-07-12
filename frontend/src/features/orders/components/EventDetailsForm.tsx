import { Input } from "../../../shared/components/ui/Input";

type EventDetailsFormProps = {
  guests: string;
  durationHours: string;
  onGuestsChange: (value: string) => void;
  onDurationHoursChange: (value: string) => void;
};

export function EventDetailsForm({
  guests,
  durationHours,
  onGuestsChange,
  onDurationHoursChange,
}: EventDetailsFormProps) {
  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
      <Input
        label="Cantidad de invitados"
        type="number"
        min="1"
        value={guests}
        onChange={(event) => onGuestsChange(event.target.value)}
        placeholder="Ej: 80"
      />

      <Input
        label="Duración del evento en horas"
        type="number"
        min="1"
        value={durationHours}
        onChange={(event) => onDurationHoursChange(event.target.value)}
        placeholder="Ej: 5"
      />
    </div>
  );
}