import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";

import { AppLayout } from "../layouts/AppLayout";
import { AuthLayout } from "../layouts/AuthLayout";

import { LoginPage } from "../features/auth/LoginPage";
import { RegisterPage } from "../features/auth/RegisterPage";
import { DashboardPage } from "../features/dashboard/DashboardPage";
import { CocktailsPage } from "../features/cocktails/CocktailsPage";
import { ProductsPage } from "../features/products/ProductsPage";
import { CreateOrderPage } from "../features/orders/CreateOrderPage";
import { OrderHistoryPage } from "../features/orders/OrderHistoryPage";
import { OrderDetailPage } from "../features/orders/OrderDetailPage";
import { ProfilePage } from "../features/profiles/ProfilePage";

import { ROUTES } from "../shared/constants/routes";
import { UnauthorizedPage } from "../features/auth/UnauthorizedPage";
import { ProtectedRoute } from "../features/auth/ProtectedRoute";
import { AdminRoute } from "../features/auth/AdminRoute";

export function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AuthLayout />}>
          <Route path={ROUTES.login} element={<LoginPage />} />
          <Route path={ROUTES.register} element={<RegisterPage />} />
        </Route>

        <Route element={<AppLayout />}>
          <Route path={ROUTES.dashboard} element={<DashboardPage />} />
          <Route path={ROUTES.cocktails} element={<CocktailsPage />} />
          <Route path={ROUTES.products} element={<ProductsPage />} />
          <Route path={ROUTES.createOrder} element={<CreateOrderPage />} />
          <Route path={ROUTES.unauthorized} element={<UnauthorizedPage/>} />

          <Route element={<ProtectedRoute />}>
            <Route path={ROUTES.orders} element={<OrderHistoryPage />} />
            <Route path={ROUTES.profile} element={<ProfilePage />} />
            <Route path={ROUTES.orderDetails} element={<OrderDetailPage />} />
          </Route>

          <Route element={<AdminRoute />}>
        
          </Route>
        </Route>

        <Route path="/" element={<Navigate to={ROUTES.dashboard} replace />} />
        <Route path="*" element={<Navigate to={ROUTES.dashboard} replace />} />
      </Routes>
    </BrowserRouter>
  );
}