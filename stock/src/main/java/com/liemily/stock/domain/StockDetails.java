package com.liemily.stock.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * StockDetails interface intended for domain objects that need to provide the given variables
 * For instance, Stock data and UserStock data need to be presented on the web interface
 * And the same information must be given in reports, which are decoupled from the web interface
 * <p>
 * This is an interface as the properties can be got in any way
 * Created by Emily Li on 01/10/2017.
 */
public interface StockDetails {
    String getSymbol();

    String getName();

    BigDecimal getValue();

    int getVolume();

    Date getLastTradeDateTime();

    BigDecimal getGains();

    BigDecimal getOpenValue();

    BigDecimal getCloseValue();
}
