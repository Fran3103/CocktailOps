import { Navigate, Outlet } from "react-router-dom";

import { ROUTES } from "../../shared/constants/routes";
import { useAuth } from "./useAuth";

export function AdminRoute() {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.login} replace />;
  }

  if (user?.role !== "ADMIN") {
    return <Navigate to={ROUTES.unauthorized} replace />;
  }

  return <Outlet />;
}