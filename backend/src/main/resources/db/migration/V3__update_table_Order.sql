ALTER TABLE orders DROP COLUMN IF EXISTS cocktail_id;

CREATE TABLE order_cocktails (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    cocktail_id BIGINT NOT NULL,
    drinks INT NOT NULL,
    CONSTRAINT uq_order_cocktail UNIQUE (order_id, cocktail_id),
    CONSTRAINT fk_order_cocktails_order
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_cocktails_cocktail
        FOREIGN KEY (cocktail_id) REFERENCES cocktails (id) ON DELETE RESTRICT
    );

