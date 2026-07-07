import { ClipboardList, Martini, Package, User, History } from "lucide-react";
import { ROUTES } from "../../constants/routes";
import { NavLink } from "react-router-dom";

const navItems = [
  {
    label: "Dashboard",
    path: ROUTES.dashboard,
    icon: Martini,
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
  return (
    <aside className="w-64 bg-surface-main border-r border-border-soft p-4">
      <div className="mb-8">
        <h1 className="font-heading text-xl font-bold text-primary">
          CocktailOps
        </h1>

        <p className="mt-1 text-sm text-text-muted">Event planning dashboard</p>
      </div>

      <nav className="space-y-2">
        {navItems.map((item) => {
          const Icon = item.icon;

          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-control py-2 px-3  text-sm transition  ${
                  isActive
                    ? "bg-primary font-semibold text-background"
                    : "text-text-muted hover:bg-surface-bright hover:text-text-main"
                }`
              }
            >
              <Icon size={18} />
              <span> {item.label}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
}
