-- ============================================================
-- V15 - Ajuste operativo de recetas
--
-- Objetivos:
-- 1. Estandarizar varios destilados base a 1.5 oz.
-- 2. Ajustar mixers para conservar volumen y balance.
-- 3. Reducir Martinis de 2.5 oz a 2 oz de destilado.
-- 4. Reemplazar azúcar granulada por almíbar en la familia Caipi.
-- 5. Ajustar consumo de lima y garnish para compras más realistas.
-- ============================================================


-- ============================================================
-- APEROL SPRITZ (cocktail 6)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 6 AND product_id = 7; -- Aperol

UPDATE cocktail_ingredients
SET amount = 3.50
WHERE cocktail_id = 6 AND product_id = 8; -- Espumante


-- ============================================================
-- BEE'S KNEES (cocktail 39)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 39 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 39 AND product_id = 10; -- Jugo de limón

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 39 AND product_id = 49; -- Almíbar de miel


-- ============================================================
-- CAIPIRINHA (cocktail 8)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 8 AND product_id = 22; -- Cachaça

UPDATE cocktail_ingredients
SET amount = 0.50
WHERE cocktail_id = 8 AND product_id = 16; -- Lima

DELETE FROM cocktail_ingredients
WHERE cocktail_id = 8 AND product_id = 19; -- Azúcar

INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES (8, 11, 1.00, 'OZ')
ON CONFLICT (cocktail_id, product_id)
    DO UPDATE SET amount = EXCLUDED.amount, unit = EXCLUDED.unit;


-- ============================================================
-- CAIPIRISSIMA (cocktail 29)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 29 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 0.50
WHERE cocktail_id = 29 AND product_id = 16; -- Lima

DELETE FROM cocktail_ingredients
WHERE cocktail_id = 29 AND product_id = 19; -- Azúcar

INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES (29, 11, 1.00, 'OZ')
ON CONFLICT (cocktail_id, product_id)
    DO UPDATE SET amount = EXCLUDED.amount, unit = EXCLUDED.unit;


-- ============================================================
-- CAIPIROSKA (cocktail 25)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 25 AND product_id = 2; -- Vodka

UPDATE cocktail_ingredients
SET amount = 0.50
WHERE cocktail_id = 25 AND product_id = 16; -- Lima

DELETE FROM cocktail_ingredients
WHERE cocktail_id = 25 AND product_id = 19; -- Azúcar

INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES (25, 11, 1.00, 'OZ')
ON CONFLICT (cocktail_id, product_id)
    DO UPDATE SET amount = EXCLUDED.amount, unit = EXCLUDED.unit;


-- ============================================================
-- CUBA LIBRE (cocktail 7)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 7 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 5.50
WHERE cocktail_id = 7 AND product_id = 13; -- Coca Cola


-- ============================================================
-- DAIQUIRI (cocktail 2)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 2 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 2 AND product_id = 10; -- Jugo de limón

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 2 AND product_id = 11; -- Almíbar simple


-- ============================================================
-- DAIQUIRI FROZEN DURAZNO (cocktail 61)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 61 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 2.50
WHERE cocktail_id = 61 AND product_id = 32; -- Pulpa durazno


-- ============================================================
-- DAIQUIRI FROZEN FRUTILLA (cocktail 45)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 45 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 2.50
WHERE cocktail_id = 45 AND product_id = 48; -- Pulpa frutilla


-- ============================================================
-- FERNET COLA (cocktail 5)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 5 AND product_id = 6; -- Fernet

UPDATE cocktail_ingredients
SET amount = 5.50
WHERE cocktail_id = 5 AND product_id = 13; -- Coca Cola


-- ============================================================
-- FROZEN CAIPIRINHA (cocktail 41)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 41 AND product_id = 22; -- Cachaça

UPDATE cocktail_ingredients
SET amount = 0.50
WHERE cocktail_id = 41 AND product_id = 16; -- Lima

DELETE FROM cocktail_ingredients
WHERE cocktail_id = 41 AND product_id = 19; -- Azúcar

INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES (41, 11, 1.00, 'OZ')
ON CONFLICT (cocktail_id, product_id)
    DO UPDATE SET amount = EXCLUDED.amount, unit = EXCLUDED.unit;


-- ============================================================
-- FROZEN CAIPIROSKA (cocktail 64)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 64 AND product_id = 2; -- Vodka

UPDATE cocktail_ingredients
SET amount = 0.50
WHERE cocktail_id = 64 AND product_id = 16; -- Lima

DELETE FROM cocktail_ingredients
WHERE cocktail_id = 64 AND product_id = 19; -- Azúcar

INSERT INTO cocktail_ingredients (cocktail_id, product_id, amount, unit)
VALUES (64, 11, 1.00, 'OZ')
ON CONFLICT (cocktail_id, product_id)
    DO UPDATE SET amount = EXCLUDED.amount, unit = EXCLUDED.unit;


-- ============================================================
-- FROZEN DAIQUIRI (cocktail 46)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 46 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 46 AND product_id = 10; -- Jugo de limón

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 46 AND product_id = 11; -- Almíbar simple


-- ============================================================
-- FROZEN MARGARITA (cocktail 43)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 43 AND product_id = 4; -- Tequila

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 43 AND product_id = 5; -- Triple sec

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 43 AND product_id = 10; -- Jugo de limón


-- ============================================================
-- GIN BASIL SMASH (cocktail 40)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 40 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 40 AND product_id = 10; -- Jugo de limón

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 40 AND product_id = 11; -- Almíbar simple


-- ============================================================
-- GIN TONIC (cocktail 3)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 3 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 4.50
WHERE cocktail_id = 3 AND product_id = 12; -- Agua tónica


-- ============================================================
-- MARGARITA (cocktail 4)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 4 AND product_id = 4; -- Tequila

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 4 AND product_id = 5; -- Triple sec

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 4 AND product_id = 10; -- Jugo de limón


-- ============================================================
-- MOJITO (cocktail 1)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 1 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 2.50
WHERE cocktail_id = 1 AND product_id = 14; -- Soda


-- ============================================================
-- PISCO SOUR (cocktail 31)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 31 AND product_id = 40; -- Pisco

UPDATE cocktail_ingredients
SET amount = 1.25
WHERE cocktail_id = 31 AND product_id = 10; -- Jugo de limón

UPDATE cocktail_ingredients
SET amount = 1.00
WHERE cocktail_id = 31 AND product_id = 11; -- Almíbar simple


-- ============================================================
-- PIÑA COLADA (cocktail 15)
-- ============================================================

UPDATE cocktail_ingredients
SET amount = 1.50
WHERE cocktail_id = 15 AND product_id = 1; -- Ron blanco

UPDATE cocktail_ingredients
SET amount = 3.50
WHERE cocktail_id = 15 AND product_id = 33; -- Jugo de ananá


-- ============================================================
-- MARTINIS / SPIRIT-FORWARD
-- ============================================================

-- Dirty Martini (47)
UPDATE cocktail_ingredients
SET amount = 2.00
WHERE cocktail_id = 47 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 0.75
WHERE cocktail_id = 47 AND product_id = 24; -- Vermouth dry

UPDATE cocktail_ingredients
SET amount = 0.25
WHERE cocktail_id = 47 AND product_id = 38; -- Aceitunas


-- Dry Martini (30)
UPDATE cocktail_ingredients
SET amount = 2.00
WHERE cocktail_id = 30 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 0.75
WHERE cocktail_id = 30 AND product_id = 24; -- Vermouth dry

UPDATE cocktail_ingredients
SET amount = 0.25
WHERE cocktail_id = 30 AND product_id = 38; -- Aceitunas


-- Gibson (50)
UPDATE cocktail_ingredients
SET amount = 2.00
WHERE cocktail_id = 50 AND product_id = 3; -- Gin

UPDATE cocktail_ingredients
SET amount = 0.75
WHERE cocktail_id = 50 AND product_id = 24; -- Vermouth dry

UPDATE cocktail_ingredients
SET amount = 0.25
WHERE cocktail_id = 50 AND product_id = 54; -- Cebollitas


-- Vodka Martini (52)
UPDATE cocktail_ingredients
SET amount = 2.00
WHERE cocktail_id = 52 AND product_id = 2; -- Vodka

UPDATE cocktail_ingredients
SET amount = 0.75
WHERE cocktail_id = 52 AND product_id = 24; -- Vermouth dry

UPDATE cocktail_ingredients
SET amount = 0.25
WHERE cocktail_id = 52 AND product_id = 38; -- Aceitunas