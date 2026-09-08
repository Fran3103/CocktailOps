-- V14__remove_ice_from_cocktail_recipes.sql
-- El hielo deja de formar parte de recetas individuales.
-- A partir de esta versión se calculará como insumo global de la orden.

DELETE FROM cocktail_ingredients ci
    USING products p
WHERE ci.product_id = p.id
  AND p.name = 'Hielo';