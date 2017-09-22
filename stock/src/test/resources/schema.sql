DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS stock_as_of_details;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS trade;

CREATE TABLE IF NOT EXISTS stock (
    symbol      VARCHAR(50)     NOT NULL    PRIMARY KEY,
    value       DECIMAL(65, 2)  NOT NULL,
    volume      INTEGER         NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_as_of_details (
    symbol      VARCHAR(50)     NOT NULL    PRIMARY KEY,
    open_value  DECIMAL(65, 2),
    close_value DECIMAL(65, 2)
);

CREATE TABLE IF NOT EXISTS company (
    symbol      VARCHAR(50)     NOT NULL    PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL
);

CREATE TABLE IF NOT EXISTS trade (
  id                INTEGER     NOT NULL PRIMARY KEY AUTO_INCREMENT,
  stock_symbol      VARCHAR(50) NOT NULL,
  trade_date_time   DATETIME(6) NOT NULL
);

CREATE OR REPLACE VIEW stock_view AS
    SELECT
        stock.symbol,
        (value - open_value) AS gains,
        MAX(trade_date_time) AS last_trade_date_time
    FROM stock
        LEFT JOIN stock_as_of_details ON stock.symbol=stock_as_of_details.symbol
        LEFT JOIN trade ON stock.symbol=trade.stock_symbol