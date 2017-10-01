package com.liemily.report;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class ReportGenerationService {
    private StockViewService stockViewService;
    private UserStockService userStockService;

    @Autowired
    public ReportGenerationService(StockViewService stockViewService, UserStockService userStockService) {
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
    }

    public Report generate(ReportRequest reportRequest) {
        List<StockView> stockViews = new ArrayList<>();
        switch (reportRequest.getReportName()) {
            case STOCK:
                stockViews = stockViewService.getStocksBySymbolOrderByName(reportRequest.getSearchTerms());
                break;
        }
        return new Report(stockViews);
    }
}
