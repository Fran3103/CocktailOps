import { apiClient } from "../../api/apiClient";
import type { Product } from "./product.types";

async function getAll(): Promise<Product[]> {
  const response = await apiClient.get<Product[]>("/products");
  return response.data;
}

export const productService = {
  getAll,
};
