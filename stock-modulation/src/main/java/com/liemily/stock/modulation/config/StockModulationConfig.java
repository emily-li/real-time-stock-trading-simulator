package com.liemily.stock.modulation.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.context.annotation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Emily Li on 23/09/2017.
 */
@Configuration
@ComponentScan("com.liemily.stock.modulation")
@Import(StockConfig.class)
@Lazy
public class StockModulationConfig {
    @Bean
    protected ScheduledExecutorService scheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
