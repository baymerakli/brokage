INSERT INTO customers (name, email) VALUES ('John Doe', 'john.doe@brokage.com');
INSERT INTO customers (name, email) VALUES ('Adem Yavuz', 'adem.yavuz@brokage.com');

INSERT INTO orders (customer_id, asset_name, size, price, order_side, status) VALUES
                                                                                  (1, 'USDTRY', 1000, 8.50, 'BUY', 'PENDING'),
                                                                                  (2, 'EURTRY', 500, 10.00, 'SELL', 'MATCHED'),
                                                                                  (1, 'GBPTRY', 300, 11.50, 'BUY', 'CANCELED'),
                                                                                  (2, 'JPYTRY', 100000, 0.08, 'BUY', 'PENDING'),
                                                                                  (1, 'AUDTRY', 700, 6.50, 'SELL', 'PENDING'),
                                                                                  (2, 'CADTRY', 600, 7.00, 'SELL', 'MATCHED'),
                                                                                  (1, 'CHFTRY', 400, 9.20, 'BUY', 'CANCELED'),
                                                                                  (2, 'CNYTRY', 2000, 1.30, 'SELL', 'PENDING'),
                                                                                  (1, 'SEKTRY', 1500, 1.00, 'BUY', 'PENDING'),
                                                                                  (2, 'NOKTRY', 800, 1.10, 'SELL', 'MATCHED');
