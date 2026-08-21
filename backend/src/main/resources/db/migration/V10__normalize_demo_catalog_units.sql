-- V10__normalize_demo_catalog_units.sql
-- Normaliza unidades del catálogo demo y unifica jugos cítricos para evitar resultados confusos en la lista de compra.

-- =========================
-- Normalización de unidades de productos
-- =========================

UPDATE products
SET unit = 'ML'
WHERE LOWER(TRIM(unit)) = 'ml';

UPDATE products
SET unit = 'GR'
WHERE LOWER(TRIM(unit)) IN ('g', 'gr');

UPDATE products
SET unit = 'UNID'
WHERE LOWER(TRIM(unit)) IN ('unid', 'unit', 'unidad', 'units');


-- =========================
-- Normalización de unidades de ingredientes
-- =========================

UPDATE cocktail_ingredients
SET unit = 'ML'
WHERE LOWER(TRIM(unit)) = 'ml';

UPDATE cocktail_ingredients
SET unit = 'GR'
WHERE LOWER(TRIM(unit)) IN ('g', 'gr');

UPDATE cocktail_ingredients
SET unit = 'UNID'
WHERE LOWER(TRIM(unit)) IN ('unid', 'unit', 'unidad', 'units');

UPDATE cocktail_ingredients
SET unit = 'OZ'
WHERE LOWER(TRIM(unit)) = 'oz';


-- =========================
-- Unificación de Jugo de lima y Jugo de limón
-- =========================
-- Para simplificar el catálogo demo, las recetas que usaban Jugo de lima
-- pasan a usar Jugo de limón. Esto evita que la lista de compra muestre
-- ambos productos como si fueran insumos separados.

DELETE FROM cocktail_ingredients ci
    USING products lime_product, products lemon_product
WHERE lime_product.name = 'Jugo de lima'
  AND lemon_product.name = 'Jugo de limón'
  AND ci.product_id = lime_product.id
  AND EXISTS (
    SELECT 1
    FROM cocktail_ingredients existing
    WHERE existing.cocktail_id = ci.cocktail_id
      AND existing.product_id = lemon_product.id
);

UPDATE cocktail_ingredients ci
SET product_id = lemon_product.id
FROM products lime_product, products lemon_product
WHERE lime_product.name = 'Jugo de lima'
  AND lemon_product.name = 'Jugo de limón'
  AND ci.product_id = lime_product.id;

UPDATE products
SET active = false
WHERE name = 'Jugo de lima';