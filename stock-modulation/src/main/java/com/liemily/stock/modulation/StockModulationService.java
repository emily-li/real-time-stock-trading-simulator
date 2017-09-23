package com.liemily.stock.modulation;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Lazy
public class StockModulationService {
    private ScheduledExecutorService scheduledExecutorService;
    private StockModulator stockModulator;

    public StockModulationService(ScheduledExecutorService scheduledExecutorService,
                                  StockModulator stockModulator) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.stockModulator = stockModulator;
    }

    @PostConstruct
    public void run() {
        scheduledExecutorService.scheduleAtFixedRate(stockModulator, 0, 1, TimeUnit.MILLISECONDS);
    }
}
