package com.liemily.report;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockDetails;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests testing creation of reports from StockView and UserStock entities
 * Created by Emily Li on 01/10/2017.
 */
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

    private Company company1;
    private Company company2;
    private User user;

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

        user = new User(UUID.randomUUID().toString());
        userService.save(user);
        UserStock userStock1 = new UserStock(user.getUsername(), company1.getSymbol(), 1);
        UserStock userStock2 = new UserStock(user.getUsername(), company2.getSymbol(), 1);
        userStockService.save(userStock1);
        userStockService.save(userStock2);
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

    }

    /**
     * S.R04 The report service should be able to create a report of current user shares
     */
    @Test
    public void testUserStockReport() {

    }

    /**
     * S.R05 The report service should be able to create a report of a list of company stock details
     */
    @Test
    public void testCompanyStockReport() {

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
