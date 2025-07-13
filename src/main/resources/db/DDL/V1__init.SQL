-- DDL script to create the FastFood database schema
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Sequence para o campo number da tabela Order
CREATE SEQUENCE sq_order_number START WITH 1 INCREMENT BY 1;

-- Tabela Product
CREATE TABLE tb_product
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT    NOT NULL,
    description TEXT,
    price       NUMERIC NOT NULL,
    category    TEXT    NOT NULL,
    image_url   TEXT,
    created_at  TIMESTAMPTZ      DEFAULT now(),
    updated_at  TIMESTAMPTZ      DEFAULT now()
);

-- Tabela Order
CREATE TABLE tb_order
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    number        BIGINT NOT NULL,
    customer_id   UUID   NOT NULL,
    status        TEXT   NOT NULL,
    order_date    TIMESTAMPTZ      DEFAULT now(),
    finished_date TIMESTAMPTZ,
    created_at    TIMESTAMPTZ      DEFAULT now(),
    updated_at    TIMESTAMPTZ      DEFAULT now()
);

-- Tabela OrderItem
CREATE TABLE tb_order_item
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id   UUID REFERENCES tb_order (id) ON DELETE CASCADE,
    product_id UUID REFERENCES tb_product (id),
    quantity   INT NOT NULL,
    created_at TIMESTAMPTZ      DEFAULT now(),
    updated_at TIMESTAMPTZ      DEFAULT now()
);
-- Trigger function to set the order number
CREATE
OR REPLACE FUNCTION set_order_number()
RETURNS TRIGGER AS $$
BEGIN
    NEW.number
:= nextval('sq_order_number');
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Add the trigger to the table
CREATE TRIGGER order_number_trigger
    BEFORE INSERT
    ON tb_order
    FOR EACH ROW
    EXECUTE FUNCTION set_order_number();