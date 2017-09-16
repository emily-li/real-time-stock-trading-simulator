DROP TABLE IF EXISTS `stock`;

CREATE TABLE IF NOT EXISTS `stock` (
    `symbol`        VARCHAR(50)     NOT NULL PRIMARY KEY,
    `value`         DECIMAL(65,2)   NOT NULL
);