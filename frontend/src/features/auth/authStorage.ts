import type { AuthResponse, StoredUser } from "./auth.types";

const TOKEN_KEY = "cocktailops_token";
const USER_KEY = "cocktailops_user";

export const AUTH_STORAGE_EVENT = "cocktailops-auth-storage-change";

type JwtPayload = {
  exp?: number;
};

function notifyAuthStorageChange() {
  window.dispatchEvent(new Event(AUTH_STORAGE_EVENT));
}

function decodeJwtPayload(token: string): JwtPayload | null {
  const [, payload] = token.split(".");

  if (!payload) {
    return null;
  }

  try {
    const normalizedPayload = payload.replace(/-/g, "+").replace(/_/g, "/");
    const padding = "=".repeat((4 - (normalizedPayload.length % 4)) % 4);
    const decodedPayload = atob(normalizedPayload + padding);

    return JSON.parse(decodedPayload) as JwtPayload;
  } catch {
    return null;
  }
}

function isTokenExpired(token: string) {
  const payload = decodeJwtPayload(token);

  if (!payload?.exp) {
    return false;
  }

  return Date.now() >= payload.exp * 1000;
}

export function saveAuthData(authData: AuthResponse) {
  const { token, ...user } = authData;

  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  notifyAuthStorageChange();
}

export function clearAuthData() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
  notifyAuthStorageChange();
}

export function getToken() {
  const token = localStorage.getItem(TOKEN_KEY);

  if (!token) {
    return null;
  }

  if (isTokenExpired(token)) {
    clearAuthData();
    return null;
  }

  return token;
}

export function getStoredUser(): StoredUser | null {
  const token = getToken();
  const user = localStorage.getItem(USER_KEY);

  if (!token || !user) {
    clearAuthData();
    return null;
  }

  try {
    return JSON.parse(user) as StoredUser;
  } catch {
    clearAuthData();
    return null;
  }
}