package com.liemily.report.domain;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class Report {
    private String reportContents;

    public Report(String reportContents) {
        this.reportContents = reportContents;
    }

    public String getReportContents() {
        return reportContents;
    }

}
