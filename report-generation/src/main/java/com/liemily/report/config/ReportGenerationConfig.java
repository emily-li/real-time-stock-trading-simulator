package com.liemily.report.config;

import com.liemily.stock.config.StockConfig;
import com.liemily.user.config.UserConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by Emily Li on 01/10/2017.
 */
@Configuration
@ComponentScan("com.liemily.report")
@Import({StockConfig.class, UserConfig.class})
@Lazy
public class ReportGenerationConfig {
}
