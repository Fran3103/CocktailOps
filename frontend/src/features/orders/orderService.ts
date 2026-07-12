import { apiClient } from "../../api/apiClient";
import type { CreateTimeOrderRequest, OrderResponse } from "./order.types";

async function createTimeOrder(
  data: CreateTimeOrderRequest
): Promise<OrderResponse> {
    console.log("Payload enviado:", data);
  const response = await apiClient.post<OrderResponse>("/orders", data);
  return response.data;
}

export const orderService = {
  createTimeOrder,
};