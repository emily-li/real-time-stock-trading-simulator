package com.liemily.trade.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Emily Li on 25/09/2017.
 */
@Configuration
@ComponentScan("com.liemily.trade")
@EnableJpaRepositories("com.liemily.trade")
@EntityScan("com.liemily.trade")
@Import({StockConfig.class})
@Lazy
public class TradeConfig {
}
