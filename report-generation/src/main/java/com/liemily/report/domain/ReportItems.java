package com.liemily.report.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Report")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItems {
    private List<ReportItem> stock;

    private ReportItems() {
    }

    public ReportItems(List<ReportItem> stock) {
        this.stock = stock;
    }

    public List<ReportItem> getStock() {
        return stock;
    }
}
