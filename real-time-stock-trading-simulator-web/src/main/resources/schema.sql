DROP TABLE IF EXISTS user_stock;

CREATE TABLE IF NOT EXISTS user_stock (
  username VARCHAR(50) NOT NULL,
  symbol   VARCHAR(50) NOT NULL,
  volume   INT         NOT NULL
);