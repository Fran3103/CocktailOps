import { useEffect, useMemo, useState } from "react";

import { Button } from "../../shared/components/ui/Button";
import { Card } from "../../shared/components/ui/Card";
import { ErrorState } from "../../shared/utils/ErrorState";
import { PageHeader } from "../../shared/components/ui/PageHeader";
import { getApiErrorMessage } from "../../shared/utils/getApiErrorMessage";
import { ProductFilters } from "./components/ProductFilters";
import { ProductTable } from "./components/ProductTable";
import { productService } from "./productService";
import type { Product } from "./product.types";

function getProductsErrorMessage(error: unknown) {
  return getApiErrorMessage(error, {
    defaultMessage: "No se pudieron cargar los productos.",
    networkMessage:
      "No se pudo conectar con el servidor para cargar los productos.",
    unauthorizedMessage:
      "Tu sesión no está activa o venció. Iniciá sesión nuevamente.",
    forbiddenMessage: "No tenés permisos para consultar los productos.",
    notFoundMessage: "No se encontró el catálogo de productos.",
    serverMessage:
      "Ocurrió un error en el servidor al cargar los productos. Intentá nuevamente más tarde.",
  });
}

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
      } catch (fetchProductsError) {
        if (!ignore) {
          setError(getProductsErrorMessage(fetchProductsError));
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
    } catch (retryError) {
      setError(getProductsErrorMessage(retryError));
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

      {!isLoading && error && (
        <ErrorState
          title="No pudimos cargar los productos"
          description={error}
        >
          <Button type="button" onClick={handleRetry}>
            Reintentar
          </Button>
        </ErrorState>
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