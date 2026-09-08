import { Package } from "lucide-react";

import type { OrderItemResponse } from "../order.types";

type OrderItemsTableProps = {
  items: OrderItemResponse[];
};

function getItemKey(item: OrderItemResponse, index: number) {
  return item.productId ?? `${item.productName}-${index}`;
}

export function OrderItemsTable({ items }: OrderItemsTableProps) {
  if (items.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Esta orden no tiene productos calculados.
      </p>
    );
  }

  return (
    <div className="rounded-card border border-border-soft bg-surface-soft">
      <div className="space-y-3 p-4 lg:hidden">
        {items.map((item, index) => (
          <article
            key={getItemKey(item, index)}
            className="rounded-card border border-border-soft bg-background/30 p-4"
          >
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary">
                <Package size={18} />
              </div>

              <div className="min-w-0 flex-1">
                <h3 className="font-semibold text-text-main">
                  {item.productName}
                </h3>

                <p className="mt-1 text-xs text-text-muted">
                  ID #{item.productId}
                </p>
              </div>
            </div>

            <div className="mt-4 grid grid-cols-1 gap-3 text-sm sm:grid-cols-2">
              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Packs a comprar
                </p>
                <p className="mt-1 text-lg font-semibold text-primary">
                  {item.packsToBuy}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Tamaño pack
                </p>
                <p className="mt-1 font-medium text-text-main">
                  {item.packSize}
                </p>
              </div>

              <div className="rounded-control border border-border-soft bg-background/40 px-3 py-2 sm:col-span-2">
                <p className="text-xs uppercase tracking-wide text-text-muted">
                  Unidad
                </p>
                <p className="mt-1 font-medium text-text-main">
                  {item.measureUnit}
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
                  Packs a comprar
                </th>
                <th className="bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Tamaño pack
                </th>
                <th className="rounded-tr-card bg-surface px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                  Unidad
                </th>
              </tr>
            </thead>

            <tbody>
              {items.map((item, index) => (
                <tr
                  key={getItemKey(item, index)}
                  className="border-b border-border-soft last:border-0"
                >
                  <td className="px-4 py-4">
                    <div className="flex items-center gap-3">
                      <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-background text-primary">
                        <Package size={18} />
                      </div>

                      <div>
                        <p className="font-medium text-text-main">
                          {item.productName}
                        </p>

                        <p className="text-xs text-text-muted">
                          ID #{item.productId}
                        </p>
                      </div>
                    </div>
                  </td>

                  <td className="px-4 py-4 text-sm font-semibold text-primary">
                    {item.packsToBuy}
                  </td>

                  <td className="px-4 py-4 text-sm text-text-muted">
                    {item.packSize}
                  </td>

                  <td className="px-4 py-4 text-sm text-text-muted">
                    {item.measureUnit}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}