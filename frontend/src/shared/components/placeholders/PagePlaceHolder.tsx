import { Card } from "../ui/Card";
import { PageHeader } from "../ui/PageHeader";

type PagePlaceholderProps = {
  title: string;
  description: string;
};

export function PagePlaceholder({ title, description }: PagePlaceholderProps) {
  return (
    <section>
      <PageHeader title={title} description={description} />

      <Card className="mt-6">
        <p className="text-text-muted">
          Esta pantalla está preparada para el próximo paso del sprint.
        </p>
      </Card>
    </section>
  );
}