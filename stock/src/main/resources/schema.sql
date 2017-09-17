DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS stock_details;
DROP EVENT IF EXISTS stock_details_update;

CREATE TABLE IF NOT EXISTS stock (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    value         DECIMAL(65,2)   NOT NULL,
    volume        INT             NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_details (
    symbol        VARCHAR(50)     NOT NULL  PRIMARY KEY,
    open_value    DECIMAL(65,2)   NOT NULL
);

CREATE EVENT IF NOT EXISTS stock_details_update
	ON SCHEDULE AT curdate() + INTERVAL 8 HOUR
    DO
		INSERT INTO stock_status
        SELECT symbol, value FROM stock;