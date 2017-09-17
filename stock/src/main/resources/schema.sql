DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS stock_as_of_details;
DROP VIEW  IF EXISTS stock_view;
DROP EVENT IF EXISTS stock_as_of_details_update;

CREATE TABLE stock (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    value         DECIMAL(65,2)   NOT NULL,
    volume        INT             NOT NULL
);

CREATE TABLE stock_as_of_details (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    open_value    DECIMAL(65,2),
    close_value   DECIMAL(65,2)
);

CREATE VIEW stock_view AS
    SELECT stock.symbol, (open_value - value) AS gains
    FROM stock INNER JOIN stock_details;

CREATE EVENT stock_as_of_details_update
	ON SCHEDULE AT curdate() + INTERVAL 8 HOUR
    DO
		INSERT INTO stock_status
        SELECT symbol, value FROM stock;