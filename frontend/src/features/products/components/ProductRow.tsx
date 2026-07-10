import { Package } from "lucide-react";
import type { Product } from "../product.types";

type ProductRowProps = {
  product: Product;
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


export function ProductRow({ product }: ProductRowProps) {
    
  return (
    <tr className="border-b border-border-soft last:border-0">
      <td className="px-4 py-4">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-control bg-surface text-primary">
            <Package size={18} />
          </div>

          <div>
            <p className="font-medium text-text-main">{product.name}</p>
            <p className="text-xs text-text-muted">ID #{product.productId}</p>
          </div>
        </div>
      </td>

      <td className="px-4 py-4 text-sm text-text-muted">
        {getCategoryName(product)}
      </td>

      <td className="px-4 py-4 text-sm text-text-muted">
        {product.unit ?? "Sin unidad"}
      </td>

      <td className="px-4 py-4 text-sm text-text-muted">
        {product.unitSize ?? "-"}
      </td>

      <td className="px-4 py-4">
        <span
          className={`rounded-full border px-2 py-1 text-xs font-medium ${
            product.active === false
              ? "border-danger text-danger"
              : "border-border-soft text-success"
          }`}
        >
          {product.active === false ? "Inactivo" : "Activo"}
        </span>
      </td>
    </tr>
  );
}