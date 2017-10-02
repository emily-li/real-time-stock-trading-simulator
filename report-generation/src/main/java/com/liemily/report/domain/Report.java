package com.liemily.report.domain;

import java.nio.file.Path;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class Report {
    private Path report;

    public Report(Path report) {
        this.report = report;
    }

    public Path getPath() {
        return report;
    }
}
