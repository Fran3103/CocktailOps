-- Seed demo data for CocktailOps
-- This migration loads a basic catalog to test the app and frontend flow.

-- =========================
-- Shop
-- =========================

INSERT INTO shops (name, slug)
VALUES ('CocktailOps Demo Bar', 'cocktailops-demo-bar');

-- =========================
-- Categories
-- =========================

INSERT INTO categories (shop_id, name, slug, active)
VALUES
    ((SELECT id FROM shops WHERE slug = 'cocktailops-demo-bar'), 'Alcoholes', 'alcoholes', true),
    ((SELECT id FROM shops WHERE slug = 'cocktailops-demo-bar'), 'Jugos y mixers', 'jugos-y-mixers', true),
    ((SELECT id FROM shops WHERE slug = 'cocktailops-demo-bar'), 'Frutas y hierbas', 'frutas-y-hierbas', true),
    ((SELECT id FROM shops WHERE slug = 'cocktailops-demo-bar'), 'Insumos', 'insumos', true);

-- =========================
-- Products
-- =========================

-- Alcoholes
INSERT INTO products (category_id, name, unit, unit_size, active, image_url, image_alt)
VALUES
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Ron blanco', 'ML', 750.00, true, null, 'Botella de ron blanco'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Vodka', 'ML', 750.00, true, null, 'Botella de vodka'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Gin', 'ML', 750.00, true, null, 'Botella de gin'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Tequila', 'ML', 750.00, true, null, 'Botella de tequila'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Triple sec', 'ML', 750.00, true, null, 'Botella de triple sec'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Fernet', 'ML', 750.00, true, null, 'Botella de fernet'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Aperol', 'ML', 750.00, true, null, 'Botella de aperol'),
    ((SELECT id FROM categories WHERE slug = 'alcoholes'), 'Espumante', 'ML', 750.00, true, null, 'Botella de espumante');

-- Jugos y mixers
INSERT INTO products (category_id, name, unit, unit_size, active, image_url, image_alt)
VALUES
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Jugo de lima', 'ML', 1000.00, true, null, 'Jugo de lima'),
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Jugo de limón', 'ML', 1000.00, true, null, 'Jugo de limón'),
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Almíbar simple', 'ML', 1000.00, true, null, 'Almíbar simple'),
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Agua tónica', 'ML', 1500.00, true, null, 'Botella de agua tónica'),
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Coca Cola', 'ML', 2250.00, true, null, 'Botella de Coca Cola'),
    ((SELECT id FROM categories WHERE slug = 'jugos-y-mixers'), 'Soda', 'ML', 1500.00, true, null, 'Botella de soda');

-- Frutas y hierbas
INSERT INTO products (category_id, name, unit, unit_size, active, image_url, image_alt)
VALUES
    ((SELECT id FROM categories WHERE slug = 'frutas-y-hierbas'), 'Menta', 'UNIT', 1.00, true, null, 'Atado de menta'),
    ((SELECT id FROM categories WHERE slug = 'frutas-y-hierbas'), 'Lima', 'UNIT', 1.00, true, null, 'Unidad de lima'),
    ((SELECT id FROM categories WHERE slug = 'frutas-y-hierbas'), 'Limón', 'UNIT', 1.00, true, null, 'Unidad de limón'),
    ((SELECT id FROM categories WHERE slug = 'frutas-y-hierbas'), 'Naranja', 'UNIT', 1.00, true, null, 'Unidad de naranja');

-- Insumos
INSERT INTO products (category_id, name, unit, unit_size, active, image_url, image_alt)
VALUES
    ((SELECT id FROM categories WHERE slug = 'insumos'), 'Azúcar', 'G', 1000.00, true, null, 'Paquete de azúcar'),
    ((SELECT id FROM categories WHERE slug = 'insumos'), 'Sal', 'G', 500.00, true, null, 'Paquete de sal');

-- =========================
-- Cocktails
-- =========================

INSERT INTO cocktails (name, description, image_url, image_alt)
VALUES
    ('Mojito', 'Cóctel clásico con ron blanco, lima, menta, azúcar y soda.', null, 'Cóctel Mojito'),
    ('Daiquiri', 'Cóctel clásico a base de ron blanco, lima y almíbar simple.', null, 'Cóctel Daiquiri'),
    ('Gin Tonic', 'Cóctel simple y refrescante con gin y agua tónica.', null, 'Cóctel Gin Tonic'),
    ('Margarita', 'Cóctel clásico con tequila, triple sec y jugo de lima.', null, 'Cóctel Margarita'),
    ('Fernet Cola', 'Trago popular con fernet y Coca Cola.', null, 'Fernet con Coca Cola'),
    ('Aperol Spritz', 'Cóctel fresco con Aperol, espumante y soda.', null, 'Cóctel Aperol Spritz'),
    ('Cuba Libre', 'Trago con ron blanco, Coca Cola y lima.', null, 'Cóctel Cuba Libre');

-- =========================
-- Cocktail Ingredients
-- Amounts are expressed in OZ
-- =========================

-- Mojito
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Mojito'), (SELECT id FROM products WHERE name = 'Ron blanco'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Mojito'), (SELECT id FROM products WHERE name = 'Jugo de lima'), 1.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Mojito'), (SELECT id FROM products WHERE name = 'Almíbar simple'), 0.75, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Mojito'), (SELECT id FROM products WHERE name = 'Soda'), 2.00, 'OZ');

-- Daiquiri
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Daiquiri'), (SELECT id FROM products WHERE name = 'Ron blanco'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Daiquiri'), (SELECT id FROM products WHERE name = 'Jugo de lima'), 1.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Daiquiri'), (SELECT id FROM products WHERE name = 'Almíbar simple'), 0.75, 'OZ');

-- Gin Tonic
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Gin Tonic'), (SELECT id FROM products WHERE name = 'Gin'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Gin Tonic'), (SELECT id FROM products WHERE name = 'Agua tónica'), 4.00, 'OZ');

-- Margarita
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Margarita'), (SELECT id FROM products WHERE name = 'Tequila'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Margarita'), (SELECT id FROM products WHERE name = 'Triple sec'), 1.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Margarita'), (SELECT id FROM products WHERE name = 'Jugo de lima'), 1.00, 'OZ');

-- Fernet Cola
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Fernet Cola'), (SELECT id FROM products WHERE name = 'Fernet'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Fernet Cola'), (SELECT id FROM products WHERE name = 'Coca Cola'), 5.00, 'OZ');

-- Aperol Spritz
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Aperol Spritz'), (SELECT id FROM products WHERE name = 'Aperol'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Aperol Spritz'), (SELECT id FROM products WHERE name = 'Espumante'), 3.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Aperol Spritz'), (SELECT id FROM products WHERE name = 'Soda'), 1.00, 'OZ');

-- Cuba Libre
INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES
    ((SELECT id FROM cocktails WHERE name = 'Cuba Libre'), (SELECT id FROM products WHERE name = 'Ron blanco'), 2.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Cuba Libre'), (SELECT id FROM products WHERE name = 'Coca Cola'), 5.00, 'OZ'),
    ((SELECT id FROM cocktails WHERE name = 'Cuba Libre'), (SELECT id FROM products WHERE name = 'Jugo de lima'), 0.50, 'OZ');