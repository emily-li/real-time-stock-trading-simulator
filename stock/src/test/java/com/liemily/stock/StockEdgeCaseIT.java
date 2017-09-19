package com.liemily.stock;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Stock edge case tests using min/max values present in the requirements
 * Ensuring that saved values are persisted correctly
 * Created by Emily Li on 16/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockEdgeCaseIT {
    @Autowired
    private StockRepository stockRepository;

    private String id;

    @Before
    public void setup() {
        id = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() {
        stockRepository.delete(id);
    }

    @Test
    public void testValueMin() {
        final BigDecimal MIN = new BigDecimal(5);

        Stock stock = new Stock(id, MIN, 0);
        stockRepository.save(stock);

        Stock persistedStock = stockRepository.findOne(id);
        assertTrue(persistedStock.getValue().compareTo(stock.getValue()) == 0);
    }

    @Test
    public void testValueMax() {
        final BigDecimal MAX = new BigDecimal(550);

        Stock stock = new Stock(id, MAX, 0);
        stockRepository.save(stock);

        Stock persistedStock = stockRepository.findOne(id);
        assertTrue(persistedStock.getValue().compareTo(stock.getValue()) == 0);
    }

    @Test
    public void testVolumeMin() {
        final int MIN = 2000000;

        Stock stock = new Stock(id, new BigDecimal(0), MIN);
        stockRepository.save(stock);

        Stock persistedStock = stockRepository.findOne(id);
        assertEquals(persistedStock.getVolume(), stock.getVolume());
    }

    @Test
    public void testVolumeMax() {
        final int MAX = 1000000000;

        Stock stock = new Stock(id, new BigDecimal(0), MAX);
        stockRepository.save(stock);

        Stock persistedStock = stockRepository.findOne(id);
        assertEquals(persistedStock.getVolume(), stock.getVolume());
    }
}
