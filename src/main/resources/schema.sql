DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS customers;

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        customer_id BIGINT,
                        asset_name TEXT,
                        size NUMERIC,
                        price NUMERIC,
                        order_side VARCHAR,
                        status VARCHAR,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE assets (
                        id SERIAL PRIMARY KEY,
                        customer_id BIGINT,
                        asset_name TEXT,
                        size NUMERIC,
                        usable_size NUMERIC,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              customer_id BIGINT,
                              transaction_type VARCHAR(50),
                              amount NUMERIC,
                              iban VARCHAR(255),
                              created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);