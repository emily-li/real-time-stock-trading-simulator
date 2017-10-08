package com.liemily.realtimestocktradingsimulator.web.config;

import com.liemily.broker.config.BrokerConfig;
import com.liemily.trade.config.TradeConfig;
import com.liemily.user.config.UserConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Configuration
@Import({EmailConfig.class, BrokerConfig.class, TradeConfig.class, UserConfig.class})
class RealTimeStockTradingSimulatorConfig {
}
