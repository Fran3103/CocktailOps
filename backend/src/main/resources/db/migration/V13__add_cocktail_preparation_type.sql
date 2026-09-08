-- V13__add_cocktail_preparation_type.sql
-- Agrega y completa el tipo de preparación de todos los cócteles.

ALTER TABLE cocktails
    ADD COLUMN preparation_type VARCHAR(20);


-- =========================
-- DIRECTOS
-- =========================

UPDATE cocktails
SET preparation_type = 'DIRECT'
WHERE name IN (
               'Mojito',
               'Gin Tonic',
               'Fernet Cola',
               'Aperol Spritz',
               'Cuba Libre',
               'Moscow Mule',
               'Paloma',
               'Tequila Sunrise',
               'Americano',
               'Garibaldi',
               'Bellini',
               'Vodka Tonic',
               'Campari Tonic',
               'Tom Collins',
               'Vodka Collins',
               'Mimosa',
               'Screwdriver',
               'Whisky Highball',
               'Vodka Soda',
               'Greyhound',
               'Sea Breeze',
               'Ranch Water',
               'Rum & Tonic'
    );


-- =========================
-- BATIDOS
-- =========================

UPDATE cocktails
SET preparation_type = 'SHAKEN'
WHERE name IN (
               'Daiquiri',
               'Margarita',
               'Whisky Sour',
               'Cosmopolitan',
               'Espresso Martini',
               'French 75',
               'Gin Fizz',
               'Caipirinha',
               'Caipiroska',
               'Caipirissima',
               'Sex on the Beach',
               'White Lady',
               'Kamikaze',
               'Lemon Drop Martini',
               'Sidecar',
               'Pisco Sour',
               'Amaretto Sour',
               'Aviation',
               'Clover Club',
               'Mai Tai',
               'Bee''s Knees',
               'Gin Basil Smash'
    );


-- =========================
-- REFRESCADOS
-- =========================

UPDATE cocktails
SET preparation_type = 'STIRRED'
WHERE name IN (
               'Negroni',
               'Old Fashioned',
               'Dry Martini',
               'Negroni Sbagliato',
               'Manhattan',
               'Boulevardier',
               'Rob Roy',
               'Vodka Martini',
               'Perfect Manhattan',
               'Hanky Panky',
               'Gibson',
               'Dirty Martini',
               'Martinez',
               'Vieux Carré',
               'Sazerac'
    );


-- =========================
-- FROZEN
-- =========================

UPDATE cocktails
SET preparation_type = 'FROZEN'
WHERE name IN (
               'Piña Colada',
               'Frozen Daiquiri',
               'Frozen Margarita',
               'Daiquiri Frozen de Frutilla',
               'Frozen Caipirinha',
               'Frozen Caipiroska',
               'Daiquiri Frozen de Durazno'
    );


-- Evita valores libres fuera del enum de la aplicación.
ALTER TABLE cocktails
    ADD CONSTRAINT chk_cocktails_preparation_type
        CHECK (
            preparation_type IN (
                                 'DIRECT',
                                 'SHAKEN',
                                 'STIRRED',
                                 'FROZEN'
                )
            );

ALTER TABLE cocktails
    ALTER COLUMN preparation_type SET NOT NULL;