package com.liemily.user.config;

import com.liemily.stock.config.StockConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.liemily.user")
@EnableJpaRepositories("com.liemily.user")
@EntityScan("com.liemily.user")
@Import(StockConfig.class)
@Lazy
public class UserConfig {
}
