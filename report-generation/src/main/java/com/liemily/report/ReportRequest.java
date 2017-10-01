package com.liemily.report;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class ReportRequest {
    private ReportName reportName;
    private String[] searchTerms;

    public ReportRequest(ReportName reportName, String... searchTerms) {
        this.reportName = reportName;
        this.searchTerms = searchTerms;
    }

    public ReportName getReportName() {
        return reportName;
    }

    public String[] getSearchTerms() {
        return searchTerms;
    }
}
