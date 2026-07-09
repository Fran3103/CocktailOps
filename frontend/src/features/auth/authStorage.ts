import type { AuthResponse, StoredUser } from "./auth.types";

const TOKEN_KEY = "cocktailops_token";
const USER_KEY = "cocktailops_user";

export function saveAuthData(authData: AuthResponse) {
  const { token, ...user } = authData;

  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function getStoredUser(): StoredUser | null {
  const user = localStorage.getItem(USER_KEY);

  if (!user) {
    return null;
  }

  return JSON.parse(user) as StoredUser;
}

export function clearAuthData() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}