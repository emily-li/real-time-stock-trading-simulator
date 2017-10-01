package com.liemily.stock.updater.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Configuration
@Lazy
@Import(StockConfig.class)
class StockUpdaterConfig {
}
