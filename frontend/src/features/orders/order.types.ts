export type OrderMode = "TIME" | "DRINKS";

export type CreateTimeOrderCocktail = {
  cocktailId: number;
  weight: number;
};

export type CreateTimeOrderRequest = {
  guests: number;
  durationHours: number;
  cocktails: CreateTimeOrderCocktail[];
};

export type CreateDrinksOrderCocktail = {
  cocktailId: number;
  quantity: number;
};

export type CreateDrinksOrderRequest = {
  totalDrinks: number;
  cocktails: CreateDrinksOrderCocktail[];
};

export type SelectedOrderCocktail = {
  cocktailId: number;
  cocktailName: string;
  weight: number;
  quantity: number;
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
  id: number | null;
  mode: OrderMode;
  createdAt: string | null;
  guests: number | null;
  drinksPerPerson: number | null;
  durationHours: number | null;
  status: string;
  items: OrderItemResponse[];
  cocktail: OrderCocktailResponse[];
  userId: number | null;
};

export type PdfSource =
  | {
      type: "SAVED_ORDER";
      orderId: number;
    }
  | {
      type: "TIME_PREVIEW";
      payload: CreateTimeOrderRequest;
    }
  | {
      type: "DRINKS_PREVIEW";
      payload: CreateDrinksOrderRequest;
    };