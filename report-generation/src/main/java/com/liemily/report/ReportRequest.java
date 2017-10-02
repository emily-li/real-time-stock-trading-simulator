package com.liemily.report;

import org.springframework.data.domain.Sort;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class ReportRequest {
    private ReportName reportName;
    private Sort sort;
    private String[] searchTerms;

    public ReportRequest(ReportName reportName, String... searchTerms) {
        this(reportName, null, searchTerms);
    }

    public ReportRequest(ReportName reportName, Sort sort, String... searchTerms) {
        this.reportName = reportName;
        this.sort = sort == null ? getDefaultSort() : sort;
        this.searchTerms = searchTerms;
    }

    public Sort getSort() {
        return sort;
    }

    public ReportName getReportName() {
        return reportName;
    }

    public String[] getSearchTerms() {
        return searchTerms;
    }

    private Sort getDefaultSort() {
        return new Sort(Sort.Direction.ASC, "company.name");
    }
}
