import { createContext } from "react";
import type { LoginRequest, RegisterRequest, StoredUser } from "./auth.types";

export type AuthContextValue = {
  user: StoredUser | null;
  isAuthenticated: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
};

export const AuthContext = createContext<AuthContextValue | undefined>(
  undefined
);