import { Package } from "lucide-react";

import type { OrderItemResponse } from "../order.types";

type OrderItemsTableProps = {
  items: OrderItemResponse[];
};

export function OrderItemsTable({ items }: OrderItemsTableProps) {
  if (items.length === 0) {
    return (
      <p className="text-sm text-text-muted">
        Esta orden no tiene productos calculados.
      </p>
    );
  }

  return (
    <div className="overflow-hidden rounded-card border border-border-soft bg-surface-soft">
      <div className="overflow-x-auto">
        <table className="w-full min-w-720px border-collapse">
          <thead className="bg-surface">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Producto
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Packs a comprar
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Tamaño pack
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-text-muted">
                Unidad
              </th>
            </tr>
          </thead>

          <tbody>
            {items.map((item) => (
              <tr
                key={item.productId}
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
  );
}