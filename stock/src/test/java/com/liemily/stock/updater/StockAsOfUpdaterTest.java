package com.liemily.stock.updater;

import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Emily Li on 20/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockAsOfUpdaterTest {
    private StockAsOfUpdater stockAsOfUpdater;

    @Value("${updater.openTime}")
    private String openTime;
    @Value("${updater.closeTime}")
    private String closeTime;

    @Before
    public void setup() {
        stockAsOfUpdater = spy(new StockAsOfUpdater(mock(StockRepository.class), mock(StockAsOfDetailsRepository.class), openTime, closeTime));
        doCallRealMethod().when(stockAsOfUpdater).run();
        stockAsOfUpdater.run();
    }

    /**
     * S.S06 - Stocks should have field 'Open' with field 'Value' as of 0800
     */
    @Test
    public void testOpenUpdatesAt0800() {
        final String EXPECTED_TIME = "08:00:00";
        assertTrue(openTime.equals(EXPECTED_TIME));
        verify(stockAsOfUpdater, times(1)).getScheduleTime(EXPECTED_TIME);
    }


    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     */
    @Test
    public void testCloseUpdatesAt1630() throws Exception {
        final String EXPECTED_TIME = "16:30:00";
        assertTrue(closeTime.equals(EXPECTED_TIME));
        verify(stockAsOfUpdater, times(1)).getScheduleTime(EXPECTED_TIME);
    }
}
