import { useMemo, useState } from "react";
import type { ReactNode } from "react";

import { AuthContext } from "./AuthContext";
import { authService } from "./authService";
import { clearAuthData, getStoredUser, saveAuthData } from "./authStorage";
import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  StoredUser,
} from "./auth.types";

type AuthProviderProps = {
  children: ReactNode;
};

function mapAuthResponseToUser(authData: AuthResponse): StoredUser {
  return {
    id: authData.id,
    email: authData.email,
    firstName: authData.firstName,
    lastName: authData.lastName,
    role: authData.role,
  };
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<StoredUser | null>(() => getStoredUser());

  async function login(data: LoginRequest) {
    const authData = await authService.login(data);

    saveAuthData(authData);
    setUser(mapAuthResponseToUser(authData));
  }

  async function register(data: RegisterRequest) {
    const authData = await authService.register(data);

    saveAuthData(authData);
    setUser(mapAuthResponseToUser(authData));
  }

  function logout() {
    clearAuthData();
    setUser(null);
  }

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(user),
      login,
      register,
      logout,
    }),
    [user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}