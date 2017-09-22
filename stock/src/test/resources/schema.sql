DROP TABLE IF EXISTS stock_as_of_details;

CREATE TABLE IF NOT EXISTS stock (
  symbol VARCHAR(50)    NOT NULL  PRIMARY KEY,
  value  DECIMAL(65, 2) NOT NULL,
  volume INT            NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_as_of_details (
  symbol     VARCHAR(50) NOT NULL  PRIMARY KEY,
  open_value  DECIMAL(65, 2),
  close_value DECIMAL(65, 2)
);

CREATE OR REPLACE VIEW stock_view AS
    SELECT
        stock.symbol,
        (value - open_value) AS gains
    FROM stock LEFT JOIN stock_as_of_details ON stock.symbol=stock_as_of_details.symbol;