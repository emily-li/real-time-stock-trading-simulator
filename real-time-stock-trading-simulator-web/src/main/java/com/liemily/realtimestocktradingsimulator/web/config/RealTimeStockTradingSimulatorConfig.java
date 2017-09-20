package com.liemily.realtimestocktradingsimulator.web.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Configuration
@Import(StockConfig.class)
public class RealTimeStockTradingSimulatorConfig {
}
