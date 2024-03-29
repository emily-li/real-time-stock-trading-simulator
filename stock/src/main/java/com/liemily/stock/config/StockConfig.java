package com.liemily.stock.config;

import com.liemily.company.config.CompanyConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Configuration
@ComponentScan("com.liemily.stock")
@EnableJpaRepositories("com.liemily.stock")
@EntityScan("com.liemily.stock")
@Import(CompanyConfig.class)
@Lazy
public class StockConfig {
}
