export type CocktailIngredient = {
  id?: number;
  productId?: number;
  productName?: string;
  amount?: number;
  unit?: string;
};

export type Cocktail = {
  id: number;
  name: string;
  description?: string | null;
  imageUrl?: string | null;
  imageAlt?: string | null;
  ingredients?: CocktailIngredient[];
};