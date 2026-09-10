const footerLinks = [
  {
    label: "LinkedIn",
    href: "https://www.linkedin.com/in/franconahuelaguirre/",
  },
  {
    label: "GitHub",
    href: "https://github.com/Fran3103",
  },
  {
    label: "Proyecto",
    href: "https://github.com/Fran3103/CocktailOps",
  },
  {
    label: "Portfolio",
    href: "https://franaguirredev.com/",
  },
  {
    label: "Email",
    href: "mailto:francoaguirre.ar@gmail.com",
  },
];

export function AppFooter() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="border-t border-border-soft px-4 py-6 text-text-muted sm:px-6 lg:px-8">
      <div className="flex flex-col gap-5 lg:flex-row lg:items-center lg:justify-between">
        <div className="max-w-2xl text-center lg:text-left">
          <p className="text-sm font-semibold text-text-main">
            Franco Aguirre · Desarrollador de software
          </p>

          <p className="mt-1 text-xs">
            Java / Spring Boot / React · CocktailOps es un proyecto portfolio no
            comercial, creado para demostrar desarrollo full stack aplicado al cálculo de 
            bebidas e insumos para eventos.
          </p>

          <p className="mt-1 text-xs">
            © {currentYear} CocktailOps
          </p>
        </div>

        <nav
          className="flex flex-wrap items-center justify-center gap-2"
          aria-label="Enlaces profesionales"
        >
          {footerLinks.map((link) => {
            const isEmailLink = link.href.startsWith("mailto:");

            return (
              <a
                key={link.href}
                href={link.href}
                target={isEmailLink ? undefined : "_blank"}
                rel={isEmailLink ? undefined : "noreferrer noopener"}
                className="rounded-full border border-border-soft bg-surface-soft px-3 py-1.5 text-xs font-medium text-text-muted transition hover:border-primary/60 hover:bg-primary/10 hover:text-primary"
              >
                {link.label}
              </a>
            );
          })}
        </nav>
      </div>
    </footer>
  );
}