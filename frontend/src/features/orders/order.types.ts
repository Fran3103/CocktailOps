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

export type OrderCocktailResponse = {
  cocktailId: number;
  cocktailName: string;
  quantity: number;
};

export type OrderItemResponse = {
  productId: number;
  productName: string;
  packsToBuy: number;
  packSize: number | string;
  measureUnit: string;
};

export type OrderResponse = {
  id: number;
  mode: "TIME" | "DRINKS";
  createdAt: string;
  guests: number | null;
  drinksPerPerson: number | null;
  durationHours: number | null;
  status: string;
  items: OrderItemResponse[];
  cocktail: OrderCocktailResponse[];
  userId: number | null;
};