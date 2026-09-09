-- ============================================================
-- V16 - Productos preparados y subrecetas
--
-- Permite distinguir:
-- - productos que se compran directamente
-- - productos que se preparan a partir de otros productos
--
-- Primer caso:
-- Almíbar simple 1:1
-- 1000 g de azúcar producen aproximadamente 1600 ml
-- de almíbar terminado.
-- ============================================================


-- ============================================================
-- PRODUCTOS COMPRABLES
-- ============================================================

ALTER TABLE products
    ADD COLUMN purchasable BOOLEAN NOT NULL DEFAULT TRUE;


-- El almíbar simple se prepara internamente.
UPDATE products p
SET purchasable = FALSE
FROM categories c
WHERE p.category_id = c.id
  AND p.name = 'Almíbar simple'
  AND c.slug = 'jugos-y-mixers';


-- ============================================================
-- RECETA DE PRODUCTOS PREPARADOS
-- ============================================================

CREATE TABLE prepared_product_recipes (
                                          id BIGSERIAL PRIMARY KEY,

                                          product_id BIGINT NOT NULL,

    -- Cantidad final que produce esta receta.
                                          output_amount NUMERIC(10,2) NOT NULL,
                                          output_unit VARCHAR(10) NOT NULL,

                                          CONSTRAINT fk_prepared_recipe_product
                                              FOREIGN KEY (product_id)
                                                  REFERENCES products(id)
                                                  ON DELETE CASCADE,

                                          CONSTRAINT uq_prepared_recipe_product
                                              UNIQUE (product_id),

                                          CONSTRAINT chk_prepared_recipe_output_amount
                                              CHECK (output_amount > 0)
);


-- ============================================================
-- INGREDIENTES DE LA RECETA PREPARADA
-- ============================================================

CREATE TABLE prepared_product_recipe_ingredients (
                                                     id BIGSERIAL PRIMARY KEY,

                                                     recipe_id BIGINT NOT NULL,
                                                     ingredient_product_id BIGINT NOT NULL,

                                                     amount NUMERIC(10,2) NOT NULL,
                                                     unit VARCHAR(10) NOT NULL,

                                                     CONSTRAINT fk_prepared_recipe_ingredient_recipe
                                                         FOREIGN KEY (recipe_id)
                                                             REFERENCES prepared_product_recipes(id)
                                                             ON DELETE CASCADE,

                                                     CONSTRAINT fk_prepared_recipe_ingredient_product
                                                         FOREIGN KEY (ingredient_product_id)
                                                             REFERENCES products(id)
                                                             ON DELETE RESTRICT,

                                                     CONSTRAINT uq_prepared_recipe_ingredient
                                                         UNIQUE (recipe_id, ingredient_product_id),

                                                     CONSTRAINT chk_prepared_recipe_ingredient_amount
                                                         CHECK (amount > 0)
);


CREATE INDEX idx_prepared_product_recipes_product_id
    ON prepared_product_recipes(product_id);

CREATE INDEX idx_prepared_recipe_ingredients_recipe_id
    ON prepared_product_recipe_ingredients(recipe_id);

CREATE INDEX idx_prepared_recipe_ingredients_product_id
    ON prepared_product_recipe_ingredients(ingredient_product_id);


-- ============================================================
-- RECETA: ALMÍBAR SIMPLE
-- ============================================================
-- Almíbar 1:1 por peso:
--
-- 1000 g azúcar
-- + agua
-- ≈ 1600 ml de almíbar terminado
--
-- El agua no se registra como producto porque no forma parte
-- de la lista de compras de CocktailOps.
-- ============================================================

INSERT INTO prepared_product_recipes (
    product_id,
    output_amount,
    output_unit
)
SELECT
    p.id,
    1600.00,
    'ML'
FROM products p
         JOIN categories c ON c.id = p.category_id
WHERE p.name = 'Almíbar simple'
  AND c.slug = 'jugos-y-mixers';


INSERT INTO prepared_product_recipe_ingredients (
    recipe_id,
    ingredient_product_id,
    amount,
    unit
)
SELECT
    recipe.id,
    sugar.id,
    1000.00,
    'GR'
FROM prepared_product_recipes recipe
         JOIN products syrup
              ON syrup.id = recipe.product_id
         JOIN categories syrup_category
              ON syrup_category.id = syrup.category_id
         CROSS JOIN products sugar
         JOIN categories sugar_category
              ON sugar_category.id = sugar.category_id
WHERE syrup.name = 'Almíbar simple'
  AND syrup_category.slug = 'jugos-y-mixers'
  AND sugar.name = 'Azúcar'
  AND sugar_category.slug = 'insumos';