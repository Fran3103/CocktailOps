-- =========================
-- Normalización de productos existentes
-- =========================

UPDATE products
SET name = 'Pulpa de durazno',
    description = 'Pulpa de durazno utilizada para aportar sabor, textura y perfil frutal a cócteles como el Bellini.'
WHERE name = 'Puré de durazno';


-- =========================
-- Nuevos productos
-- =========================

INSERT INTO products (
    category_id,
    name,
    description,
    unit,
    unit_size,
    active
)
SELECT
    c.id,
    v.name,
    v.description,
    v.unit,
    v.unit_size,
    true
FROM (
         VALUES

             -- Alcoholes
             (
                 'Alcoholes',
                 'Cognac',
                 'Destilado francés de vino envejecido en madera, de perfil cálido, frutal y aromático.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Pisco',
                 'Destilado de uva tradicional de Perú y Chile, utilizado como base en cócteles como el Pisco Sour.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Amaretto',
                 'Licor dulce de perfil almendrado utilizado en cócteles como el Amaretto Sour.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Absenta',
                 'Bebida alcohólica de alta graduación y marcado perfil herbal y anisado, utilizada en pequeñas cantidades en cócteles clásicos.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Licor Bénédictine',
                 'Licor francés de hierbas y especias, dulce y aromático, utilizado en cócteles clásicos como el Vieux Carré.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Licor de marrasquino',
                 'Licor elaborado a partir de cerezas marrascas, de perfil frutal, aromático y ligeramente seco.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Licor de violetas',
                 'Licor floral de violetas utilizado en pequeñas cantidades para aportar aroma, sabor y color a determinados cócteles.',
                 'ML',
                 750.00
             ),
             (
                 'Alcoholes',
                 'Ron añejo',
                 'Ron envejecido en madera, de perfil más intenso, complejo y especiado que el ron blanco.',
                 'ML',
                 750.00
             ),

             -- Insumos
             (
                 'Insumos',
                 'Bitter Peychaud''s',
                 'Bitter aromático concentrado de perfil especiado y levemente anisado, utilizado principalmente en cócteles clásicos como el Sazerac.',
                 'ML',
                 150.00
             ),
             (
                 'Insumos',
                 'Clara de huevo',
                 'Clara de huevo utilizada en coctelería para aportar textura, cuerpo y espuma a preparaciones como los cócteles sour.',
                 'ML',
                 1000.00
             ),
             (
                 'Insumos',
                 'Cebollitas para cóctel',
                 'Cebollitas pequeñas encurtidas utilizadas principalmente como garnish en cócteles como el Gibson.',
                 'UNID',
                 50.00
             ),
             (
                 'Insumos',
                 'Salmuera de aceitunas',
                 'Líquido salino de conservación de aceitunas utilizado para aportar sabor salado a cócteles como el Dirty Martini.',
                 'ML',
                 500.00
             ),

             -- Jugos y mixers
             (
                 'Jugos y mixers',
                 'Almíbar de almendras',
                 'Almíbar dulce elaborado a base de almendras, también conocido como orgeat, utilizado en cócteles tropicales como el Mai Tai.',
                 'ML',
                 1000.00
             ),
             (
                 'Jugos y mixers',
                 'Almíbar de miel',
                 'Preparado de miel diluida utilizado para endulzar cócteles y facilitar su integración con otros ingredientes.',
                 'ML',
                 1000.00
             ),
             (
                 'Jugos y mixers',
                 'Almíbar de frambuesa',
                 'Almíbar frutal utilizado para aportar dulzor, sabor a frambuesa y color a distintas preparaciones.',
                 'ML',
                 1000.00
             ),
             (
                 'Jugos y mixers',
                 'Pulpa de frutilla',
                 'Pulpa de frutilla utilizada principalmente en cócteles frozen y otras preparaciones frutales.',
                 'ML',
                 1000.00
             ),
             (
                 'Frutas y hierbas',
                 'Albahaca',
                 'Hierba aromática fresca de perfil herbal utilizada en cócteles como el Gin Basil Smash.',
                 'UNID',
                 1.00
             ),
             (
                 'Insumos',
                 'Bitter de naranja',
                 'Bitter aromático concentrado de perfil cítrico y especiado, utilizado en pequeñas cantidades en cócteles clásicos.',
                 'ML',
                 150.00
             ),
             (
                 'Insumos',
                 'Hielo',
                 'Hielo utilizado para enfriar, batir y preparar cócteles, especialmente preparaciones frozen.',
                 'GR',
                 15000.00
             )

     ) AS v(category_name, name, description, unit, unit_size)

         JOIN categories c
              ON c.name = v.category_name

WHERE NOT EXISTS (
    SELECT 1
    FROM products p
    WHERE p.name = v.name
);

-- =========================
-- Nuevos cócteles
-- =========================

INSERT INTO cocktails (
    name,
    description,
    created_at,
    image_url,
    image_alt
)
SELECT
    v.name,
    v.description,
    NOW(),
    NULL,
    v.image_alt
FROM (
         VALUES

             -- =========================
             -- DIRECTOS
             -- =========================

             (
                 'Mimosa',
                 'Trago fresco y ligero elaborado con espumante y jugo de naranja.',
                 'Cóctel Mimosa'
             ),
             (
                 'Screwdriver',
                 'Trago largo y simple elaborado con vodka y jugo de naranja.',
                 'Cóctel Screwdriver'
             ),
             (
                 'Whisky Highball',
                 'Trago largo y refrescante elaborado con whisky y soda.',
                 'Cóctel Whisky Highball'
             ),
             (
                 'Vodka Soda',
                 'Trago largo y ligero elaborado con vodka y soda.',
                 'Cóctel Vodka Soda'
             ),
             (
                 'Greyhound',
                 'Trago cítrico y refrescante elaborado con vodka y jugo de pomelo.',
                 'Cóctel Greyhound'
             ),
             (
                 'Sea Breeze',
                 'Trago frutal elaborado con vodka, cranberry y jugo de pomelo.',
                 'Cóctel Sea Breeze'
             ),
             (
                 'Ranch Water',
                 'Trago largo y refrescante elaborado con tequila, cítrico y soda.',
                 'Cóctel Ranch Water'
             ),
             (
                 'Rum & Tonic',
                 'Trago largo elaborado con ron blanco y agua tónica.',
                 'Cóctel Rum & Tonic'
             ),


             -- =========================
             -- BATIDOS
             -- =========================

             (
                 'White Lady',
                 'Cóctel clásico batido elaborado con gin, triple sec y limón.',
                 'Cóctel White Lady'
             ),
             (
                 'Kamikaze',
                 'Cóctel batido elaborado con vodka, triple sec y cítrico.',
                 'Cóctel Kamikaze'
             ),
             (
                 'Lemon Drop Martini',
                 'Cóctel batido de vodka, triple sec, limón y almíbar.',
                 'Cóctel Lemon Drop Martini'
             ),
             (
                 'Sidecar',
                 'Cóctel clásico batido elaborado con cognac, triple sec y limón.',
                 'Cóctel Sidecar'
             ),
             (
                 'Pisco Sour',
                 'Cóctel clásico a base de pisco, limón, almíbar y clara de huevo.',
                 'Cóctel Pisco Sour'
             ),
             (
                 'Amaretto Sour',
                 'Cóctel batido de perfil dulce y cítrico elaborado con amaretto y limón.',
                 'Cóctel Amaretto Sour'
             ),
             (
                 'Aviation',
                 'Cóctel clásico de gin con marrasquino, licor de violetas y limón.',
                 'Cóctel Aviation'
             ),
             (
                 'Clover Club',
                 'Cóctel batido de gin, limón, frambuesa y clara de huevo.',
                 'Cóctel Clover Club'
             ),
             (
                 'Mai Tai',
                 'Cóctel tropical con ron, licor de naranja, cítrico y almíbar de almendras.',
                 'Cóctel Mai Tai'
             ),
             (
                 'Bee''s Knees',
                 'Cóctel batido de gin, limón y almíbar de miel.',
                 'Cóctel Bee''s Knees'
             ),
             (
                 'Gin Basil Smash',
                 'Cóctel batido de gin, limón, almíbar y albahaca fresca.',
                 'Cóctel Gin Basil Smash'
             ),


             -- =========================
             -- REFRESCADOS
             -- =========================

             (
                 'Negroni Sbagliato',
                 'Variante del Negroni elaborada con Campari, vermouth rosso y espumante.',
                 'Cóctel Negroni Sbagliato'
             ),
             (
                 'Manhattan',
                 'Cóctel clásico de whisky, vermouth rosso y bitters.',
                 'Cóctel Manhattan'
             ),
             (
                 'Boulevardier',
                 'Cóctel clásico de whisky, Campari y vermouth rosso.',
                 'Cóctel Boulevardier'
             ),
             (
                 'Rob Roy',
                 'Cóctel clásico de whisky, vermouth rosso y bitters.',
                 'Cóctel Rob Roy'
             ),
             (
                 'Vodka Martini',
                 'Variante del Martini elaborada con vodka y vermouth dry.',
                 'Cóctel Vodka Martini'
             ),
             (
                 'Perfect Manhattan',
                 'Variante del Manhattan elaborada con whisky, vermouth rosso y vermouth dry.',
                 'Cóctel Perfect Manhattan'
             ),
             (
                 'Hanky Panky',
                 'Cóctel clásico de gin, vermouth rosso y Fernet.',
                 'Cóctel Hanky Panky'
             ),
             (
                 'Gibson',
                 'Variante seca del Martini acompañada con cebollitas encurtidas.',
                 'Cóctel Gibson'
             ),
             (
                 'Dirty Martini',
                 'Variante del Martini con vermouth dry y salmuera de aceitunas.',
                 'Cóctel Dirty Martini'
             ),
             (
                 'Martinez',
                 'Cóctel clásico de gin, vermouth rosso y licor de marrasquino.',
                 'Cóctel Martinez'
             ),
             (
                 'Vieux Carré',
                 'Cóctel clásico y complejo elaborado con whisky, cognac, vermouth y licores herbales.',
                 'Cóctel Vieux Carré'
             ),
             (
                 'Sazerac',
                 'Cóctel clásico de whisky con azúcar, bitters y un toque de absenta.',
                 'Cóctel Sazerac'
             ),


             -- =========================
             -- FROZEN
             -- =========================

             (
                 'Frozen Daiquiri',
                 'Versión frozen del Daiquiri clásico con ron blanco, cítrico, almíbar y hielo.',
                 'Cóctel Frozen Daiquiri'
             ),
             (
                 'Frozen Margarita',
                 'Versión frozen de la Margarita con tequila, triple sec, cítrico y hielo.',
                 'Cóctel Frozen Margarita'
             ),
             (
                 'Daiquiri Frozen de Frutilla',
                 'Daiquiri frozen frutal elaborado con ron blanco y pulpa de frutilla.',
                 'Cóctel Daiquiri Frozen de Frutilla'
             ),
             (
                 'Frozen Caipirinha',
                 'Versión frozen de la Caipirinha elaborada con cachaça, lima, azúcar y hielo.',
                 'Cóctel Frozen Caipirinha'
             ),
             (
                 'Frozen Caipiroska',
                 'Versión frozen de la Caipiroska elaborada con vodka, lima, azúcar y hielo.',
                 'Cóctel Frozen Caipiroska'
             ),
             (
                 'Daiquiri Frozen de Durazno',
                 'Daiquiri frozen frutal elaborado con ron blanco y pulpa de durazno.',
                 'Cóctel Daiquiri Frozen de Durazno'
             )

     ) AS v(name, description, image_alt)

WHERE NOT EXISTS (
    SELECT 1
    FROM cocktails c
    WHERE c.name = v.name
);

-- =========================
-- Ingredientes - DIRECTOS
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

             -- Mimosa
             ('Mimosa', 'Espumante', 3.00, 'OZ'),
             ('Mimosa', 'Jugo de naranja', 3.00, 'OZ'),

             -- Screwdriver
             ('Screwdriver', 'Vodka', 1.50, 'OZ'),
             ('Screwdriver', 'Jugo de naranja', 4.00, 'OZ'),

             -- Whisky Highball
             ('Whisky Highball', 'Whisky', 2.00, 'OZ'),
             ('Whisky Highball', 'Soda', 4.00, 'OZ'),

             -- Vodka Soda
             ('Vodka Soda', 'Vodka', 1.50, 'OZ'),
             ('Vodka Soda', 'Soda', 4.00, 'OZ'),

             -- Greyhound
             ('Greyhound', 'Vodka', 1.50, 'OZ'),
             ('Greyhound', 'Jugo de pomelo', 4.00, 'OZ'),

             -- Sea Breeze
             ('Sea Breeze', 'Vodka', 1.50, 'OZ'),
             ('Sea Breeze', 'Jugo de cranberry', 3.00, 'OZ'),
             ('Sea Breeze', 'Jugo de pomelo', 1.50, 'OZ'),

             -- Ranch Water
             ('Ranch Water', 'Tequila', 1.50, 'OZ'),
             ('Ranch Water', 'Jugo de limón', 0.75, 'OZ'),
             ('Ranch Water', 'Soda', 4.00, 'OZ'),

             -- Rum & Tonic
             ('Rum & Tonic', 'Ron blanco', 1.50, 'OZ'),
             ('Rum & Tonic', 'Agua tónica', 4.00, 'OZ')

     ) AS v(cocktail_name, product_name, amount, unit)

         JOIN cocktails c
              ON c.name = v.cocktail_name

         JOIN products p
              ON p.name = v.product_name

WHERE NOT EXISTS (
    SELECT 1
    FROM cocktail_ingredients ci
    WHERE ci.cocktail_id = c.id
      AND ci.product_id = p.id
);

-- =========================
-- Ingredientes - BATIDOS
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

             -- White Lady
             ('White Lady', 'Gin', 1.50, 'OZ'),
             ('White Lady', 'Triple sec', 0.75, 'OZ'),
             ('White Lady', 'Jugo de limón', 0.75, 'OZ'),

             -- Kamikaze
             ('Kamikaze', 'Vodka', 1.50, 'OZ'),
             ('Kamikaze', 'Triple sec', 0.75, 'OZ'),
             ('Kamikaze', 'Jugo de limón', 0.75, 'OZ'),

             -- Lemon Drop Martini
             ('Lemon Drop Martini', 'Vodka', 1.50, 'OZ'),
             ('Lemon Drop Martini', 'Triple sec', 0.75, 'OZ'),
             ('Lemon Drop Martini', 'Jugo de limón', 0.75, 'OZ'),
             ('Lemon Drop Martini', 'Almíbar simple', 0.50, 'OZ'),

             -- Sidecar
             ('Sidecar', 'Cognac', 2.00, 'OZ'),
             ('Sidecar', 'Triple sec', 0.75, 'OZ'),
             ('Sidecar', 'Jugo de limón', 0.75, 'OZ'),

             -- Pisco Sour
             ('Pisco Sour', 'Pisco', 2.00, 'OZ'),
             ('Pisco Sour', 'Jugo de limón', 1.00, 'OZ'),
             ('Pisco Sour', 'Almíbar simple', 0.75, 'OZ'),
             ('Pisco Sour', 'Clara de huevo', 0.75, 'OZ'),
             ('Pisco Sour', 'Angostura bitters', 0.05, 'OZ'),

             -- Amaretto Sour
             ('Amaretto Sour', 'Amaretto', 1.50, 'OZ'),
             ('Amaretto Sour', 'Jugo de limón', 0.75, 'OZ'),
             ('Amaretto Sour', 'Almíbar simple', 0.50, 'OZ'),
             ('Amaretto Sour', 'Clara de huevo', 0.75, 'OZ'),

             -- Aviation
             ('Aviation', 'Gin', 1.50, 'OZ'),
             ('Aviation', 'Licor de marrasquino', 0.50, 'OZ'),
             ('Aviation', 'Licor de violetas', 0.25, 'OZ'),
             ('Aviation', 'Jugo de limón', 0.75, 'OZ'),

             -- Clover Club
             ('Clover Club', 'Gin', 1.50, 'OZ'),
             ('Clover Club', 'Jugo de limón', 0.75, 'OZ'),
             ('Clover Club', 'Almíbar de frambuesa', 0.50, 'OZ'),
             ('Clover Club', 'Clara de huevo', 0.75, 'OZ'),

             -- Mai Tai
             ('Mai Tai', 'Ron blanco', 1.00, 'OZ'),
             ('Mai Tai', 'Ron añejo', 1.00, 'OZ'),
             ('Mai Tai', 'Triple sec', 0.50, 'OZ'),
             ('Mai Tai', 'Jugo de limón', 0.75, 'OZ'),
             ('Mai Tai', 'Almíbar de almendras', 0.50, 'OZ'),

             -- Bee's Knees
             ('Bee''s Knees', 'Gin', 2.00, 'OZ'),
             ('Bee''s Knees', 'Jugo de limón', 0.75, 'OZ'),
             ('Bee''s Knees', 'Almíbar de miel', 0.75, 'OZ'),

             -- Gin Basil Smash
             ('Gin Basil Smash', 'Gin', 2.00, 'OZ'),
             ('Gin Basil Smash', 'Jugo de limón', 0.75, 'OZ'),
             ('Gin Basil Smash', 'Almíbar simple', 0.75, 'OZ'),
             ('Gin Basil Smash', 'Albahaca', 0.05, 'UNID')

     ) AS v(cocktail_name, product_name, amount, unit)

         JOIN cocktails c
              ON c.name = v.cocktail_name

         JOIN products p
              ON p.name = v.product_name

WHERE NOT EXISTS (
    SELECT 1
    FROM cocktail_ingredients ci
    WHERE ci.cocktail_id = c.id
      AND ci.product_id = p.id
);


-- =========================
-- Ingredientes - REFRESCADOS
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

             -- Negroni Sbagliato
             ('Negroni Sbagliato', 'Campari', 1.00, 'OZ'),
             ('Negroni Sbagliato', 'Vermouth rosso', 1.00, 'OZ'),
             ('Negroni Sbagliato', 'Espumante', 1.00, 'OZ'),

             -- Manhattan
             ('Manhattan', 'Whisky', 2.00, 'OZ'),
             ('Manhattan', 'Vermouth rosso', 1.00, 'OZ'),
             ('Manhattan', 'Angostura bitters', 0.10, 'OZ'),

             -- Boulevardier
             ('Boulevardier', 'Whisky', 1.50, 'OZ'),
             ('Boulevardier', 'Campari', 1.00, 'OZ'),
             ('Boulevardier', 'Vermouth rosso', 1.00, 'OZ'),

             -- Rob Roy
             ('Rob Roy', 'Whisky', 2.00, 'OZ'),
             ('Rob Roy', 'Vermouth rosso', 1.00, 'OZ'),
             ('Rob Roy', 'Angostura bitters', 0.10, 'OZ'),

             -- Vodka Martini
             ('Vodka Martini', 'Vodka', 2.50, 'OZ'),
             ('Vodka Martini', 'Vermouth dry', 0.50, 'OZ'),
             ('Vodka Martini', 'Aceitunas', 1.00, 'UNID'),

             -- Perfect Manhattan
             ('Perfect Manhattan', 'Whisky', 2.00, 'OZ'),
             ('Perfect Manhattan', 'Vermouth rosso', 0.50, 'OZ'),
             ('Perfect Manhattan', 'Vermouth dry', 0.50, 'OZ'),
             ('Perfect Manhattan', 'Angostura bitters', 0.10, 'OZ'),

             -- Hanky Panky
             ('Hanky Panky', 'Gin', 1.50, 'OZ'),
             ('Hanky Panky', 'Vermouth rosso', 1.50, 'OZ'),
             ('Hanky Panky', 'Fernet', 0.25, 'OZ'),

             -- Gibson
             ('Gibson', 'Gin', 2.50, 'OZ'),
             ('Gibson', 'Vermouth dry', 0.50, 'OZ'),
             ('Gibson', 'Cebollitas para cóctel', 1.00, 'UNID'),

             -- Dirty Martini
             ('Dirty Martini', 'Gin', 2.50, 'OZ'),
             ('Dirty Martini', 'Vermouth dry', 0.50, 'OZ'),
             ('Dirty Martini', 'Salmuera de aceitunas', 0.50, 'OZ'),
             ('Dirty Martini', 'Aceitunas', 1.00, 'UNID'),

             -- Martinez
             ('Martinez', 'Gin', 1.50, 'OZ'),
             ('Martinez', 'Vermouth rosso', 1.50, 'OZ'),
             ('Martinez', 'Licor de marrasquino', 0.25, 'OZ'),
             ('Martinez', 'Bitter de naranja', 0.10, 'OZ'),

             -- Vieux Carré
             ('Vieux Carré', 'Whisky', 0.75, 'OZ'),
             ('Vieux Carré', 'Cognac', 0.75, 'OZ'),
             ('Vieux Carré', 'Vermouth rosso', 0.75, 'OZ'),
             ('Vieux Carré', 'Licor Bénédictine', 0.25, 'OZ'),
             ('Vieux Carré', 'Angostura bitters', 0.05, 'OZ'),
             ('Vieux Carré', 'Bitter Peychaud''s', 0.05, 'OZ'),

             -- Sazerac
             ('Sazerac', 'Whisky', 2.00, 'OZ'),
             ('Sazerac', 'Azúcar', 5.00, 'GR'),
             ('Sazerac', 'Bitter Peychaud''s', 0.10, 'OZ'),
             ('Sazerac', 'Absenta', 0.10, 'OZ')

     ) AS v(cocktail_name, product_name, amount, unit)

         JOIN cocktails c
              ON c.name = v.cocktail_name

         JOIN products p
              ON p.name = v.product_name

WHERE NOT EXISTS (
    SELECT 1
    FROM cocktail_ingredients ci
    WHERE ci.cocktail_id = c.id
      AND ci.product_id = p.id
);

-- =========================
-- Ingredientes - FROZEN
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

             -- Piña Colada existente: agregamos hielo
             ('Piña Colada', 'Hielo', 180.00, 'GR'),

             -- Frozen Daiquiri
             ('Frozen Daiquiri', 'Ron blanco', 2.00, 'OZ'),
             ('Frozen Daiquiri', 'Jugo de limón', 1.00, 'OZ'),
             ('Frozen Daiquiri', 'Almíbar simple', 0.75, 'OZ'),
             ('Frozen Daiquiri', 'Hielo', 180.00, 'GR'),

             -- Frozen Margarita
             ('Frozen Margarita', 'Tequila', 2.00, 'OZ'),
             ('Frozen Margarita', 'Triple sec', 1.00, 'OZ'),
             ('Frozen Margarita', 'Jugo de limón', 1.00, 'OZ'),
             ('Frozen Margarita', 'Hielo', 180.00, 'GR'),

             -- Daiquiri Frozen de Frutilla
             ('Daiquiri Frozen de Frutilla', 'Ron blanco', 2.00, 'OZ'),
             ('Daiquiri Frozen de Frutilla', 'Pulpa de frutilla', 2.00, 'OZ'),
             ('Daiquiri Frozen de Frutilla', 'Jugo de limón', 0.75, 'OZ'),
             ('Daiquiri Frozen de Frutilla', 'Almíbar simple', 0.50, 'OZ'),
             ('Daiquiri Frozen de Frutilla', 'Hielo', 180.00, 'GR'),

             -- Frozen Caipirinha
             ('Frozen Caipirinha', 'Cachaça', 2.00, 'OZ'),
             ('Frozen Caipirinha', 'Lima', 1.00, 'UNID'),
             ('Frozen Caipirinha', 'Azúcar', 10.00, 'GR'),
             ('Frozen Caipirinha', 'Hielo', 180.00, 'GR'),

             -- Frozen Caipiroska
             ('Frozen Caipiroska', 'Vodka', 2.00, 'OZ'),
             ('Frozen Caipiroska', 'Lima', 1.00, 'UNID'),
             ('Frozen Caipiroska', 'Azúcar', 10.00, 'GR'),
             ('Frozen Caipiroska', 'Hielo', 180.00, 'GR'),

             -- Daiquiri Frozen de Durazno
             ('Daiquiri Frozen de Durazno', 'Ron blanco', 2.00, 'OZ'),
             ('Daiquiri Frozen de Durazno', 'Pulpa de durazno', 2.00, 'OZ'),
             ('Daiquiri Frozen de Durazno', 'Jugo de limón', 0.75, 'OZ'),
             ('Daiquiri Frozen de Durazno', 'Almíbar simple', 0.50, 'OZ'),
             ('Daiquiri Frozen de Durazno', 'Hielo', 180.00, 'GR')

     ) AS v(cocktail_name, product_name, amount, unit)

         JOIN cocktails c
              ON c.name = v.cocktail_name

         JOIN products p
              ON p.name = v.product_name

WHERE NOT EXISTS (
    SELECT 1
    FROM cocktail_ingredients ci
    WHERE ci.cocktail_id = c.id
      AND ci.product_id = p.id
);