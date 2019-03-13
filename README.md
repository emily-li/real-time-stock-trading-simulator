# real-time-stock-trading-simulator
Real-Time Stock Trading Simulator developed from MSc project specifications and requirements

## Setup
* Expects running MySQL service
* Login details should be specified in
    * resources/application.properties
    * resources-test/application.properties
* Confirmed working with Java version 1.8.0_201-b09

## Tests
* Unit tests can be run with ```mvn clean test```
* Integration tests can be run with ```mvn clean integration-test```