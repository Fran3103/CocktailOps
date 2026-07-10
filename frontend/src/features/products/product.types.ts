export type ProductCategory = {
  id?: number;
  name?: string;
};
export type Product = {
  productId: number;
  name: string;
  unit?: string | null;
  unitSize?: number | null;
  active?: boolean;
  imageUrl?: string | null;
  imageAlt?: string | null;
  category?: number | null;
  categoryId?: number | null;
  categoryName?: string | null;
};