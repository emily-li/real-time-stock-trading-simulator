package com.liemily.report;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.report.domain.*;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockItem;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.User;
import com.liemily.user.domain.UserStock;
import com.liemily.user.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Integration tests testing creation of reports from StockView and UserStock entities
 * Created by Emily Li on 01/10/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportGenerationServiceIT {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private ReportGenerationService reportGenerationService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private Company company1;
    private Company company2;
    private User user;
    private Collection<UserStock> userStocks;


    @Before
    public void setup() {
        company1 = new Company(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        company2 = new Company(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        companyService.save(company1);
        companyService.save(company2);

        Stock stock1 = new Stock(company1.getSymbol(), new BigDecimal(1), 0);
        Stock stock2 = new Stock(company2.getSymbol(), new BigDecimal(1), 0);
        stockService.save(stock1);
        stockService.save(stock2);

        StockAsOfDetails stockAsOfDetails1 = new StockAsOfDetails(stock1);
        stockAsOfDetails1.setOpenValue(new BigDecimal(1));
        StockAsOfDetails stockAsOfDetails2 = new StockAsOfDetails(stock2);
        stockAsOfDetails1.setOpenValue(new BigDecimal(1));
        stockAsOfDetailsRepository.save(stockAsOfDetails1);
        stockAsOfDetailsRepository.save(stockAsOfDetails2);

        user = new User(UUID.randomUUID().toString());
        userService.save(user);
        userStocks = new ArrayList<>();
        userStocks.add(new UserStock(user.getUsername(), company1.getSymbol(), 1));
        userStocks.add(new UserStock(user.getUsername(), company2.getSymbol(), 1));
        userStockService.save(userStocks);

        userStocks = userStockService.getUserStocks(user.getUsername(), null);
    }

    /**
     * S.R01 The company stocks report should be ordered by company name
     */
    @Test
    public void testCompanyStockReportOrderedByCompanyName() throws Exception {
        ReportRequest reportRequest = new StockReportRequest(FileType.XML);
        assertOrderedByCompanyName(reportRequest);
    }

    /**
     * S.R02 The current shares report should be ordered by company name
     */
    @Test
    public void testUserStocksReportOrderedByCompanyName() throws Exception {
        ReportRequest reportRequest = new UserStockReportRequest(FileType.XML, user.getUsername());
        assertOrderedByCompanyName(reportRequest);
    }

    private void assertOrderedByCompanyName(ReportRequest reportRequest) throws Exception {
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockItem> stockDetails = getStocksFromXML(report.getReport());
        assertTrue(stockDetails.size() > 0);

        List<String> companyNames = new ArrayList<>();
        stockDetails.forEach(stockDetail -> companyNames.add(stockDetail.getName()));
        companyNames.removeAll(Collections.singleton(null));
        List<String> orderedCompanyNames = new ArrayList<>(companyNames);
        orderedCompanyNames.removeAll(Collections.singleton(null));
        Collections.sort(orderedCompanyNames);
        assertEquals(orderedCompanyNames, companyNames);
    }

    /**
     * S.R03 The reports must display values: Stock Symbol, Stock Company, Value, Volume, Gains
     */
    @Test
    public void testReportFields() throws Exception {
        ReportRequest reportRequest = new StockReportRequest(FileType.XML, company1.getSymbol());
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockItem> stockDetailLists = getStocksFromXML(report.getReport());
        for (StockItem stockItem : stockDetailLists) {
            assertNotNull(stockItem.getSymbol());
            assertNotNull(stockItem.getName());
            assertNotNull(stockItem.getValue());
            assertTrue(stockItem.getVolume() > -1);
            assertNotNull(stockItem.getGains());
        }
    }

    /**
     * S.R05 The report service should be able to create a report of a list of company stock details
     */
    @Test
    public void testCompanyStockReport() throws Exception {
        ReportRequest reportRequest = new StockReportRequest(FileType.XML);
        Report report = reportGenerationService.generate(reportRequest);

        Collection<String> stocks = getStockSymbols(report);

        assertTrue(stocks.contains(company1.getSymbol().toUpperCase()));
        assertTrue(stocks.contains(company2.getSymbol().toUpperCase()));
    }

    /**
     * S.R06 The report service should be able to create a report of company stock details via stock symbols
     */
    @Test
    public void testCompanyStockReportGeneratedGivenStockSymbol() throws Exception {
        ReportRequest reportRequest = new StockReportRequest(FileType.XML, company1.getSymbol(), company2.getSymbol());
        Report report = reportGenerationService.generate(reportRequest);

        Collection<String> stocks = getStockSymbols(report);

        assertTrue(stocks.contains(company1.getSymbol().toUpperCase()));
        assertTrue(stocks.contains(company2.getSymbol().toUpperCase()));
        assertTrue(stocks.size() == 2);
    }

    /**
     * S.R07 The report service should be able to create a report of stock values in ascending order
     * S.R08 The report service should be able to create a report of stock values in descending order
     */
    @Test
    public void testStockValueReportAsc() throws Exception {
        for (Sort.Direction direction : Sort.Direction.values()) {
            Sort sort = new Sort(direction, "stock.value");
            ReportRequest reportRequest = new StockReportRequest(FileType.XML, sort);
            Report report = reportGenerationService.generate(reportRequest);

            List<? extends StockItem> stockItems = getStocksFromXML(report.getReport());
            List<BigDecimal> values = new ArrayList<>();
            stockItems.forEach(stockDetails -> values.add(stockDetails.getValue()));
            List<BigDecimal> orderedValues = new ArrayList<>(values);
            Collections.sort(orderedValues);
            if (direction.equals(Sort.Direction.DESC)) {
                Collections.reverse(orderedValues);
            }
            assertEquals(orderedValues, values);
        }
    }

    /**
     * S.R04 The report service should be able to create a report of current user shares
     * S.R09 The report service should be able to create reports in XML
     */
    @Test
    public void testXMLReport() throws Exception {
        ReportRequest reportRequest = new UserStockReportRequest(FileType.XML, user.getUsername());
        Report report = reportGenerationService.generate(reportRequest);

        logger.info("Generated XML stock report:\n" + report.getReport());

        Collection<ReportItem> expectedReportItems = new ArrayList<>();
        userStocks.forEach(userStock -> expectedReportItems.add(new ReportItem(userStock.getSymbol(), userStock.getName(), userStock.getValue(), userStock.getVolume(), userStock.getGains())));

        List<ReportItem> marshalledStocks = getStocksFromXML(report.getReport());
        assertTrue(marshalledStocks.containsAll(expectedReportItems));
    }

    /**
     * S.R10 The report service should be able to create reports in CSV
     */
    @Test
    public void testCSVReport() throws Exception {
        ReportRequest reportRequest = new UserStockReportRequest(FileType.CSV, user.getUsername());
        Report report = reportGenerationService.generate(reportRequest);

        logger.info("Generated CSV stock report:\n" + report.getReport());

        String reportContents = report.getReport();

        String expectedHeader = ReportItem.getCsvHeader();
        String actualHeader = reportContents.split("\n")[0];
        assertEquals(expectedHeader, actualHeader);

        userStocks.forEach(userStock -> assertTrue(reportContents.contains(userStock.getSymbol())));
    }

    private List<ReportItem> getStocksFromXML(String xml) throws Exception {
        Path path = generateXMLReportPath();
        Files.write(path, xml.getBytes());
        JAXBContext jaxbContext = JAXBContext.newInstance(ReportItems.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ReportItems reportItems = (ReportItems) unmarshaller.unmarshal(path.toFile());

        List<ReportItem> marshalledStocks = new ArrayList<>();
        reportItems.getStock().forEach(reportItem -> marshalledStocks.add(new ReportItem(reportItem.getSymbol(), reportItem.getName(), reportItem.getValue(), reportItem.getVolume(), reportItem.getGains())));
        return marshalledStocks;
    }

    private Path generateXMLReportPath() {
        Path path = Paths.get(UUID.randomUUID().toString() + "." + FileType.XML.toString().toLowerCase());
        path.toFile().deleteOnExit();
        return path;
    }

    private Collection<String> getStockSymbols(Report report) throws Exception {
        Collection<String> stockSymbols = new ArrayList<>();
        List<? extends StockItem> stockItems = getStocksFromXML(report.getReport());
        stockItems.forEach(stockDetails -> stockSymbols.add(stockDetails.getSymbol()));
        return stockSymbols;
    }
}
