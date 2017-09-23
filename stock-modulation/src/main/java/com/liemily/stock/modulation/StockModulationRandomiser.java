package com.liemily.stock.modulation;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class StockModulationRandomiser {
    public double numberGen() {
        return Math.random();
    }
}
