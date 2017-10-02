package com.liemily.report.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Report")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItems {
    private List<ReportItem> reportItems;

    private ReportItems() {
    }

    public ReportItems(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }

    public List<ReportItem> getReportItems() {
        return reportItems;
    }
}
