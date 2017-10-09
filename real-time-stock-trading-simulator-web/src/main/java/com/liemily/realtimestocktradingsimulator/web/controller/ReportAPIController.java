package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.report.ReportGenerationService;
import com.liemily.report.domain.*;
import com.liemily.report.exception.ReportGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Created by Emily Li on 09/10/2017.
 */
@RestController
public class ReportAPIController {
    private final ReportGenerationService reportGenerationService;

    @Autowired
    public ReportAPIController(ReportGenerationService reportGenerationService) {
        this.reportGenerationService = reportGenerationService;
    }

    @RequestMapping("report/stock")
    private String generateStockReport(@RequestParam FileType fileType) throws ReportGenerationException {
        ReportRequest reportRequest = new StockReportRequest(fileType);
        Report report = reportGenerationService.generate(reportRequest);
        return report.getReportContents();
    }

    @RequestMapping("report/user")
    private String generateUserStockReport(@RequestParam FileType fileType) throws ReportGenerationException {
        Sort sort = new Sort(ASC, "company.name");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ReportRequest reportRequest = new UserStockReportRequest(fileType, sort, username);
        Report report = reportGenerationService.generate(reportRequest);
        return report.getReportContents();
    }
}