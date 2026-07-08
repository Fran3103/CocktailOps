type PageHeaderProps = {
  title: string;
  description?: string;
};

export function PageHeader({ title, description }: PageHeaderProps) {
  return (
    <header>
      <h1 className="font-heading text-3xl font-bold text-text-main">
        {title}
      </h1>

      {description && (
        <p className="mt-2 max-w-2xl text-text-muted">{description}</p>
      )}
    </header>
  );
}