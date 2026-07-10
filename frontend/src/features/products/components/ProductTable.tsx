import type { Product } from "../product.types";
import { ProductRow } from "./ProductRow";

type ProductTableProps = {
  products: Product[];
};

export function ProductTable({ products }: ProductTableProps) {

  return (
    <div className="overflow-hidden rounded-card border border-border-soft bg-surface-soft">
      <div className="overflow-x-auto">
        <table className="w-full min-w-[760px] border-collapse">
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Producto
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Categoría
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Unidad
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Tamaño
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Estado
              </th>
            </tr>
          </thead>

          <tbody>
            {products.map((product) => (
              <ProductRow key={product.productId} product={product} />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}