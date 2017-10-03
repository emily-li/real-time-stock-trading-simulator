package com.liemily.report;

import com.liemily.report.domain.FileType;
import com.liemily.report.domain.ReportItem;
import com.liemily.report.domain.ReportItems;
import com.liemily.report.exception.ReportGenerationException;
import com.liemily.report.exception.ReportMarshallingException;
import com.liemily.stock.domain.StockItem;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
@Lazy
public class ReportWriter {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    String generateReport(List<? extends StockItem> stockItems, FileType fileType) throws ReportGenerationException {
        ReportItems reportItems = generateReportItems(stockItems);

        String contents;
        if (fileType.equals(FileType.XML)) {
            contents = generateXML(reportItems);
        } else if (fileType.equals(FileType.CSV)) {
            contents = generateCSV(reportItems);
        } else {
            throw new ReportGenerationException("Unsupported report file type " + fileType);
        }
        return contents;
    }

    private ReportItems generateReportItems(List<? extends StockItem> stockItems) {
        List<ReportItem> reportItems = new ArrayList<>();
        stockItems.forEach(stock -> reportItems.add(new ReportItem(stock.getSymbol(), stock.getName(), stock.getValue(), stock.getVolume(), stock.getLastTradeDateTime(), stock.getGains(), stock.getOpenValue(), stock.getCloseValue())));
        return new ReportItems(reportItems);
    }

    private String generateCSV(ReportItems reportItems) throws ReportMarshallingException {
        try (Writer stringWriter = new StringWriter()) {
            stringWriter.write(ReportItem.getCsvHeader());
            stringWriter.append("\n");

            StatefulBeanToCsv<ReportItem> reportItemToCsv = new StatefulBeanToCsvBuilder<ReportItem>(stringWriter).build();
            reportItemToCsv.write(reportItems.getStock());
            return stringWriter.toString();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            throw new ReportMarshallingException("Failed to marshall CSV", e);
        }
    }

    private String generateXML(ReportItems reportItems) throws ReportMarshallingException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReportItems.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            try (StringWriter stringWriter = new StringWriter()) {
                marshaller.marshal(reportItems, stringWriter);
                return stringWriter.toString();
            }
        } catch (JAXBException | IOException e) {
            String msg = "Failed to marshal report to string";
            logger.info(msg, e);
            throw new ReportMarshallingException(msg, e);
        }
    }
}
