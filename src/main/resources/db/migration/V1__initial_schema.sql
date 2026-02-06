-- Shops

CREATE TABLE IF NOT EXISTS shops (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE ,
    created_at TIMESTAMP NOT NULL DEFAULT NOW());

-- Users

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    shop_id     BIGINT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT fk_users_shop
        FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE SET NULL
                   );


-- Categories

CREATE TABLE IF NOT EXISTS categories (
    id          BIGSERIAL PRIMARY KEY,
    shop_id     BIGINT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(100) NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_categories_shop
    FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE,
    CONSTRAINT uq_categories_shop_slug
    UNIQUE (shop_id, slug)
    );

-- Products
CREATE TABLE IF NOT EXISTS products (
    id          BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    unit        VARCHAR(50),
    unit_size   NUMERIC(10,2),
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_products_category
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
    );

-- Shop_products (producto publicado en una tienda específica)
CREATE TABLE IF NOT EXISTS shop_products (
    id           BIGSERIAL PRIMARY KEY,
    shop_id      BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    purchase_url TEXT,
    price        NUMERIC(12,2),
    sku          VARCHAR(100),
    active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT fk_shop_products_shop
    FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE,
    CONSTRAINT fk_shop_products_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_shop_products_shop_product
    UNIQUE (shop_id, product_id)
    );


-- Cocktails
CREATE TABLE IF NOT EXISTS cocktails (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
    );

--Cocktail_ingredients
CREATE TABLE IF NOT EXISTS cocktail_ingredients (
    id          BIGSERIAL PRIMARY KEY,
    cocktail_id BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    ounces      NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_ci_cocktail
    FOREIGN KEY (cocktail_id) REFERENCES cocktails(id) ON DELETE CASCADE,
    CONSTRAINT fk_ci_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    CONSTRAINT uq_ci_cocktail_product
    UNIQUE (cocktail_id, product_id)
    );

-- Orders

-- ORDERS
CREATE TABLE IF NOT EXISTS orders (
    id               BIGSERIAL PRIMARY KEY,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    guests           INTEGER NOT NULL CHECK (guests > 0),
    drinks_per_person INTEGER NOT NULL ,
    duration_hours   INTEGER NOT NULL ,
    cocktail_id      BIGINT NOT NULL,
    status           VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    CONSTRAINT fk_orders_cocktail
    FOREIGN KEY (cocktail_id) REFERENCES cocktails(id) ON DELETE RESTRICT
    );


-- Order_items

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INTEGER NOT NULL ,
    unit       VARCHAR(50),
    CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    CONSTRAINT uq_order_items_order_product
    UNIQUE (order_id, product_id)
    );

-- Indexes

CREATE INDEX IF NOT EXISTS idx_users_shop_id ON users(shop_id);

CREATE INDEX IF NOT EXISTS idx_categories_shop_id ON categories(shop_id);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);

CREATE INDEX IF NOT EXISTS idx_shop_products_shop_id ON shop_products(shop_id);
CREATE INDEX IF NOT EXISTS idx_shop_products_product_id ON shop_products(product_id);

CREATE INDEX IF NOT EXISTS idx_ci_cocktail_id ON cocktail_ingredients(cocktail_id);
CREATE INDEX IF NOT EXISTS idx_ci_product_id ON cocktail_ingredients(product_id);

CREATE INDEX IF NOT EXISTS idx_orders_cocktail_id ON orders(cocktail_id);

CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product_id ON order_items(product_id);