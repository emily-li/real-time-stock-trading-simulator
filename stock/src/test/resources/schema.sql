DROP TABLE IF EXISTS stock_as_of_details;

CREATE TABLE IF NOT EXISTS stock (
  symbol VARCHAR(50)    NOT NULL  PRIMARY KEY,
  value  DECIMAL(65, 2) NOT NULL,
  volume INT            NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_as_of_details (
  symbol     VARCHAR(50) NOT NULL  PRIMARY KEY,
  openValue  DECIMAL(65, 2),
  closeValue DECIMAL(65, 2)
);