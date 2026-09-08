import { Package } from "lucide-react";

import type { Product } from "../product.types";
import { ProductRow } from "./ProductRow";

type ProductTableProps = {
  products: Product[];
};

function getCategoryName(product: Product) {
  if (product.categoryName) {
    return product.categoryName;
  }

  const categoryId = product.categoryId ?? product.category;

  if (categoryId) {
    return `Categoría #${categoryId}`;
  }

  return "Sin categoría";
}

function getProductKey(product: Product) {
  return product.productId ?? product.name;
}

function getStatusLabel(product: Product) {
  return product.active === false ? "Inactivo" : "Activo";
}

export function ProductTable({ products }: ProductTableProps) {
  return (
    <div className="rounded-card border border-border-soft bg-surface-soft">
      <div className="space-y-3 p-4 lg:hidden">
        {products.map((product) => (
          <article
            key={getProductKey(product)}
            className="rounded-card border border-border-soft bg-background/30 p-4"
          >
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary">
                <Package size={18} />
              </div>

              <div className="min-w-0 flex-1">
                <div className="flex flex-wrap items-center gap-2">
                  <h3 className="font-semibold text-text-main">
                    {product.name}
                  </h3>

                  <span
                    className={`rounded-full border px-2 py-0.5 text-[11px] font-medium ${
                      product.active === false
                        ? "border-danger text-danger"
                        : "border-border-soft text-success"
                    }`}
                  >
                    {getStatusLabel(product)}
                  </span>
                </div>

                <p className="mt-1 text-xs text-text-muted">
                  ID #{product.productId}
                </p>
              </div>
            </div>

            <div className="mt-4 grid grid-cols-1 gap-3 text-sm sm:grid-cols-2">
              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Categoría
                </p>
                <p className="mt-1 font-medium text-text-main">
                  {getCategoryName(product)}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Unidad
                </p>
                <p className="mt-1 font-medium text-text-main">
                  {product.unit ?? "Sin unidad"}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2 sm:col-span-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Tamaño
                </p>
                <p className="mt-1 font-medium text-text-main">
                  {product.unitSize ?? "-"}
                </p>
              </div>
            </div>
          </article>
        ))}
      </div>

      <div className="hidden overflow-hidden rounded-card lg:block">
        <div className="overflow-x-auto">
          <table className="w-full min-w-190 border-separate border-spacing-0">
            <thead>
              <tr>
                <th className="rounded-tl-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Producto
                </th>
                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Categoría
                </th>
                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Unidad
                </th>
                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Tamaño
                </th>
                <th className="rounded-tr-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Estado
                </th>
              </tr>
            </thead>

            <tbody>
              {products.map((product) => (
                <ProductRow key={getProductKey(product)} product={product} />
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}