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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
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
    public void testCompanyStockReportOrderedByCompanyName() {
        ReportRequest reportRequest = new StockReportRequest(FileType.CSV);
        assertOrderedByCompanyName(reportRequest);
    }

    /**
     * S.R02 The current shares report should be ordered by company name
     */
    @Test
    public void testUserStocksReportOrderedByCompanyName() {
        ReportRequest reportRequest = new UserStockReportRequest(FileType.CSV, user.getUsername());
        assertOrderedByCompanyName(reportRequest);
    }

    private void assertOrderedByCompanyName(ReportRequest reportRequest) {
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockItem> stockDetails = report.getStockDetails();
        assertTrue(stockDetails.size() > 0);

        List<String> companyNames = new ArrayList<>();
        stockDetails.forEach(stockDetail -> companyNames.add(stockDetail.getName()));
        List<String> orderedCompanyNames = new ArrayList<>(companyNames);
        Collections.sort(orderedCompanyNames);
        assertEquals(orderedCompanyNames, companyNames);
    }

    /**
     * S.R03 The reports must display values: Stock Symbol, Stock Company, Value, Volume, Gains
     */
    @Test
    public void testReportFields() {
        ReportRequest reportRequest = new StockReportRequest(FileType.CSV, company1.getSymbol());
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockItem> stockDetailLists = report.getStockDetails();
        for (StockItem stockItem : stockDetailLists) {
            assertNotNull(stockItem.getSymbol());
            assertNotNull(stockItem.getName());
            assertNotNull(stockItem.getValue());
            assertTrue(stockItem.getVolume() > -1);
            assertNotNull(stockItem.getGains());
        }
    }

    /**
     * S.R04 The report service should be able to create a report of current user shares
     */
    @Test
    public void testUserStockReport() {
        ReportRequest reportRequest = new UserStockReportRequest(FileType.CSV, user.getUsername());
        Report report = reportGenerationService.generate(reportRequest);
        assertTrue(report.getStockDetails().containsAll(userStocks));
    }

    /**
     * S.R05 The report service should be able to create a report of a list of company stock details
     */
    @Test
    public void testCompanyStockReport() {
        ReportRequest reportRequest = new StockReportRequest(FileType.CSV);
        Report report = reportGenerationService.generate(reportRequest);

        Collection<String> stocks = getStockSymbols(report);

        assertTrue(stocks.contains(company1.getSymbol().toUpperCase()));
        assertTrue(stocks.contains(company2.getSymbol().toUpperCase()));
    }

    /**
     * S.R06 The report service should be able to create a report of company stock details via stock symbols
     */
    @Test
    public void testCompanyStockReportGeneratedGivenStockSymbol() {
        ReportRequest reportRequest = new StockReportRequest(FileType.CSV, company1.getSymbol(), company2.getSymbol());
        Report report = reportGenerationService.generate(reportRequest);

        Collection<String> stocks = getStockSymbols(report);

        assertTrue(stocks.contains(company1.getSymbol().toUpperCase()));
        assertTrue(stocks.contains(company2.getSymbol().toUpperCase()));
        assertTrue(stocks.size() == 2);
    }

    private Collection<String> getStockSymbols(Report report) {
        Collection<String> stockSymbols = new ArrayList<>();
        report.getStockDetails().forEach(stockDetails -> stockSymbols.add(stockDetails.getSymbol()));
        return stockSymbols;
    }

    /**
     * S.R07 The report service should be able to create a report of stock values in ascending order
     * S.R08 The report service should be able to create a report of stock values in descending order
     */
    @Test
    public void testStockValueReportAsc() {
        for (Sort.Direction direction : Sort.Direction.values()) {
            Sort sort = new Sort(direction, "stock.value");
            ReportRequest reportRequest = new StockReportRequest(FileType.CSV, sort);
            Report report = reportGenerationService.generate(reportRequest);

            List<BigDecimal> values = new ArrayList<>();
            report.getStockDetails().forEach(stockDetails -> values.add(stockDetails.getValue()));
            List<BigDecimal> orderedValues = new ArrayList<>(values);
            Collections.sort(orderedValues);
            if (direction.equals(Sort.Direction.DESC)) {
                Collections.reverse(orderedValues);
            }
            assertEquals(orderedValues, values);
        }
    }

    /**
     * S.R09 The report service should be able to create reports in XML
     */
    @Test
    public void testXMLReport() throws Exception {
        Collection<ReportItem> expectedReportItems = new ArrayList<>();
        userStocks.forEach(userStock -> expectedReportItems.add(new ReportItem(userStock.getSymbol(), userStock.getName(), userStock.getValue(), userStock.getVolume(), userStock.getLastTradeDateTime(), userStock.getGains(), userStock.getOpenValue(), userStock.getCloseValue())));

        ReportRequest reportRequest = new StockReportRequest(FileType.XML);
        Report report = reportGenerationService.generate(reportRequest);

        String reportContents = report.getReport();
        Path path = Paths.get(UUID.randomUUID().toString() + "." + FileType.XML.toString().toLowerCase());
        path.toFile().deleteOnExit();
        Files.write(path, reportContents.getBytes());

        List<ReportItem> marshalledStocks = getStocksFromXML(path);
        assertTrue(marshalledStocks.containsAll(expectedReportItems));
    }

    private List<ReportItem> getStocksFromXML(Path xmlPath) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ReportItems.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ReportItems reportItems = (ReportItems) unmarshaller.unmarshal(xmlPath.toFile());

        List<ReportItem> marshalledStocks = new ArrayList<>();
        reportItems.getReportItems().forEach(reportItem -> marshalledStocks.add(new ReportItem(reportItem.getSymbol(), reportItem.getName(), reportItem.getValue(), reportItem.getVolume(), reportItem.getLastTradeDateTime(), reportItem.getGains(), reportItem.getOpenValue(), reportItem.getCloseValue())));
        return marshalledStocks;
    }
    /**
     * S.R10 The report service should be able to create reports in CSV
     */
    @Test
    public void testCSVReport() {

    }
}
