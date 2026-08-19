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

async function getOrderById(orderId: number): Promise<OrderResponse> {
  const response = await apiClient.get<OrderResponse>(`/orders/${orderId}`);
  return response.data;
}

async function getMyOrders(): Promise<OrderResponse[]> {
  const response = await apiClient.get<OrderResponse[]>("/orders/my-orders");
  return response.data;
}

async function getAllOrders(): Promise<OrderResponse[]> {
  const response = await apiClient.get<OrderResponse[]>("/orders");
  return response.data;
}

async function downloadPdf(orderId: number | null): Promise<Blob> {
  const response = await apiClient.get<Blob>(`/orders/${orderId}/pdf`, {
    responseType: "blob",
  });

  return response.data;
}

async function downloadTimePreviewPdf(
  data: CreateTimeOrderRequest
): Promise<Blob> {
  const response = await apiClient.post<Blob>("/orders/preview/pdf", data, {
    responseType: "blob",
  });

  return response.data;
}


async function createDrinksPreview(
    data: CreateDrinksOrderRequest
): Promise<OrderResponse> {
    const response = await apiClient.post<OrderResponse>("/orders/by-drinks/preview", data);
    return response.data;
}

async function createTimePreview(
    data: CreateTimeOrderRequest
): Promise<OrderResponse> {
    const response = await apiClient.post<OrderResponse>("/orders/preview", data);
    return response.data;
}

async function downloadDrinksPreviewPdf(
  data: CreateDrinksOrderRequest
): Promise<Blob> {
  const response = await apiClient.post<Blob>(
    "/orders/by-drinks/preview/pdf",
    data,
    {
      responseType: "blob",
    }
  );

  return response.data;
}

export const orderService = {
  createTimeOrder,
  createDrinksOrder,
  getOrderById,
    getMyOrders,
    getAllOrders,
    downloadPdf,
    downloadTimePreviewPdf,
    downloadDrinksPreviewPdf,
    createDrinksPreview,
    createTimePreview,
};