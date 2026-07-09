export type UserRole = "USER" | "ADMIN";

export type AuthResponse = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  token: string;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
};

export type StoredUser = Omit<AuthResponse, "token">;