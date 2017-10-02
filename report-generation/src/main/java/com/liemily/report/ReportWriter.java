package com.liemily.report;

import com.liemily.report.domain.FileType;
import com.liemily.report.domain.ReportItems;
import com.liemily.report.exception.ReportGenerationException;
import com.liemily.report.exception.ReportMarshallingException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Lazy
public class ReportWriter {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    void write(ReportItems reportItems, FileType fileType, Path path) throws ReportGenerationException {
        String reportItemsString = generateReport(reportItems, fileType);
        writeFile(reportItemsString, path);
    }

    private String generateReport(ReportItems reportItems, FileType fileType) throws ReportGenerationException {
        String contents;
        if (fileType.equals(FileType.XML)) {
            contents = generateXML(reportItems);
        } else {
            throw new ReportGenerationException("Unsupported report file type " + fileType);
        }
        return contents;
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

    private void writeFile(String reportContents, Path path) throws ReportGenerationException {
        try {
            Files.write(path, reportContents.getBytes());
        } catch (IOException e) {
            logger.info("Failed to write report to " + path.toAbsolutePath());
            throw new ReportGenerationException("Failed to write report to " + path.toAbsolutePath(), e);
        }
        logger.info("Wrote report to " + path.toAbsolutePath());
    }
}
