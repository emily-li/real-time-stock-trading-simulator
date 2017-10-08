DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS stock_as_of_details;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS trade;
DROP TABLE IF EXISTS user_stock;

CREATE TABLE IF NOT EXISTS company (
    symbol          VARCHAR(50)     NOT NULL    PRIMARY KEY,
    name            VARCHAR(50)     NOT NULL
);

CREATE TABLE IF NOT EXISTS stock (
    symbol          VARCHAR(50)     NOT NULL    PRIMARY KEY,
    value           DECIMAL(65, 2)  NOT NULL,
    volume          INTEGER         NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_as_of_details (
    symbol          VARCHAR(50)     NOT NULL    PRIMARY KEY,
    open_value      DECIMAL(65, 2),
    close_value     DECIMAL(65, 2)
);

CREATE TABLE IF NOT EXISTS USER (
    username        VARCHAR(255)     NOT NULL    PRIMARY KEY,
    password        VARCHAR(255)    NOT NULL,
    enabled         BOOLEAN         NOT NULL,
    role            VARCHAR(50)     NOT NULL,
    credits         DECIMAL(65, 2),
    email           VARCHAR(255),
    forename        VARCHAR(255),
    surname         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_stock (
  username          VARCHAR(50)     NOT NULL,
  symbol            VARCHAR(50)     NOT NULL,
  volume            INT             NOT NULL
);

CREATE TABLE IF NOT EXISTS trade (
  id                BIGINT          NOT NULL    PRIMARY KEY AUTO_INCREMENT,
  stock_symbol      VARCHAR(50)     NOT NULL,
  username          VARCHAR(50)     NOT NULL,
  volume            INT             NOT NULL,
  trade_date_time   DATETIME(6)     NOT NULL
);

CREATE OR REPLACE VIEW stock_view AS
    SELECT
        UPPER(stock.symbol) AS symbol,
        (value - open_value) AS gains,
        last_trade_date_time
    FROM
        stock
            LEFT JOIN
        stock_as_of_details ON stock.symbol = stock_as_of_details.symbol
            LEFT JOIN
        (SELECT
            stock_symbol, MAX(trade_date_time) AS last_trade_date_time
        FROM
            trade
        GROUP BY stock_symbol) last_trades ON stock.symbol = last_trades.stock_symbol