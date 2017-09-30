package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * C.S20 The user should be able to order stocks in ascending or descending direction given a field
 * Created by Emily Li on 30/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksControllerSortIT {
    @Autowired
    private StocksController stocksController;

    @Autowired
    private StockService stockService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private Model model;
    private String username;
    private Principal principal;

    private Sort sort;
    private int comparison;
    private BigDecimal firstExpectedValue;

    @Before
    public void setup() {
        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;

        BigDecimal smallValue = new BigDecimal("-" + Math.random());
        smallValue = smallValue.setScale(2, RoundingMode.CEILING);
        BigDecimal smallerValue = smallValue.multiply(new BigDecimal(2));
        smallerValue = smallerValue.setScale(2, RoundingMode.CEILING);

        Stock smallStock = new Stock(UUID.randomUUID().toString(), smallValue, 1);
        Stock smallerStock = new Stock(UUID.randomUUID().toString(), smallerValue, 1);
        stockService.save(smallerStock);
        stockService.save(smallStock);

        Company smallCompany = new Company(smallStock.getSymbol(), "b" + UUID.randomUUID());
        Company smallerCompany = new Company(smallerStock.getSymbol(), "a" + UUID.randomUUID());
        companyService.save(smallCompany);
        companyService.save(smallerCompany);

        StockAsOfDetails smallStockAsOfDetails = new StockAsOfDetails(smallStock);
        smallStockAsOfDetails.setOpenValue(new BigDecimal(5));
        smallStockAsOfDetails.setCloseValue(new BigDecimal(10));
        StockAsOfDetails smallerStockAsOfDetails = new StockAsOfDetails(smallerStock);
        smallerStockAsOfDetails.setOpenValue(new BigDecimal(4));
        smallerStockAsOfDetails.setCloseValue(new BigDecimal(6));
        stockAsOfDetailsRepository.save(smallStockAsOfDetails);
        stockAsOfDetailsRepository.save(smallerStockAsOfDetails);

        UserStock smallUserStock = new UserStock(username, smallStock.getSymbol(), 1);
        UserStock smallerUserStock = new UserStock(username, smallerStock.getSymbol(), 1);
        userStockService.save(smallUserStock);
        userStockService.save(smallerUserStock);

        Trade smallTrade = new Trade(smallStock.getSymbol());
        Trade smallerTrade = new Trade(smallerStock.getSymbol());
        tradeService.save(smallTrade);
        tradeService.save(smallerTrade);
    }

    /**
     * Tests stocks can be ordered by value
     */
    @Test
    public void testOrderStocksByValue() {
        final String property = "stock.value";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by open value
     */
    @Test
    public void testOrderStocksByOpenValue() {
        final String property = "stockAsOfDetails.openValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getOpenValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by close value
     */
    @Test
    public void testOrderStocksByCloseValue() {
        final String property = "stockAsOfDetails.closeValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getCloseValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by gains
     */
    @Test
    public void testOrderStocksByGains() {
        final String property = "gains";
        int stocksWithGains = 0;
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stockViews = getOrderedStocks();
            for (StockView stockView : stockViews) {
                if (stockView.getGains() != null) {
                    stocksWithGains++;
                    assertTrue(firstExpectedValue.compareTo(stockView.getGains()) == comparison);
                }
            }
        }
        assertTrue(stocksWithGains >= 2);
    }

    /**
     * Tests stocks can be ordered by volume
     */
    @Test
    public void testOrderStocksByVolume() {
        final String property = "stock.volume";
        for (Sort.Direction direction : Sort.Direction.values()) {
            int firstExpectedVol = direction.isAscending() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(Integer.compare(firstExpectedVol, stockView.getVolume()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by symbol
     */
    @Test
    public void testOrderStocksBySymbol() {
        final String property = "symbol";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();

            String prevSymbol = stocks.get(0).getSymbol();
            for (int i = 1; i < stocks.size(); i++) {
                String nextSymbol = stocks.get(i).getSymbol();
                int symbolCompare = prevSymbol.compareTo(nextSymbol) < 0 ? -1 : 1;
                assertTrue(symbolCompare == comparison);
                prevSymbol = nextSymbol;
            }
        }
    }

    /**
     * Tests stocks can be ordered by company name
     */
    @Test
    public void testOrderStocksByName() {
        final String property = "company.name";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();

            String prevName = stocks.get(0).getName();
            for (int i = 1; i < stocks.size(); i++) {
                String nextName = stocks.get(i).getName();
                int symbolCompare = prevName.compareTo(nextName) < 0 ? -1 : 1;
                assertTrue(symbolCompare == comparison);
                prevName = nextName;
            }
        }
    }

    /**
     * Tests stocks can be ordered by last trade date time
     */
    @Test
    public void testOrderStocksByLastTradeDateTime() {
        final String property = "last_trade_date_time";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            int datesFound = 0;

            Date prevDate = null;
            for (int i = 1; i < stocks.size(); i++) {
                Date date = stocks.get(i).getLastTradeDateTime();
                if (date != null) {
                    datesFound++;
                    if (prevDate == null) {
                        prevDate = date;
                    } else {
                        assertTrue(prevDate.compareTo(date) == comparison);
                        prevDate = date;
                    }
                }
            }
            assertTrue(datesFound >= 2);
        }
    }

    /**
     * Tests user stocks can be ordered by value
     */
    @Test
    public void testOrderUserStocksByValue() {
        final String property = "stockView.stock.value";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by open value
     */
    @Test
    public void testOrderUserStocksByOpenValue() {
        final String property = "stockView.stockAsOfDetails.openValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getOpenValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by volume
     */
    @Test
    public void testOrderUserStocksByVolume() {
        final String property = "volume";
        for (Sort.Direction direction : Sort.Direction.values()) {
            int firstExpectedVol = direction.isAscending() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(Integer.compare(firstExpectedVol, userStock.getVolume()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by close value
     */
    @Test
    public void testOrderUserStocksByCloseValue() {
        final String property = "stockView.stockAsOfDetails.closeValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getCloseValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by symbol
     */
    @Test
    public void testOrderUserStocksBySymbol() {
        final String property = "symbol";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> stocks = getOrderedUserStocks();
            String prevSymbol = stocks.get(0).getSymbol();
            for (int i = 1; i < stocks.size(); i++) {
                String nextSymbol = stocks.get(i).getSymbol();
                int symbolCompare = prevSymbol.compareTo(nextSymbol) < 0 ? -1 : 1;
                assertTrue(symbolCompare == comparison);
                prevSymbol = nextSymbol;
            }
        }
    }

    /**
     * Tests user stocks can be ordered by company name
     */
    @Test
    public void testOrderUserStocksByName() {
        final String property = "stockView.company.name";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> stocks = getOrderedUserStocks();

            String prevSymbol = stocks.get(0).getName();
            for (int i = 1; i < stocks.size(); i++) {
                String nextSymbol = stocks.get(i).getName();
                int symbolCompare = prevSymbol.compareTo(nextSymbol) < 0 ? -1 : 1;
                assertTrue(symbolCompare == comparison);
                prevSymbol = nextSymbol;
            }
        }
    }

    /**
     * Tests stocks can be ordered by last trade date time
     */
    @Test
    public void testOrderUserStocksByLastTradeDateTime() {
        final String property = "stockView.lastTradeDateTime";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> stocks = getOrderedUserStocks();

            Date prevDate = stocks.get(0).getLastTradeDateTime();
            for (int i = 1; i < stocks.size(); i++) {
                Date nextDate = stocks.get(i).getLastTradeDateTime();
                assertTrue(prevDate.compareTo(nextDate) == comparison);
                prevDate = nextDate;
            }
        }
    }

    /**
     * Tests stocks can be ordered by gains
     */
    @Test
    public void testOrderUserStocksByGains() {
        final String property = "stockView.gains";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> stocks = getOrderedUserStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getGains()) == comparison));
        }
    }

    private List<StockView> getOrderedStocks() {
        model = new ExtendedModelMap();
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, sort), null, null);
        return (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
    }

    private List<UserStock> getOrderedUserStocks() {
        model = new ExtendedModelMap();
        stocksController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE, sort), null);
        return (List<UserStock>) model.asMap().get(stocksController.getStocksAttribute());
    }

    private void setupTest(Sort.Direction direction, String property) {
        sort = new Sort(direction, property);
        comparison = direction.isAscending() ? -1 : 1;
        firstExpectedValue = direction.isAscending() ? new BigDecimal(Integer.MIN_VALUE) : new BigDecimal(Integer.MAX_VALUE);
    }
}
