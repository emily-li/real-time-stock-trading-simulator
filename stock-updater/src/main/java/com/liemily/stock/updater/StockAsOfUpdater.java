package com.liemily.stock.updater;

import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by Emily Li on 18/09/2017.
 */
@Component
public class StockAsOfUpdater {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final long SCHEDULE_FREQUENCY = 24 * 60 * 60 * 1_000L;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final Map<STOCK_AS_OF, String> asOfUpdateTimes = new EnumMap<>(STOCK_AS_OF.class);
    private final StockAsOfDetailsRepository stockAsOfDetailsRepository;
    private StockRepository stockRepository;

    @Autowired
    public StockAsOfUpdater(StockRepository stockRepository, StockAsOfDetailsRepository stockAsOfDetailsRepository, @Value("${updater.openTime}") String openTime, @Value("${updater.closeTime}") String closeTime) {
        this.stockRepository = stockRepository;
        this.stockAsOfDetailsRepository = stockAsOfDetailsRepository;
        asOfUpdateTimes.put(STOCK_AS_OF.OPEN, openTime);
        asOfUpdateTimes.put(STOCK_AS_OF.CLOSE, closeTime);
    }

    @PostConstruct
    void run() {
        setupScheduler();
    }

    private void setupScheduler() {
        for (Map.Entry<STOCK_AS_OF, String> stockAsOfTime : asOfUpdateTimes.entrySet()) {
            logger.info("Setting up scheduled update for " + stockAsOfTime.getKey() + " for time " + stockAsOfTime.getValue());
            long initialDelay = getScheduleTime(stockAsOfTime.getValue()).toMillis();
            StockAsOfUpdateRunnable updater = new StockAsOfUpdateRunnable(stockRepository, stockAsOfDetailsRepository, stockAsOfTime.getKey());

            logger.info("Scheduling updater for " + stockAsOfTime.getKey() + " to run in " + initialDelay + "ms every 24 hours");
            scheduledExecutorService.scheduleAtFixedRate(updater, initialDelay, SCHEDULE_FREQUENCY, TimeUnit.MILLISECONDS);
        }
    }

    Duration getScheduleTime(String time) {
        LocalDateTime now = LocalDateTime.now();

        int[] splitTime = Stream.of(time.split(":")).mapToInt(Integer::parseInt).toArray();
        LocalDateTime runTime = LocalDateTime.now().withHour(splitTime[0]).withMinute(splitTime[1]).withSecond(splitTime[2]);
        runTime = runTime.compareTo(now) > 0 ? runTime : runTime.plusDays(1);

        return Duration.between(now, runTime);
    }
}
