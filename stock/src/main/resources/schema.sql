DROP TABLE IF EXISTS `stock`;
DROP TABLE IF EXISTS `stock_view`;

CREATE TABLE IF NOT EXISTS stock (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    value         DECIMAL(65,2)   NOT NULL,
    volume        INT             NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_view (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    open_value    DECIMAL(65,2)   NOT NULL
);