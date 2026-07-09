import { apiClient } from "../../api/apiClient";
import type { AuthResponse, LoginRequest, RegisterRequest } from "./auth.types";

async function login(data: LoginRequest): Promise<AuthResponse> {
  const response = await apiClient.post<AuthResponse>("/auth/login", data);
  return response.data;
}

async function register(data: RegisterRequest): Promise<AuthResponse> {
  const response = await apiClient.post<AuthResponse>("/auth/register", data);
  return response.data;
}

export const authService = {
  login,
  register,
};