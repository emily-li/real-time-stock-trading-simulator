package com.liemily.stock.repository;

import com.liemily.stock.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Emily Li on 22/09/2017.
 */
public interface CompanyRepository extends JpaRepository<Company, String> {
}
