package com.liemily.report.domain;

import org.springframework.data.domain.Sort;

public class StockReportRequest extends ReportRequest {
    private String[] searchTerms;

    public StockReportRequest(FileType fileType, String... searchTerms) {
        this(fileType, null, searchTerms);
    }

    public StockReportRequest(FileType fileType, Sort sort, String... searchTerms) {
        super(fileType, sort);
        this.searchTerms = searchTerms;
    }

    public String[] getSearchTerms() {
        return searchTerms;
    }
}
