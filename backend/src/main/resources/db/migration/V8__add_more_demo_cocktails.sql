-- V8__add_more_demo_cocktails.sql
-- Amplía el catálogo demo con productos y cócteles adicionales.
-- No modifica migraciones anteriores.

-- =========================
-- Productos adicionales
-- =========================

INSERT INTO products (
    category_id,
    name,
    unit,
    unit_size,
    active,
    created_at,
    image_url,
    image_alt

)
SELECT
    c.id,
    v.name,
    v.unit,
    v.unit_size,
    true,
    NOW(),
    v.image_alt,
    v.image_url
FROM (
         VALUES
             ('Alcoholes', 'Campari', 'ml', 750, 'Botella de Campari', NULL),
             ('Alcoholes', 'Vermouth rosso', 'ml', 1000, 'Botella de vermouth rosso', NULL),
             ('Alcoholes', 'Vermouth dry', 'ml', 1000, 'Botella de vermouth dry', NULL),
             ('Alcoholes', 'Whisky', 'ml', 750, 'Botella de whisky', NULL),
             ('Alcoholes', 'Licor de café', 'ml', 750, 'Botella de licor de café', NULL),
             ('Alcoholes', 'Cachaça', 'ml', 750, 'Botella de cachaça', NULL),
             ('Alcoholes', 'Licor de durazno', 'ml', 750, 'Botella de licor de durazno', NULL),

             ('Insumos', 'Angostura bitters', 'ml', 100, 'Botella de bitter aromático', NULL),
             ('Insumos', 'Aceitunas', 'unid', 50, 'Aceitunas para garnish', NULL),

             ('Jugos y mixers', 'Café espresso', 'ml', 1000, 'Café espresso preparado', NULL),
             ('Jugos y mixers', 'Ginger beer', 'ml', 1000, 'Ginger beer', NULL),
             ('Jugos y mixers', 'Jugo de pomelo', 'ml', 1000, 'Jugo de pomelo', NULL),
             ('Jugos y mixers', 'Jugo de cranberry', 'ml', 1000, 'Jugo de cranberry', NULL),
             ('Jugos y mixers', 'Jugo de ananá', 'ml', 1000, 'Jugo de ananá', NULL),
             ('Jugos y mixers', 'Jugo de naranja', 'ml', 1000, 'Jugo de naranja', NULL),
             ('Jugos y mixers', 'Crema de coco', 'ml', 1000, 'Crema de coco', NULL),
             ('Jugos y mixers', 'Granadina', 'ml', 750, 'Granadina', NULL),
             ('Jugos y mixers', 'Puré de durazno', 'ml', 1000, 'Puré de durazno', NULL)
     ) AS v(category_name, name, unit, unit_size, image_alt, image_url)
         JOIN categories c ON c.name = v.category_name
WHERE NOT EXISTS (
    SELECT 1
    FROM products p
    WHERE p.name = v.name
);


-- =========================
-- Cócteles adicionales
-- =========================

INSERT INTO cocktails (
    name,
    description,
    created_at,
    image_alt,
    image_url
)
SELECT
    v.name,
    v.description,
    NOW(),
    v.image_alt,
    v.image_url
FROM (
         VALUES
             ('Negroni', 'Clásico italiano con gin, vermouth rosso y Campari.', 'Cóctel Negroni', NULL),
             ('Old Fashioned', 'Clásico con whisky, azúcar y bitters.', 'Cóctel Old Fashioned', NULL),
             ('Whisky Sour', 'Cóctel clásico con whisky, limón y almíbar.', 'Cóctel Whisky Sour', NULL),
             ('Tom Collins', 'Trago largo con gin, limón, almíbar y soda.', 'Cóctel Tom Collins', NULL),
             ('Dry Martini', 'Clásico seco con gin y vermouth dry.', 'Cóctel Dry Martini', NULL),
             ('Cosmopolitan', 'Cóctel con vodka, triple sec, lima y cranberry.', 'Cóctel Cosmopolitan', NULL),
             ('Moscow Mule', 'Trago fresco con vodka, lima y ginger beer.', 'Cóctel Moscow Mule', NULL),
             ('Paloma', 'Cóctel refrescante con tequila, pomelo, lima y soda.', 'Cóctel Paloma', NULL),
             ('Caipirinha', 'Clásico brasileño con cachaça, lima y azúcar.', 'Cóctel Caipirinha', NULL),
             ('Caipiroska', 'Versión con vodka de la caipirinha.', 'Cóctel Caipiroska', NULL),
             ('Piña Colada', 'Cóctel tropical con ron, ananá y coco.', 'Cóctel Piña Colada', NULL),
             ('Sex on the Beach', 'Cóctel frutal con vodka, durazno, naranja y cranberry.', 'Cóctel Sex on the Beach', NULL),
             ('Espresso Martini', 'Cóctel moderno con vodka, licor de café y espresso.', 'Cóctel Espresso Martini', NULL),
             ('Tequila Sunrise', 'Trago con tequila, naranja y granadina.', 'Cóctel Tequila Sunrise', NULL),
             ('Americano', 'Clásico aperitivo con Campari, vermouth rosso y soda.', 'Cóctel Americano', NULL),
             ('Garibaldi', 'Aperitivo simple con Campari y jugo de naranja.', 'Cóctel Garibaldi', NULL),
             ('French 75', 'Cóctel con gin, limón, almíbar y espumante.', 'Cóctel French 75', NULL),
             ('Bellini', 'Cóctel suave con espumante y durazno.', 'Cóctel Bellini', NULL),
             ('Vodka Tonic', 'Trago largo simple con vodka y agua tónica.', 'Cóctel Vodka Tonic', NULL),
             ('Campari Tonic', 'Aperitivo con Campari y agua tónica.', 'Cóctel Campari Tonic', NULL),
             ('Gin Fizz', 'Trago fresco con gin, limón, almíbar y soda.', 'Cóctel Gin Fizz', NULL),
             ('Vodka Collins', 'Versión con vodka del clásico Collins.', 'Cóctel Vodka Collins', NULL),
             ('Caipirissima', 'Versión con ron de la caipirinha.', 'Cóctel Caipirissima', NULL)
     ) AS v(name, description, image_alt, image_url)
WHERE NOT EXISTS (
    SELECT 1
    FROM cocktails c
    WHERE c.name = v.name
);


-- =========================
-- Ingredientes de cócteles adicionales
-- =========================

INSERT INTO cocktail_ingredients (
    cocktail_id,
    product_id,
    amount,
    unit
)
SELECT
    c.id,
    p.id,
    v.amount,
    v.unit
FROM (
         VALUES
             -- Negroni
             ('Negroni', 'Gin', 1.00, 'OZ'),
             ('Negroni', 'Vermouth rosso', 1.00, 'OZ'),
             ('Negroni', 'Campari', 1.00, 'OZ'),

             -- Old Fashioned
             ('Old Fashioned', 'Whisky', 2.00, 'OZ'),
             ('Old Fashioned', 'Azúcar', 1.00, 'UNID'),
             ('Old Fashioned', 'Angostura bitters', 0.10, 'OZ'),

             -- Whisky Sour
             ('Whisky Sour', 'Whisky', 2.00, 'OZ'),
             ('Whisky Sour', 'Jugo de limón', 0.75, 'OZ'),
             ('Whisky Sour', 'Almíbar simple', 0.75, 'OZ'),

             -- Tom Collins
             ('Tom Collins', 'Gin', 1.50, 'OZ'),
             ('Tom Collins', 'Jugo de limón', 1.00, 'OZ'),
             ('Tom Collins', 'Almíbar simple', 0.75, 'OZ'),
             ('Tom Collins', 'Soda', 3.00, 'OZ'),

             -- Dry Martini
             ('Dry Martini', 'Gin', 2.50, 'OZ'),
             ('Dry Martini', 'Vermouth dry', 0.50, 'OZ'),
             ('Dry Martini', 'Aceitunas', 1.00, 'UNID'),

             -- Cosmopolitan
             ('Cosmopolitan', 'Vodka', 1.50, 'OZ'),
             ('Cosmopolitan', 'Triple sec', 0.75, 'OZ'),
             ('Cosmopolitan', 'Jugo de lima', 0.50, 'OZ'),
             ('Cosmopolitan', 'Jugo de cranberry', 1.00, 'OZ'),

             -- Moscow Mule
             ('Moscow Mule', 'Vodka', 1.50, 'OZ'),
             ('Moscow Mule', 'Jugo de lima', 0.50, 'OZ'),
             ('Moscow Mule', 'Ginger beer', 4.00, 'OZ'),

             -- Paloma
             ('Paloma', 'Tequila', 1.50, 'OZ'),
             ('Paloma', 'Jugo de pomelo', 3.00, 'OZ'),
             ('Paloma', 'Jugo de lima', 0.50, 'OZ'),
             ('Paloma', 'Soda', 1.00, 'OZ'),

             -- Caipirinha
             ('Caipirinha', 'Cachaça', 2.00, 'OZ'),
             ('Caipirinha', 'Lima', 1.00, 'UNID'),
             ('Caipirinha', 'Azúcar', 2.00, 'UNID'),

             -- Caipiroska
             ('Caipiroska', 'Vodka', 2.00, 'OZ'),
             ('Caipiroska', 'Lima', 1.00, 'UNID'),
             ('Caipiroska', 'Azúcar', 2.00, 'UNID'),

             -- Piña Colada
             ('Piña Colada', 'Ron blanco', 2.00, 'OZ'),
             ('Piña Colada', 'Jugo de ananá', 3.00, 'OZ'),
             ('Piña Colada', 'Crema de coco', 1.50, 'OZ'),

             -- Sex on the Beach
             ('Sex on the Beach', 'Vodka', 1.50, 'OZ'),
             ('Sex on the Beach', 'Licor de durazno', 0.75, 'OZ'),
             ('Sex on the Beach', 'Jugo de naranja', 2.00, 'OZ'),
             ('Sex on the Beach', 'Jugo de cranberry', 2.00, 'OZ'),

             -- Espresso Martini
             ('Espresso Martini', 'Vodka', 1.50, 'OZ'),
             ('Espresso Martini', 'Licor de café', 1.00, 'OZ'),
             ('Espresso Martini', 'Café espresso', 1.00, 'OZ'),
             ('Espresso Martini', 'Almíbar simple', 0.50, 'OZ'),

             -- Tequila Sunrise
             ('Tequila Sunrise', 'Tequila', 1.50, 'OZ'),
             ('Tequila Sunrise', 'Jugo de naranja', 3.00, 'OZ'),
             ('Tequila Sunrise', 'Granadina', 0.50, 'OZ'),

             -- Americano
             ('Americano', 'Campari', 1.50, 'OZ'),
             ('Americano', 'Vermouth rosso', 1.50, 'OZ'),
             ('Americano', 'Soda', 2.00, 'OZ'),

             -- Garibaldi
             ('Garibaldi', 'Campari', 1.50, 'OZ'),
             ('Garibaldi', 'Jugo de naranja', 4.00, 'OZ'),

             -- French 75
             ('French 75', 'Gin', 1.00, 'OZ'),
             ('French 75', 'Jugo de limón', 0.50, 'OZ'),
             ('French 75', 'Almíbar simple', 0.50, 'OZ'),
             ('French 75', 'Espumante', 3.00, 'OZ'),

             -- Bellini
             ('Bellini', 'Espumante', 4.00, 'OZ'),
             ('Bellini', 'Puré de durazno', 2.00, 'OZ'),

             -- Vodka Tonic
             ('Vodka Tonic', 'Vodka', 1.50, 'OZ'),
             ('Vodka Tonic', 'Agua tónica', 4.00, 'OZ'),

             -- Campari Tonic
             ('Campari Tonic', 'Campari', 1.50, 'OZ'),
             ('Campari Tonic', 'Agua tónica', 4.00, 'OZ'),

             -- Gin Fizz
             ('Gin Fizz', 'Gin', 1.50, 'OZ'),
             ('Gin Fizz', 'Jugo de limón', 0.75, 'OZ'),
             ('Gin Fizz', 'Almíbar simple', 0.75, 'OZ'),
             ('Gin Fizz', 'Soda', 3.00, 'OZ'),

             -- Vodka Collins
             ('Vodka Collins', 'Vodka', 1.50, 'OZ'),
             ('Vodka Collins', 'Jugo de limón', 1.00, 'OZ'),
             ('Vodka Collins', 'Almíbar simple', 0.75, 'OZ'),
             ('Vodka Collins', 'Soda', 3.00, 'OZ'),

             -- Caipirissima
             ('Caipirissima', 'Ron blanco', 2.00, 'OZ'),
             ('Caipirissima', 'Lima', 1.00, 'UNID'),
             ('Caipirissima', 'Azúcar', 2.00, 'UNID')
     ) AS v(cocktail_name, product_name, amount, unit)
         JOIN cocktails c ON c.name = v.cocktail_name
         JOIN products p ON p.name = v.product_name
WHERE NOT EXISTS (
    SELECT 1
    FROM cocktail_ingredients ci
    WHERE ci.cocktail_id = c.id
      AND ci.product_id = p.id
);