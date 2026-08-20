-- V9__fix_sugar_ingredient_units.sql
-- Corrige recetas que usaban azúcar como UNID.
-- El producto Azúcar se mide en gramos, por eso la receta debe usar GR.

UPDATE cocktail_ingredients ci
SET amount = fixed.amount,
    unit = 'GR'
FROM (
         VALUES
             ('Old Fashioned', 'Azúcar', 5.00),
             ('Caipirinha', 'Azúcar', 10.00),
             ('Caipiroska', 'Azúcar', 10.00),
             ('Caipirissima', 'Azúcar', 10.00)
     ) AS fixed(cocktail_name, product_name, amount)
         JOIN cocktails c ON c.name = fixed.cocktail_name
         JOIN products p ON p.name = fixed.product_name
WHERE ci.cocktail_id = c.id
  AND ci.product_id = p.id;