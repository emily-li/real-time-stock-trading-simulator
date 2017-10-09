INSERT INTO company             (symbol, name)                      VALUES ("testSymbol", "testName");
INSERT INTO stock               (symbol, value, volume)             VALUES ("testSymbol", 1, 1);
INSERT INTO stock_as_of_details (symbol, open_value, close_value)   VALUES ("testSymbol", 1, 1);

INSERT INTO trade(id, stock_symbol, volume, trade_date_time, username, value)
VALUES (1 , "testSymbol" , 1 , "2000-01-01 01:00:00" , "username", 1);