type StepHeaderProps = {
  step: string;
  title: string;
  description: string;
};

export function StepHeader({ step, title, description }: StepHeaderProps) {
  return (
    <div className="flex flex-col gap-2">
      <span className="w-fit rounded-full border border-primary/30 bg-primary/10 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-primary">
        {step}
      </span>

      <div>
        <h2 className="font-heading text-xl font-semibold text-text-main">
          {title}
        </h2>

        <p className="mt-1 text-sm leading-6 text-text-muted">
          {description}
        </p>
      </div>
    </div>
  );
}
