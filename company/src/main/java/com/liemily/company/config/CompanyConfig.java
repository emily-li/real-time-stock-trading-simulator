package com.liemily.company.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Emily Li on 22/09/2017.
 */
@Configuration
@ComponentScan("com.liemily.company")
@EnableJpaRepositories("com.liemily.company")
@EntityScan("com.liemily.company")
@Lazy
public class CompanyConfig {
}
