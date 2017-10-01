package com.liemily.report;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockDetails;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
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
    }

    /**
     * S.R01 The company stocks report should be ordered by company name
     */
    @Test
    public void testCompanyStockReportOrderedByCompanyName() {
        ReportRequest reportRequest = new ReportRequest(ReportName.STOCK, company1.getSymbol(), company2.getSymbol());
        assertOrderedByCompanyName(reportRequest);
    }

    /**
     * S.R02 The current shares report should be ordered by company name
     */
    @Test
    public void testUserStocksReportOrderedByCompanyName() {
        ReportRequest reportRequest = new ReportRequest(ReportName.USER_STOCK, user.getUsername());
        assertOrderedByCompanyName(reportRequest);
    }

    private void assertOrderedByCompanyName(ReportRequest reportRequest) {
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockDetails> stockDetails = report.getStockDetails();
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
        ReportRequest reportRequest = new ReportRequest(ReportName.STOCK, company1.getSymbol());
        Report report = reportGenerationService.generate(reportRequest);

        List<? extends StockDetails> stockDetailLists = report.getStockDetails();
        for (StockDetails stockDetails : stockDetailLists) {
            assertNotNull(stockDetails.getSymbol());
            assertNotNull(stockDetails.getName());
            assertNotNull(stockDetails.getValue());
            assertTrue(stockDetails.getVolume() > -1);
            assertNotNull(stockDetails.getGains());
        }
    }

    /**
     * S.R04 The report service should be able to create a report of current user shares
     */
    @Test
    public void testUserStockReport() {
        ReportRequest reportRequest = new ReportRequest(ReportName.USER_STOCK, user.getUsername());
        Report report = reportGenerationService.generate(reportRequest);
        assertTrue(report.getStockDetails().containsAll(userStocks));
    }

    /**
     * S.R05 The report service should be able to create a report of a list of company stock details
     */
    @Test
    public void testCompanyStockReport() {
        ReportRequest reportRequest = new ReportRequest(ReportName.STOCK);
        Report report = reportGenerationService.generate(reportRequest);

        Collection<String> stocks = new ArrayList<>();
        report.getStockDetails().forEach(stockDetails -> stocks.add(stockDetails.getSymbol()));

        assertTrue(stocks.contains(company1.getSymbol()));
        assertTrue(stocks.contains(company2.getSymbol()));
    }

    /**
     * S.R06 The report service should be able to create a report of company stock details via stock symbols
     */
    @Test
    public void testCompanyStockReportGeneratedGivenStockSymbol() {

    }

    /**
     * S.R07 The report service should be able to create a report of stock values in ascending order
     */
    @Test
    public void testStockValueReportAsc() {

    }

    /**
     * S.R08 The report service should be able to create a report of stock values in descending order
     */
    @Test
    public void testStockValueReportDesc() {

    }

    /**
     * S.R09 The report service should be able to create reports in XML
     */
    @Test
    public void testXMLReport() {

    }

    /**
     * S.R10 The report service should be able to create reports in CSV
     */
    @Test
    public void testCSVReport() {

    }
}
