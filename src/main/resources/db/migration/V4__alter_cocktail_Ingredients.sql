ALTER TABLE cocktail_ingredients RENAME COLUMN ounces TO amount;
ALTER TABLE cocktail_ingredients ADD COLUMN unit VARCHAR(10) NOT NULL DEFAULT 'OZ';
UPDATE cocktail_ingredients SET unit = 'OZ' WHERE unit IS NULL;
