import { Outlet } from "react-router-dom";

export function AuthLayout() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-background px-4 text-text-main">
      <Outlet />
    </main>
  );
}
