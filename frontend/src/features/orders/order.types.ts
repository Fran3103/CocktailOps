export type CreateTimeOrderCocktail = {
  cocktailId: number;
  weight: number;
};

export type CreateTimeOrderRequest = {
  guests: number;
  durationHours: number;
  cocktails: CreateTimeOrderCocktail[];
};

export type SelectedOrderCocktail = {
  cocktailId: number;
  cocktailName: string;
  weight: number;
};