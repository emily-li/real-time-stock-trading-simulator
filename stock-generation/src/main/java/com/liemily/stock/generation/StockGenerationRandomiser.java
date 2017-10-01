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
    private int minVol;
    private int maxVol;

    @Autowired
    public StockGenerationRandomiser(@Value("${stock.generation.value.min}") int minValue,
                                     @Value("${stock.generation.value.max}") int maxValue,
                                     @Value("${stock.generation.vol.min}") int minVol,
                                     @Value("${stock.generation.vol.max}") int maxVol,
                                     Random random) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minVol = minVol;
        this.maxVol = maxVol;
        this.random = random;
    }

    Stock randomise(String stockId) {
        BigDecimal value = new BigDecimal(random.nextInt((maxValue - minValue) + 1) + minValue);
        int vol = random.nextInt((maxVol - minVol) + 1) + minVol;
        return new Stock(stockId, value, vol);
    }
}
