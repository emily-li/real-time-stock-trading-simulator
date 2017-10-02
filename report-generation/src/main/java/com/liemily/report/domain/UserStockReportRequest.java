package com.liemily.report.domain;

import org.springframework.data.domain.Sort;

public class UserStockReportRequest extends ReportRequest {
    private String username;

    public UserStockReportRequest(FileType fileType, String username) {
        super(fileType);
        this.username = username;
    }

    public UserStockReportRequest(FileType fileType, Sort sort, String username) {
        super(fileType, sort);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
