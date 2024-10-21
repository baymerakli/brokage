INSERT INTO customers (name, email) VALUES ('John Doe', 'john.doe@brokage.com');
INSERT INTO customers (name, email) VALUES ('Adem Yavuz', 'adem.yavuz@brokage.com');

INSERT INTO orders (customer_id, asset_name, size, price, order_side, status) VALUES
                                                                                  (1, 'USD', 1000, 8.50, 'BUY', 'PENDING'),
                                                                                  (2, 'EUR', 500, 10.00, 'SELL', 'MATCHED'),
                                                                                  (1, 'GBP', 300, 11.50, 'BUY', 'CANCELED'),
                                                                                  (2, 'JPY', 100000, 0.08, 'BUY', 'PENDING'),
                                                                                  (1, 'AUD', 700, 6.50, 'SELL', 'PENDING'),
                                                                                  (2, 'CAD', 600, 7.00, 'SELL', 'MATCHED'),
                                                                                  (1, 'CHF', 400, 9.20, 'BUY', 'CANCELED'),
                                                                                  (2, 'CNY', 2000, 1.30, 'SELL', 'PENDING'),
                                                                                  (1, 'SEK', 1500, 1.00, 'BUY', 'PENDING'),
                                                                                  (2, 'NOK', 800, 1.10, 'SELL', 'MATCHED');
