DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS stock_as_of_details;
DROP VIEW  IF EXISTS stock_view;
DROP EVENT IF EXISTS stock_as_of_details_open_update;
DROP EVENT IF EXISTS stock_as_of_details_close_update;

SET GLOBAL event_scheduler = ON;

CREATE TABLE stock (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    value         DECIMAL(65,2)   NOT NULL,
    volume        INT             NOT NULL
);

CREATE TABLE stock_as_of_details (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    open          DECIMAL(65,2),
    close         DECIMAL(65,2)
);

CREATE VIEW stock_view AS
    SELECT stock.symbol, (value - open) AS gains
    FROM stock LEFT JOIN stock_as_of_details ON stock.symbol=stock_as_of_details.symbol;