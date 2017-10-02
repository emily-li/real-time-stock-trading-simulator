package com.liemily.report;

import com.liemily.stock.domain.StockItem;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
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
        List<? extends StockItem> stockDetails = new ArrayList<>();
        Sort sort = reportRequest.getSort();
        String[] searchTerms = reportRequest.getSearchTerms();

        switch (reportRequest.getReportName()) {
            case STOCK:
                stockDetails = (searchTerms == null || searchTerms.length == 0) ? stockViewService.getStocks(sort) : stockViewService.getStocksBySymbol(searchTerms, sort);
                break;
            case USER_STOCK:
                stockDetails = userStockService.getUserStocksOrderByCompanyName(searchTerms[0]);
                break;
        }
        return new Report(stockDetails);
    }
}
