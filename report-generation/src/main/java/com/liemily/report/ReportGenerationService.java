package com.liemily.report;

import com.liemily.stock.service.StockService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class ReportGenerationService {
    private StockService stockService;
    private UserStockService userStockService;

    @Autowired
    public ReportGenerationService(StockService stockService, UserStockService userStockService) {
        this.stockService = stockService;
        this.userStockService = userStockService;
    }

    public Report generate(ReportRequest reportRequest) {
        return null;
    }
}
