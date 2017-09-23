package com.liemily.stock.modulation;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockModulationService {
    private ScheduledExecutorService scheduledExecutorService;
    private StockModulator stockModulator;

    public StockModulationService(ScheduledExecutorService scheduledExecutorService,
                                  StockModulator stockModulator) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.stockModulator = stockModulator;
    }

    public void run() {
        scheduledExecutorService.scheduleAtFixedRate(stockModulator, 0, 1, TimeUnit.MILLISECONDS);
    }
}
