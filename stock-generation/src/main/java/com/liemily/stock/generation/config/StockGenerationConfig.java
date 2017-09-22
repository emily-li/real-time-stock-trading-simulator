package com.liemily.stock.generation.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by Emily Li on 22/09/2017.
 */
@Configuration
@Import(StockConfig.class)
@Lazy
public class StockGenerationConfig {
}
