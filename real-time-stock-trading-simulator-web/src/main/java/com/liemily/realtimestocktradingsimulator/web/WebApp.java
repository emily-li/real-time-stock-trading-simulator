package com.liemily.realtimestocktradingsimulator.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Emily Li on 16/09/2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
