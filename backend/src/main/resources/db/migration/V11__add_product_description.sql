-- V11__add_product_description.sql
-- Agrega una descripción breve a los productos del catálogo.

ALTER TABLE products
    ADD COLUMN description VARCHAR(500);

-- =========================
-- Alcoholes
-- =========================

UPDATE products
SET description = 'Destilado de caña de azúcar de perfil suave y ligero, utilizado como base en cócteles como Mojito, Daiquiri y Cuba Libre.'
WHERE name = 'Ron blanco';

UPDATE products
SET description = 'Destilado neutro y versátil utilizado como base en una gran variedad de cócteles.'
WHERE name = 'Vodka';

UPDATE products
SET description = 'Destilado aromático elaborado principalmente con enebro y otros botánicos.'
WHERE name = 'Gin';

UPDATE products
SET description = 'Destilado elaborado a partir de agave, de sabor vegetal y característico.'
WHERE name = 'Tequila';

UPDATE products
SET description = 'Licor de naranja utilizado para aportar dulzor y notas cítricas a distintos cócteles.'
WHERE name = 'Triple sec';

UPDATE products
SET description = 'Aperitivo amargo de hierbas y especias, muy popular en Argentina y utilizado principalmente combinado con cola.'
WHERE name = 'Fernet';

UPDATE products
SET description = 'Aperitivo italiano de perfil agridulce, cítrico y levemente amargo.'
WHERE name = 'Aperol';

UPDATE products
SET description = 'Vino espumoso utilizado tanto como bebida como para completar cócteles y aperitivos.'
WHERE name = 'Espumante';

UPDATE products
SET description = 'Aperitivo italiano amargo de perfil herbal y cítrico, utilizado en clásicos como Negroni y Americano.'
WHERE name = 'Campari';

UPDATE products
SET description = 'Vino aromatizado dulce de perfil herbal y especiado, utilizado en cócteles clásicos.'
WHERE name = 'Vermouth rosso';

UPDATE products
SET description = 'Vino aromatizado seco y herbal, utilizado principalmente en cócteles como el Dry Martini.'
WHERE name = 'Vermouth dry';

UPDATE products
SET description = 'Destilado de cereales envejecido en madera, de perfil cálido, tostado y especiado.'
WHERE name = 'Whisky';

UPDATE products
SET description = 'Licor dulce con sabor y aroma predominante a café.'
WHERE name = 'Licor de café';

UPDATE products
SET description = 'Destilado brasileño elaborado a partir de jugo fresco de caña de azúcar, base tradicional de la Caipirinha.'
WHERE name = 'Cachaça';

UPDATE products
SET description = 'Licor dulce y frutal con sabor predominante a durazno.'
WHERE name = 'Licor de durazno';


-- =========================
-- Jugos y mixers
-- =========================

UPDATE products
SET description = 'Jugo cítrico de lima utilizado para aportar acidez a los cócteles. Producto inactivo en el catálogo actual.'
WHERE name = 'Jugo de lima';

UPDATE products
SET description = 'Jugo cítrico utilizado para aportar acidez y frescura a distintas preparaciones.'
WHERE name = 'Jugo de limón';

UPDATE products
SET description = 'Mezcla de azúcar y agua utilizada para endulzar y equilibrar cócteles.'
WHERE name = 'Almíbar simple';

UPDATE products
SET description = 'Bebida gasificada con quinina, de sabor amargo característico, utilizada principalmente en tragos largos.'
WHERE name = 'Agua tónica';

UPDATE products
SET description = 'Bebida cola gasificada utilizada como mixer en tragos largos y combinados.'
WHERE name = 'Coca Cola';

UPDATE products
SET description = 'Agua carbonatada sin sabor utilizada para completar y alargar cócteles.'
WHERE name = 'Soda';

UPDATE products
SET description = 'Café espresso concentrado utilizado como ingrediente en cócteles como el Espresso Martini.'
WHERE name = 'Café espresso';

UPDATE products
SET description = 'Bebida gasificada de jengibre, dulce y especiada, utilizada como mixer en cócteles como el Moscow Mule.'
WHERE name = 'Ginger beer';

UPDATE products
SET description = 'Jugo de pomelo de perfil cítrico, fresco y levemente amargo.'
WHERE name = 'Jugo de pomelo';

UPDATE products
SET description = 'Jugo de arándano rojo, de perfil frutal y ácido, utilizado en distintos cócteles.'
WHERE name = 'Jugo de cranberry';

UPDATE products
SET description = 'Jugo de ananá de perfil tropical, dulce y frutal.'
WHERE name = 'Jugo de ananá';

UPDATE products
SET description = 'Jugo de naranja utilizado como mixer y como ingrediente en cócteles frutales.'
WHERE name = 'Jugo de naranja';

UPDATE products
SET description = 'Preparado cremoso y dulce de coco utilizado principalmente en cócteles tropicales.'
WHERE name = 'Crema de coco';

UPDATE products
SET description = 'Jarabe dulce de granada utilizado para aportar dulzor, sabor frutal y color a los cócteles.'
WHERE name = 'Granadina';

UPDATE products
SET description = 'Preparado de durazno utilizado para aportar sabor y textura frutal a cócteles como el Bellini.'
WHERE name = 'Puré de durazno';


-- =========================
-- Frutas y hierbas
-- =========================

UPDATE products
SET description = 'Hierba aromática fresca utilizada para aportar aroma y frescura a distintas preparaciones.'
WHERE name = 'Menta';

UPDATE products
SET description = 'Fruta cítrica utilizada tanto por su jugo como en garnish y preparaciones de coctelería.'
WHERE name = 'Lima';

UPDATE products
SET description = 'Fruta cítrica utilizada para jugo, decoración y preparación de cócteles.'
WHERE name = 'Limón';

UPDATE products
SET description = 'Fruta cítrica utilizada para jugo, decoración y aporte aromático.'
WHERE name = 'Naranja';


-- =========================
-- Insumos
-- =========================

UPDATE products
SET description = 'Endulzante utilizado directamente o como base para preparar almíbares.'
WHERE name = 'Azúcar';

UPDATE products
SET description = 'Condimento utilizado en pequeñas cantidades y para escarchar el borde de determinadas copas.'
WHERE name = 'Sal';

UPDATE products
SET description = 'Bitter aromático concentrado de hierbas y especias, utilizado en pequeñas cantidades para aportar aroma, amargor y complejidad.'
WHERE name = 'Angostura bitters';

UPDATE products
SET description = 'Aceitunas utilizadas principalmente como garnish en cócteles como el Martini.'
WHERE name = 'Aceitunas';