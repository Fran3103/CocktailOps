type PagePlaceholderProps = {
  title: string;
  description: string;
};

export const PagePlaceholder = ({
  title,
  description,
}: PagePlaceholderProps) => {
  return (
    <section>
      <h2 className="font-heading text-3xl font-bold text-text-main">
        {title}
      </h2>
      <p className="text-lg text-text-muted">{description}</p>

      <div className="mt-6 rounded-card border border-border-soft  bg-surface-soft p-6">
        <p className="text-text-muted">Esta es una página continua...</p>
      </div>
    </section>
  );
};
