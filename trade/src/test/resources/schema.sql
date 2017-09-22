DROP TABLE IF EXISTS trade;

CREATE TABLE IF NOT EXISTS trade (
  id                INTEGER     NOT NULL PRIMARY KEY AUTO_INCREMENT,
  stock_symbol      VARCHAR(50) NOT NULL,
  trade_date_time   DATETIME(6) NOT NULL
);