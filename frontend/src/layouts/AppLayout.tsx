import { useState } from "react";
import { Outlet } from "react-router-dom";
import { Menu, X } from "lucide-react";

import { Sidebar } from "../shared/components/navigation/Sidebar";

export function AppLayout() {
  const [isMobileSidebarOpen, setIsMobileSidebarOpen] = useState(false);

  function closeMobileSidebar() {
    setIsMobileSidebarOpen(false);
  }

  return (
    <div className="min-h-screen bg-background text-text-main lg:flex">
      <div className="hidden lg:block">
        <Sidebar />
      </div>

      <div className="flex min-h-screen flex-1 flex-col">
        <header className="sticky top-0 z-30 flex items-center justify-between border-b border-border-soft bg-background/95 px-4 py-4 backdrop-blur lg:hidden">
          <div>
            <p className="font-heading text-lg font-bold text-primary">
              CocktailOps
            </p>
            <p className="text-xs text-text-muted">Event planning dashboard</p>
          </div>

          <button
            type="button"
            onClick={() => setIsMobileSidebarOpen(true)}
            className="rounded-control border border-border-soft p-2 text-text-main"
            aria-label="Abrir menú"
          >
            <Menu size={20} />
          </button>
        </header>

        {isMobileSidebarOpen && (
          <div className="fixed inset-0 z-40 lg:hidden">
            <button
              type="button"
              className="absolute inset-0 bg-black/60"
              onClick={closeMobileSidebar}
              aria-label="Cerrar menú"
            />

            <div className="relative h-full w-72 max-w-[85vw] bg-surface">
              <button
                type="button"
                onClick={closeMobileSidebar}
                className="absolute right-4 top-4 rounded-control border border-border-soft p-2 text-text-main"
                aria-label="Cerrar menú"
              >
                <X size={18} />
              </button>

              <Sidebar onNavigate={closeMobileSidebar} />
            </div>
          </div>
        )}

        <main className="flex-1 px-4 py-6 sm:px-6 lg:p-6 xl:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}