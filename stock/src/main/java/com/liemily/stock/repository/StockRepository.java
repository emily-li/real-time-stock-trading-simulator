package com.liemily.stock.repository;

import com.liemily.stock.domain.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {
    @Query("FROM Stock WHERE volume > 0")
    List<Stock> findStocksWithVolume();

    @Query("FROM Stock WHERE volume > 0")
    List<Stock> findStocksWithVolume(Pageable pageable);
}
