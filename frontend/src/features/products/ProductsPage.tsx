import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { productService } from "./productService";
import type { Product } from "./product.types";
import { ProductFilters } from "./components/ProductFilters";
import { ProductTable } from "./components/ProductTable";


export function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let ignore = false;

    async function fetchProducts() {
      try {
        const data = await productService.getAll();

        if (!ignore) {
          setProducts(data);
          setError(null);
        }
      } catch {
        if (!ignore) {
          setError("No se pudieron cargar los productos.");
        }
      } finally {
        if (!ignore) {
          setIsLoading(false);
        }
      }
    }

    void fetchProducts();

    return () => {
      ignore = true;
    };
  }, []);

  async function handleRetry() {
    setIsLoading(true);
    setError(null);

    try {
      const data = await productService.getAll();
      setProducts(data);
    } catch {
      setError("No se pudieron cargar los productos.");
    } finally {
      setIsLoading(false);
    }
  }

  const filteredProducts = useMemo(() => {
    const normalizedSearch = searchTerm.toLowerCase().trim();

    if (!normalizedSearch) {
      return products;
    }

    return products.filter((product) => {
      const name = product.name.toLowerCase();
      const category = product.categoryName?.toLowerCase() ?? "";
      const unit = product.unit?.toLowerCase() ?? "";
      const size = product.unitSize?.toString() ?? "";

      return (
        name.includes(normalizedSearch) ||
        category.includes(normalizedSearch) ||
        unit.includes(normalizedSearch) ||
        size.includes(normalizedSearch)
      );
    });
  }, [products, searchTerm]);

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <PageHeader
          title="Productos"
          description="Consultá los productos e insumos disponibles para el cálculo de órdenes."
        />
      </div>

      <ProductFilters
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
      />

      {isLoading && (
        <Card>
          <p className="text-text-muted">Cargando productos...</p>
        </Card>
      )}

      {error && (
        <Card>
          <p className="text-danger">{error}</p>

          <Button type="button" className="mt-4" onClick={handleRetry}>
            Reintentar
          </Button>
        </Card>
      )}

      {!isLoading && !error && filteredProducts.length === 0 && (
        <Card>
          <p className="text-text-muted">
            No se encontraron productos para mostrar.
          </p>
        </Card>
      )}

      {!isLoading && !error && filteredProducts.length > 0 && (
        <>
          <ProductTable products={filteredProducts} />

          <p className="text-sm text-text-muted">
            Mostrando {filteredProducts.length} de {products.length} productos.
          </p>
        </>
      )}
    </section>
  );
}