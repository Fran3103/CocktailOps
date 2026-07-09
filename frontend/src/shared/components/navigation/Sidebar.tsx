import { NavLink, useNavigate } from "react-router-dom";
import {
  ClipboardList,
  History,
  LayoutDashboard,
  LogOut,
  Martini,
  Package,
  User,
} from "lucide-react";

import { useAuth } from "../../../features/auth/useAuth";
import { ROUTES } from "../../constants/routes";
import { Button } from "../ui/Button";

const navItems = [
  {
    label: "Dashboard",
    path: ROUTES.dashboard,
    icon: LayoutDashboard,
  },
  {
    label: "Cócteles",
    path: ROUTES.cocktails,
    icon: Martini,
  },
  {
    label: "Productos",
    path: ROUTES.products,
    icon: Package,
  },
  {
    label: "Nueva orden",
    path: ROUTES.createOrder,
    icon: ClipboardList,
  },
  {
    label: "Historial",
    path: ROUTES.orders,
    icon: History,
  },
  {
    label: "Perfil",
    path: ROUTES.profile,
    icon: User,
  },
];

export function Sidebar() {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuth();

  function handleLogout() {
    logout();
    navigate(ROUTES.login);
  }

  return (
    <aside className="flex min-h-screen w-64 shrink-0 flex-col border-r border-border-soft px-4 py-6">
      <div className="mb-8">
        <h1 className="font-heading text-xl font-bold text-primary">
          CocktailOps
        </h1>

        <p className="mt-1 text-sm text-text-muted">
          Event planning dashboard
        </p>
      </div>

      <nav className="space-y-2">
        {navItems.map((item) => {
          const Icon = item.icon;

          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-control px-3 py-2 text-sm transition ${
                  isActive
                    ? "bg-primary font-semibold text-background"
                    : "text-text-muted hover:bg-surface-bright hover:text-text-main"
                }`
              }
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </nav>

      <div className="mt-auto border-t border-border-soft pt-4">
        {isAuthenticated && user ? (
          <div className="space-y-3">
            <div>
              <p className="text-sm font-semibold text-text-main">
                {user.firstName} {user.lastName}
              </p>

              <p className="text-xs uppercase tracking-wide text-text-muted">
                {user.role}
              </p>
            </div>

            <Button
              type="button"
              variant="ghost"
              fullWidth
              className="justify-start"
              onClick={handleLogout}
            >
              <span className="flex items-center gap-2">
                <LogOut size={16} />
                Cerrar sesión
              </span>
            </Button>
          </div>
        ) : (
          <div className="space-y-3">
            <p className="text-sm text-text-muted">Modo invitado</p>

            <Button
              type="button"
              variant="secondary"
              fullWidth
              onClick={() => navigate(ROUTES.login)}
            >
              Iniciar sesión
            </Button>
          </div>
        )}
      </div>
    </aside>
  );
}