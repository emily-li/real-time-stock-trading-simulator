package com.liemily.stock.generation;

import com.liemily.stock.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@Lazy
public class StockGenerationRandomiser {
    private Random random;
    private int minValue;
    private int maxValue;

    @Autowired
    public StockGenerationRandomiser(@Value("${stock.generation.value.min}") int minValue,
                                     @Value("${stock.generation.value.max}") int maxValue,
                                     Random random) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = random;
    }

    Stock randomise(String stockId) {
        BigDecimal value = new BigDecimal(random.nextInt((maxValue - minValue) + 1) + minValue);
        return new Stock(stockId, value, 2500000);
    }
}
