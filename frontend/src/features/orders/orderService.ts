import { apiClient } from "../../api/apiClient";
import type {
  CreateDrinksOrderRequest,
  CreateTimeOrderRequest,
  OrderResponse,
} from "./order.types";

async function createTimeOrder(
  data: CreateTimeOrderRequest
): Promise<OrderResponse> {
  const response = await apiClient.post<OrderResponse>("/orders", data);
  return response.data;
}

async function createDrinksOrder(
  data: CreateDrinksOrderRequest
): Promise<OrderResponse> {
  const response = await apiClient.post<OrderResponse>("/orders/by-drinks", data);
  return response.data;
}

export const orderService = {
  createTimeOrder,
  createDrinksOrder,
};