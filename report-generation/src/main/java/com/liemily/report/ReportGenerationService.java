package com.liemily.report;

import com.liemily.report.domain.Report;
import com.liemily.report.domain.ReportRequest;
import com.liemily.report.domain.StockReportRequest;
import com.liemily.report.domain.UserStockReportRequest;
import com.liemily.report.exception.ReportGenerationException;
import com.liemily.stock.domain.StockItem;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.service.UserStockService;
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
    private ReportWriter reportWriter;

    @Autowired
    public ReportGenerationService(StockViewService stockViewService,
                                   UserStockService userStockService,
                                   ReportWriter reportWriter) {
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
        this.reportWriter = reportWriter;
    }

    public Report generate(ReportRequest reportRequest) throws ReportGenerationException {
        List<? extends StockItem> stockItems = getStockItems(reportRequest);
        String reportContents = reportWriter.generateReport(stockItems, reportRequest.getFileType());
        return new Report(reportContents);
    }

    private List<? extends StockItem> getStockItems(ReportRequest reportRequest) {
        List<? extends StockItem> stockItems = new ArrayList<>();
        Sort sort = reportRequest.getSort();

        if (reportRequest instanceof StockReportRequest) {
            StockReportRequest stockReportRequest = (StockReportRequest) reportRequest;
            String[] searchTerms = stockReportRequest.getSearchTerms();
            stockItems = (searchTerms == null || searchTerms.length == 0) ? stockViewService.getStocks(sort) : stockViewService.getStocksBySymbol(searchTerms, sort);
        } else if (reportRequest instanceof UserStockReportRequest) {
            UserStockReportRequest userStockReportRequest = (UserStockReportRequest) reportRequest;
            String username = userStockReportRequest.getUsername();
            stockItems = userStockService.getUserStocksOrderByCompanyName(username);
        }
        return stockItems;
    }
}
