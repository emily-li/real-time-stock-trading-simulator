package com.liemily.stock.modulation;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Long running StockModulationService that schedules an update job for modulation
 * Note that this should not be marked as @Lazy
 * This service keeps the app running with its executor service
 */
@Service
class StockModulationService {
    private ScheduledExecutorService scheduledExecutorService;
    private StockModulator stockModulator;

    StockModulationService(ScheduledExecutorService scheduledExecutorService,
                           StockModulator stockModulator) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.stockModulator = stockModulator;
    }

    @PostConstruct
    void run() {
        scheduledExecutorService.scheduleAtFixedRate(stockModulator, 0, 1, TimeUnit.MILLISECONDS);
    }
}
