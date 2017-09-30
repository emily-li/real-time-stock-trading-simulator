package com.liemily.stock.repository;

import com.liemily.stock.domain.StockView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Emily Li on 17/09/2017.
 */
public interface StockViewRepository extends JpaRepository<StockView, String> {
    @Query("FROM StockView WHERE stock.volume > 0")
    List<StockView> findAllWithVolumeGreaterThan0(Pageable pageable);
}
