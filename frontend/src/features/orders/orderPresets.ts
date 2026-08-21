export type OrderPresetCocktail = {
  cocktailName: string;
  weight: number;
};

export type OrderPreset = {
  id: string;
  title: string;
  description: string;
  recommendedFor: string;
  cocktails: OrderPresetCocktail[];
};

export const orderPresets: OrderPreset[] = [
  {
    id: "classic-short",
    title: "Clásicos simples",
    description:
      "Una selección corta de tragos conocidos, ideal para eventos chicos o barras simples.",
    recommendedFor: "Cumpleaños, reuniones chicas y eventos informales",
    cocktails: [
      { cocktailName: "Fernet Cola", weight: 3 },
      { cocktailName: "Gin Tonic", weight: 3 },
      { cocktailName: "Mojito", weight: 2 },
      { cocktailName: "Daiquiri", weight: 1 },
      { cocktailName: "Margarita", weight: 1 },
    ],
  },
  {
    id: "classic-full",
    title: "Clásicos completos",
    description:
      "Lista amplia de cócteles clásicos para una barra más variada y completa.",
    recommendedFor: "Eventos grandes, cumpleaños y barras generales",
    cocktails: [
      { cocktailName: "Fernet Cola", weight: 4 },
      { cocktailName: "Gin Tonic", weight: 4 },
      { cocktailName: "Mojito", weight: 3 },
      { cocktailName: "Daiquiri", weight: 2 },
      { cocktailName: "Margarita", weight: 2 },
      { cocktailName: "Negroni", weight: 2 },
      { cocktailName: "Old Fashioned", weight: 1 },
      { cocktailName: "Whisky Sour", weight: 2 },
      { cocktailName: "Tom Collins", weight: 2 },
      { cocktailName: "Dry Martini", weight: 1 },
    ],
  },
  {
    id: "wedding",
    title: "Boda / evento elegante",
    description:
      "Selección fresca, elegante y variada para eventos largos con muchos invitados.",
    recommendedFor: "Bodas, fiestas formales y celebraciones grandes",
    cocktails: [
      { cocktailName: "Aperol Spritz", weight: 4 },
      { cocktailName: "Gin Tonic", weight: 4 },
      { cocktailName: "Mojito", weight: 3 },
      { cocktailName: "French 75", weight: 2 },
      { cocktailName: "Bellini", weight: 3 },
      { cocktailName: "Cosmopolitan", weight: 2 },
      { cocktailName: "Margarita", weight: 2 },
      { cocktailName: "Daiquiri", weight: 2 },
      { cocktailName: "Tom Collins", weight: 2 },
      { cocktailName: "Paloma", weight: 2 },
    ],
  },
  {
    id: "modern-party",
    title: "Modernos y fiesta",
    description:
      "Tragos frescos, actuales y fáciles de vender en eventos relajados.",
    recommendedFor: "After office, fiestas jóvenes y eventos informales",
    cocktails: [
      { cocktailName: "Aperol Spritz", weight: 4 },
      { cocktailName: "Moscow Mule", weight: 3 },
      { cocktailName: "Cosmopolitan", weight: 2 },
      { cocktailName: "Espresso Martini", weight: 2 },
      { cocktailName: "Sex on the Beach", weight: 3 },
      { cocktailName: "Tequila Sunrise", weight: 3 },
      { cocktailName: "Paloma", weight: 3 },
      { cocktailName: "Vodka Tonic", weight: 3 },
      { cocktailName: "Gin Tonic", weight: 3 },
    ],
  },
  {
    id: "summer",
    title: "Verano / tropical",
    description:
      "Cócteles frescos, frutales y fáciles de tomar para eventos de verano.",
    recommendedFor: "Eventos al aire libre, verano, fiestas de día y terrazas",
    cocktails: [
      { cocktailName: "Mojito", weight: 4 },
      { cocktailName: "Daiquiri", weight: 3 },
      { cocktailName: "Caipirinha", weight: 3 },
      { cocktailName: "Caipiroska", weight: 3 },
      { cocktailName: "Caipirissima", weight: 2 },
      { cocktailName: "Piña Colada", weight: 3 },
      { cocktailName: "Paloma", weight: 3 },
      { cocktailName: "Tequila Sunrise", weight: 2 },
      { cocktailName: "Sex on the Beach", weight: 2 },
    ],
  },
  {
    id: "aperitif",
    title: "Aperitivos",
    description:
      "Lista pensada para una barra de tragos livianos, amargos y refrescantes.",
    recommendedFor: "Recepciones, eventos de tarde, tapeos y barras de aperitivo",
    cocktails: [
      { cocktailName: "Aperol Spritz", weight: 4 },
      { cocktailName: "Negroni", weight: 3 },
      { cocktailName: "Americano", weight: 3 },
      { cocktailName: "Garibaldi", weight: 3 },
      { cocktailName: "Campari Tonic", weight: 3 },
      { cocktailName: "Gin Tonic", weight: 3 },
      { cocktailName: "Vodka Tonic", weight: 2 },
      { cocktailName: "Tom Collins", weight: 2 },
    ],
  },
  {
    id: "premium-classic",
    title: "Premium clásico",
    description:
      "Selección más fuerte y clásica, con tragos de coctelería tradicional.",
    recommendedFor: "Eventos premium, barras especiales y degustaciones",
    cocktails: [
      { cocktailName: "Old Fashioned", weight: 3 },
      { cocktailName: "Negroni", weight: 3 },
      { cocktailName: "Dry Martini", weight: 2 },
      { cocktailName: "Whisky Sour", weight: 3 },
      { cocktailName: "French 75", weight: 2 },
      { cocktailName: "Espresso Martini", weight: 2 },
      { cocktailName: "Margarita", weight: 2 },
      { cocktailName: "Cosmopolitan", weight: 2 },
    ],
  },
  {
    id: "simple-popular",
    title: "Popular y rápido",
    description:
      "Pocos tragos, alta rotación y preparación simple para barras con poco tiempo.",
    recommendedFor: "Eventos chicos, barras rápidas y presupuestos ajustados",
    cocktails: [
      { cocktailName: "Fernet Cola", weight: 4 },
      { cocktailName: "Gin Tonic", weight: 3 },
      { cocktailName: "Vodka Tonic", weight: 2 },
      { cocktailName: "Mojito", weight: 2 },
      { cocktailName: "Aperol Spritz", weight: 2 },
      { cocktailName: "Daiquiri", weight: 1 },
    ],
  },
];