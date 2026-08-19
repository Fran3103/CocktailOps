import { useAuth } from "../auth/useAuth";
import { AdminDashboard } from "./components/AdminDashboard";
import { GuestDashboard } from "./components/GuestDashboard";
import { UserDashboard } from "./components/UserDashboard";

export function DashboardPage() {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated || !user) {
    return <GuestDashboard />;
  }

  if (user.role === "ADMIN") {
    return <AdminDashboard />;
  }

  return <UserDashboard />;
}