package com.liemily.stock;

import com.liemily.stock.domain.StockView;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Emily Li on 17/09/2017.
 */
public interface StockViewRepository extends JpaRepository<StockView, String> {
}
