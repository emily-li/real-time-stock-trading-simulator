package com.liemily.stock.repository;

import com.liemily.stock.domain.StockView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Emily Li on 17/09/2017.
 */
public interface StockViewRepository extends JpaRepository<StockView, String> {
    @Query
    List<StockView> findByStockVolumeGreaterThan(int volume, Pageable pageable);

    @Query
    List<StockView> findBySymbolContainingIgnoreCaseAndStockVolumeGreaterThan(String symbol, int i, Pageable pageable);

    @Query
    List<StockView> findByCompanyNameContainingIgnoreCaseAndStockVolumeGreaterThan(String name, int i, Pageable stocksPageable);

    @Query
    List<StockView> findByGainsLessThanAndStockVolumeGreaterThan(BigDecimal gains, int i, Pageable stocksPageable);

    @Query
    List<StockView> findByGainsGreaterThanAndStockVolumeGreaterThan(BigDecimal gains, int i, Pageable stocksPageable);

    @Query
    List<StockView> findByStockValueLessThanAndStockVolumeGreaterThan(BigDecimal volume, int i, Pageable stocksPageable);

    @Query
    List<StockView> findByStockValueGreaterThanAndStockVolumeGreaterThan(BigDecimal volume, int i, Pageable stocksPageable);
}
