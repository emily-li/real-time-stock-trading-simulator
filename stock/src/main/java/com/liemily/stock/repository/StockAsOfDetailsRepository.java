package com.liemily.stock.repository;

import com.liemily.stock.domain.StockAsOfDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Emily Li on 18/09/2017.
 */
public interface StockAsOfDetailsRepository extends JpaRepository<StockAsOfDetails, String> {
}
