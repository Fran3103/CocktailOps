import { Navigate, Outlet, useLocation } from "react-router-dom";

import { ROUTES } from "../../shared/constants/routes";
import { useAuth } from "./useAuth";

export function ProtectedRoute() {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return (
      <Navigate
        to={ROUTES.login}
        replace
        state={{ from: location }}
      />
    );
  }

  return <Outlet />;
}