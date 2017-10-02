package com.liemily.report;

import com.liemily.report.domain.*;
import com.liemily.report.exception.ReportGenerationException;
import com.liemily.stock.domain.StockItem;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        Path reportContents = getReportPath(reportRequest, stockItems);
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

    private Path getReportPath(ReportRequest reportRequest, List<? extends StockItem> stockItems) throws ReportGenerationException {
        ReportItems reportItems = generateReportItems(stockItems);
        Path reportPath = generateReportPath(reportRequest.getFileType());
        reportWriter.write(reportItems, reportRequest.getFileType(), reportPath);
        return reportPath;
    }

    private ReportItems generateReportItems(List<? extends StockItem> stockItems) {
        List<ReportItem> reportItems = new ArrayList<>();
        stockItems.forEach(stock -> reportItems.add(new ReportItem(stock.getSymbol(), stock.getName(), stock.getValue(), stock.getVolume(), stock.getLastTradeDateTime(), stock.getGains(), stock.getOpenValue(), stock.getCloseValue())));
        return new ReportItems(reportItems);
    }

    private Path generateReportPath(FileType fileType) {
        Path path = Paths.get(UUID.randomUUID().toString() + "." + fileType.toString().toLowerCase());
        path.toFile().deleteOnExit();
        return path;
    }
}
