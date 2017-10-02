package com.liemily.report.domain;

import org.springframework.data.domain.Sort;

/**
 * Created by Emily Li on 01/10/2017.
 */
public abstract class ReportRequest {
    private FileType fileType;
    private Sort sort;

    public ReportRequest(FileType fileType) {
        this(fileType, null);
    }

    public ReportRequest(FileType fileType, Sort sort) {
        this.fileType = fileType;
        this.sort = sort == null ? getDefaultSort() : sort;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Sort getSort() {
        return sort;
    }

    Sort getDefaultSort() {
        return new Sort(Sort.Direction.ASC, "company.name");
    }
}
